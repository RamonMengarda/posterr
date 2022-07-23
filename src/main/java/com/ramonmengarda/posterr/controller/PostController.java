package com.ramonmengarda.posterr.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        //CONSTRAINT: Load posts made by user on chunks of 5 posts
        Pageable pageable = PageRequest.of(pageNumber, 5);

        return postRepository.findAllByUserId_IdOrderByCreatedAtDesc(id, pageable);
    }
}
