package com.ramonmengarda.posterr.model;

import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
@DiscriminatorValue("Q")
public class Quote extends Post{
    
    //Quotes have text content and a "parent" post
    //CONSTRAINT: Quotes can reference only Original or Reposts, but not other Quotes
    @Builder(builderMethodName = "quoteOriginalBuilder")    
    public Quote(long id, Date createdAt, User author, String content, Original original){
        super(id, createdAt, author, content, original, null);
    }

    @Builder(builderMethodName = "quoteRepostBuilder")    
    public Quote(long id, Date createdAt, User author, String content, Repost repost){
        super(id, createdAt, author, content, repost, null);
    }
}