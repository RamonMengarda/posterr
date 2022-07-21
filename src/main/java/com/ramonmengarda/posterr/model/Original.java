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
@DiscriminatorValue("O")
public class Original extends Post{

    //Original posts only have text content
    @Builder(builderMethodName = "originalBuilder")    
    public Original(long id, Date createdAt, User author, String content){
        super(id, createdAt, author, content, null, null);
    }
}