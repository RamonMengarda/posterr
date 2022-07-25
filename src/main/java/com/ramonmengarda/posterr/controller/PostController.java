package com.ramonmengarda.posterr.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.ramonmengarda.posterr.model.Post;
import com.ramonmengarda.posterr.repository.PostRepository;

@RestController
@RequestMapping("/posts")
@CrossOrigin
public class PostController {
    
    private final PostRepository postRepository;

    public PostController(PostRepository postRepository){
        this.postRepository = postRepository;
    }

    @GetMapping("/page/{pageNumber}")
    public Page<Post> getPosts(@PathVariable int pageNumber){
        //CONSTRAINT: Load posts on chunks of 10 posts, ordered from latest to oldest
        Pageable pageable = PageRequest.of(pageNumber, 10, Sort.by(Sort.Direction.DESC, "createdAt"));

        return postRepository.findAll(pageable);
    }

    @GetMapping("/user/{id}/page/{pageNumber}")
    public Page<Post> getPostsByUser(@PathVariable long id, @PathVariable int pageNumber){
        //CONSTRAINT: Load posts made by user on chunks of 5 posts, orderes from latest to oldest
        Pageable pageable = PageRequest.of(pageNumber, 5);

        return postRepository.findAllByUserId_IdOrderByCreatedAtDesc(id, pageable);
    }

    @GetMapping("/date/{start}/{end}")
    public List<Post> getPostsByDate(@PathVariable String start, @PathVariable String end){

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

        return Lists.newArrayList(postRepository.findAllByCreatedAtBetween(startDate, endDate));
    }

    @GetMapping("/count/{id}")
    public int getCountByUser(@PathVariable long id){

        //CONSTRAINT: Count of number of posts one user has made
        return postRepository.countByUserId_Id(id);
    }

    @PostMapping
    public ResponseEntity createPost(@RequestBody Post post) throws URISyntaxException{

        //CONSTRAINT: A user is not allowed to post more than 5 posts in one day

        //Check if user has 5 or more posts
        if(postRepository.findAllByUserId_IdOrderByCreatedAtDesc(post.getUser().getId(), PageRequest.of(4,1)).hasContent()){
            
            //Get the 24 hour interval
            Calendar c = Calendar.getInstance(); 
			c.setTime(new Date()); 
			c.add(Calendar.HOUR, -24);
			Date twentyFourHoursAgo = c.getTime();
            
            //Check if the fifth post has been published less than 24 hours ago
            if(postRepository.findAllByUserId_IdOrderByCreatedAtDesc(post.getUser().getId(), PageRequest.of(4, 1)).getContent().get(0).getCreatedAt().after(twentyFourHoursAgo)){
                return ResponseEntity.internalServerError().body("Unable to create post - Reached the limit of 5 posts in a 24 hour interval.");
            }
        }

        Post savedPost = postRepository.save(post);
        return ResponseEntity.created(new URI("/posts/" + savedPost.getId())).body(savedPost);
    }
}
