package com.ramonmengarda.posterr.controller;

import java.net.URISyntaxException;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ramonmengarda.posterr.model.Post;
import com.ramonmengarda.posterr.service.PostService;

@RestController
@RequestMapping("/posts")
@CrossOrigin
public class PostController {
    
    private final PostService postService;

    public PostController(PostService postService){
        this.postService = postService;
    }
    
    @GetMapping
    public Page<Post> getPosts(
        @RequestParam(name = "pageNumber", defaultValue = "0") Optional<Integer> pageNumber,
        @RequestParam(name = "user") Optional<Long> id,
        @RequestParam(name = "startDate", defaultValue = "0") Optional<String> start,
        @RequestParam(name = "endDate", defaultValue = "0") Optional<String> end){
        
            if(pageNumber.isPresent() && !id.isPresent() && !start.isPresent() && !end.isPresent()){
                return postService.getPosts(pageNumber.get());
            } else if(pageNumber.isPresent() && id.isPresent() && !start.isPresent() && !end.isPresent()){
                return postService.getPostsByUser(id.get(), pageNumber.get());
            } else if(pageNumber.isPresent() && !id.isPresent() && start.isPresent() && end.isPresent()){
                return postService.getPostsByDate(pageNumber.get(), start.get(), end.get());
            } else {
                return null;
            }
    }

    @GetMapping("/count/{id}")
    public int getCountByUser(@PathVariable long id){
        return postService.getCountByUser(id);
    }

    @PostMapping
    public ResponseEntity createPost(@RequestBody Post post) throws URISyntaxException{
        return postService.createPost(post);
    }
}
