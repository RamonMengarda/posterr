package com.ramonmengarda.posterr.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

        Date startDate = new Date(0);
        Date endDate = new Date();

        try{
            startDate = formatter.parse(start);
            endDate = formatter.parse(end);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return Lists.newArrayList(postRepository.findAllByCreatedAtBetween(startDate, endDate));
    }

    @GetMapping("/count/{id}")
    public int getCountByUser(@PathVariable long id){

        //CONSTRAINT: Count of number of posts one user has made
        return postRepository.countByUserId_Id(id);
    }
}
