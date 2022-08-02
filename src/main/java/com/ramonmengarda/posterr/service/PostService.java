package com.ramonmengarda.posterr.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ramonmengarda.posterr.model.Post;
import com.ramonmengarda.posterr.repository.PostRepository;

@Service
public class PostService {
    
    private PostRepository postRepository;

    public PostService(PostRepository postRepository){
        this.postRepository = postRepository;
    }

    public Page<Post> getPosts(int pageNumber){
        //CONSTRAINT: Load posts on chunks of 10 posts, ordered from latest to oldest
        Pageable pageable = PageRequest.of(pageNumber, 10, Sort.by(Sort.Direction.DESC, "createdAt"));

        return postRepository.findAll(pageable);
    }

    public Page<Post> getPostsByUser(long id, int pageNumber){
        //CONSTRAINT: Load posts made by user on chunks of 5 posts, orderes from latest to oldest
        Pageable pageable = PageRequest.of(pageNumber, 5);

        return postRepository.findAllByUserId_IdOrderByCreatedAtDesc(id, pageable);
    }

    public Page<Post> getPostsByDate(int pageNumber, String start, String end){

        Pageable pageable = PageRequest.of(pageNumber, 10, Sort.by(Sort.Direction.DESC, "createdAt"));

        //CONSTRAINT: Filtering based on the posted Date, both values optional.
        //Default values are set to startDate=190-01-01 00:00:00 and endDate=now, to make both ends optional. The frontend just has to send a non-empty url parameter in order to use the default values.
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");

        Date startDate;
        Date endDate;

        try{
            startDate = formatter.parse(start);
        } catch (ParseException e) {
            startDate = new Date(0);
        }

        try{
            endDate = formatter.parse(end);
        } catch (ParseException e) {
            endDate = new Date();
        }

        return postRepository.findAllByCreatedAtBetween(startDate, endDate, pageable);
    }

    public int getCountByUser(long id){

        //CONSTRAINT: Count of number of posts one user has made
        return postRepository.countByUserId_Id(id);
    }

    public ResponseEntity createPost(Post post) throws URISyntaxException{

        //CONSTRAINT: A user is not allowed to post more than 5 posts in one day

        //Check if user has 5 or more posts
        if(postRepository.findAllByUserId_IdOrderByCreatedAtDesc(post.getUser().getId(), PageRequest.of(4,1)).hasContent()){
            
            //Get the 24 hour interval
            Calendar c = Calendar.getInstance(); 
			c.setTime(new Date()); 
			c.add(Calendar.HOUR, -24);
			Date twentyFourHoursAgo = c.getTime();
            
            //Return fifth page os user posts with only one result per page
            Page<Post> fifthPage = postRepository.findAllByUserId_IdOrderByCreatedAtDesc(post.getUser().getId(), PageRequest.of(4, 1));

            //Get the Post object from the page
            Post fifthPost = fifthPage.getContent().get(0);

            //Check if the fifth post has been published less than 24 hours ago
            if(fifthPost.getCreatedAt().after(twentyFourHoursAgo)){
                return ResponseEntity.internalServerError().body("Unable to create post - Reached the limit of 5 posts in a 24 hour interval.");
            }
        }

        Post savedPost = postRepository.save(post);
        return ResponseEntity.created(new URI("/posts/" + savedPost.getId())).body(savedPost);
    }
}
