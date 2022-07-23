package com.ramonmengarda.posterr.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.ramonmengarda.posterr.model.Post;

@RepositoryRestResource(collectionResourceRel = "post", path = "post")
public interface PostRepository extends PagingAndSortingRepository<Post, Long>{
   
    Page<Post> findAllByUserId_IdOrderByCreatedAtDesc(long id, Pageable pageable);

    List<Post> findAllByCreatedAtBetween(Date start, Date end);
}
