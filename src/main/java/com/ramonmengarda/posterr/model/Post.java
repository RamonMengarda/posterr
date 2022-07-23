package com.ramonmengarda.posterr.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.CreatedDate;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonSubTypes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "post")
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes(
    {
        @Type(value = Original.class, name = "original"),
        @Type(value = Repost.class, name = "repost"),
        @Type(value = Quote.class, name = "quote")
    }
)

public abstract class Post {
    
    @Id
    @GeneratedValue
    private long id;

    @NotNull
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    
    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    //CONSTRAINT: content must be 777 characters long max
    @Column(length = 777)
    @Size(max = 777)
    private String content;

    @ManyToOne
    private Post parent;

    @OneToMany(mappedBy = "parent")
    @Transient
    private List<Post> children;
}
