package com.ramonmengarda.posterr.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.CreatedDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder(builderMethodName = "userBuilder")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
//"user" is a reserved word, therefore the table's gonna be called "usertable" to avoid conflicts 
//CONSTRAINT: usernames should be unique
@Table(name = "usertable", uniqueConstraints = {@UniqueConstraint(columnNames = {"username"})})
public class User {
    
    @Id
    @GeneratedValue
    private long id;

    @NotNull
    //CONSTRAINT: username must be alphanumerical
    @Pattern(regexp = "^[a-zA-Z0-9]+$")
    //CONSTRAINT: username must be 14 characters long max
    @Column(length = 14)
    @Size(max = 14)
    private String username;

    @NotNull
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @OneToMany(mappedBy = "user")
    private Set<Post> posts;
}