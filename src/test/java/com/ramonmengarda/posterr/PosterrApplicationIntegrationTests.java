package com.ramonmengarda.posterr;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.aMapWithSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.ramonmengarda.posterr.model.User;
import com.ramonmengarda.posterr.repository.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PosterrApplication.class)
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
@AutoConfigureTestDatabase
public class PosterrApplicationIntegrationTests {
    
    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    @After
    public void resetDb(){
        userRepository.deleteAll();
    }

    @Test
    public void getUser() throws Exception {
        
        User testUser = User.userBuilder().username("user1").createdAt(new Date()).build();
        userRepository.save(testUser);

        mvc.perform(get("/user/{id}", String.valueOf(testUser.getId())).contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$", aMapWithSize(4)))
        .andExpect(jsonPath("$.username", is("user1")));
    }
}
