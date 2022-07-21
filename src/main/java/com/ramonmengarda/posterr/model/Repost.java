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
@DiscriminatorValue("R")
public class Repost extends Post{
    
    //Reposts don't have text content, only a "parent" post
    //CONSTRAINT: Reposts can reference only Original or Quotes, but not other Reposts
    @Builder(builderMethodName = "repostOriginalBuilder")    
    public Repost(long id, Date createdAt, User author, Original original){
        super(id, createdAt, author, null, original, null);
    }

    @Builder(builderMethodName = "repostQuoteBuilder")    
    public Repost(long id, Date createdAt, User author, Quote quote){
        super(id, createdAt, author, null, quote, null);
    }
}