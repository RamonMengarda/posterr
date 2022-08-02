package com.ramonmengarda.posterr.service;

import org.springframework.stereotype.Service;

import com.ramonmengarda.posterr.model.User;
import com.ramonmengarda.posterr.repository.UserRepository;

@Service
public class UserService {
 
    private UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User getUser(Long id) {
        return userRepository.findById(id).orElseThrow(RuntimeException::new);
    }
}
