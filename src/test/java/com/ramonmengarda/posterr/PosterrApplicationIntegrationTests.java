package com.ramonmengarda.posterr;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.aMapWithSize;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

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

import com.ramonmengarda.posterr.model.Original;
import com.ramonmengarda.posterr.model.Quote;
import com.ramonmengarda.posterr.model.Repost;
import com.ramonmengarda.posterr.model.User;
import com.ramonmengarda.posterr.repository.PostRepository;
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

    @Autowired
    private PostRepository postRepository;

    @After
    public void resetDb(){
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void getUser() throws Exception{
        
        User testUser = User.userBuilder().username("user1").createdAt(new Date()).build();
        userRepository.save(testUser);

        mvc.perform(get("/user/{id}", String.valueOf(testUser.getId())).contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$", aMapWithSize(4)))
        .andExpect(jsonPath("$.username", is("user1")));
    }

    @Test
    public void getPosts() throws Exception{

        User user1 = User.userBuilder().username("user1").createdAt(new Date()).build();
        User user2 = User.userBuilder().username("user2").createdAt(new Date()).build();
        User user3 = User.userBuilder().username("user3").createdAt(new Date()).build();
        
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
        
        Date dateNow = new Date();

		Calendar c = Calendar.getInstance(); 
		c.setTime(dateNow);
		c.add(Calendar.HOUR, -1);
		Date nowMinus1Hours = c.getTime();
		c.add(Calendar.HOUR, -1);
		Date nowMinus2Hours = c.getTime();
		c.add(Calendar.HOUR, -1);
		Date nowMinus3Hours = c.getTime();
		c.add(Calendar.HOUR, -1);
		Date nowMinus4Hours = c.getTime();
        c.add(Calendar.HOUR, -1);
		Date nowMinus5Hours = c.getTime();
        c.add(Calendar.HOUR, -1);
		Date nowMinus6Hours = c.getTime();
        c.add(Calendar.HOUR, -1);
		Date nowMinus7Hours = c.getTime();
        c.add(Calendar.HOUR, -1);
		Date nowMinus8Hours = c.getTime();
        c.add(Calendar.HOUR, -1);
		Date nowMinus9Hours = c.getTime();
        c.add(Calendar.HOUR, -1);
		Date nowMinus10Hours = c.getTime();
        c.add(Calendar.HOUR, -1);
		Date nowMinus11Hours = c.getTime();
        c.add(Calendar.HOUR, -1);
		Date nowMinus12Hours = c.getTime();
        c.add(Calendar.HOUR, -1);
		Date nowMinus13Hours = c.getTime();
        c.add(Calendar.HOUR, -1);
		Date nowMinus14Hours = c.getTime();
        
        Original u1OriginalTemplate = Original.originalBuilder().createdAt(dateNow).author(user1).content("Original content").build();
		Repost u1RepostOriginalTemplate = Repost.repostOriginalBuilder().createdAt(nowMinus1Hours).author(user1).original(u1OriginalTemplate).build();
		Quote u1QuoteOriginalTemplate = Quote.quoteOriginalBuilder().createdAt(nowMinus2Hours).author(user1).content("Quote Original content").original(u1OriginalTemplate).build();
		Repost u1RepostQuoteTemplate = Repost.repostOriginalBuilder().createdAt(nowMinus3Hours).author(user1).quote(u1QuoteOriginalTemplate).build();
		Quote u1QuoteRepostTemplate = Quote.quoteOriginalBuilder().createdAt(nowMinus4Hours).author(user1).content("Quote Repost content").repost(u1RepostOriginalTemplate).build();
        
        postRepository.save(u1OriginalTemplate);
        postRepository.save(u1RepostOriginalTemplate);
        postRepository.save(u1QuoteOriginalTemplate);
        postRepository.save(u1RepostQuoteTemplate);
        postRepository.save(u1QuoteRepostTemplate);

        Original u2OriginalTemplate = Original.originalBuilder().createdAt(nowMinus5Hours).author(user2).content("Original content").build();
		Repost u2RepostOriginalTemplate = Repost.repostOriginalBuilder().createdAt(nowMinus6Hours).author(user2).original(u2OriginalTemplate).build();
		Quote u2QuoteOriginalTemplate = Quote.quoteOriginalBuilder().createdAt(nowMinus7Hours).author(user2).content("Quote Original content").original(u2OriginalTemplate).build();
		Repost u2RepostQuoteTemplate = Repost.repostOriginalBuilder().createdAt(nowMinus8Hours).author(user2).quote(u2QuoteOriginalTemplate).build();
		Quote u2QuoteRepostTemplate = Quote.quoteOriginalBuilder().createdAt(nowMinus9Hours).author(user2).content("Quote Repost content").repost(u2RepostOriginalTemplate).build();
        
        postRepository.save(u2OriginalTemplate);
        postRepository.save(u2RepostOriginalTemplate);
        postRepository.save(u2QuoteOriginalTemplate);
        postRepository.save(u2RepostQuoteTemplate);
        postRepository.save(u2QuoteRepostTemplate);

        Original u3OriginalTemplate = Original.originalBuilder().createdAt(nowMinus10Hours).author(user3).content("Original content").build();
		Repost u3RepostOriginalTemplate = Repost.repostOriginalBuilder().createdAt(nowMinus11Hours).author(user3).original(u3OriginalTemplate).build();
		Quote u3QuoteOriginalTemplate = Quote.quoteOriginalBuilder().createdAt(nowMinus12Hours).author(user3).content("Quote Original content").original(u3OriginalTemplate).build();
		Repost u3RepostQuoteTemplate = Repost.repostOriginalBuilder().createdAt(nowMinus13Hours).author(user3).quote(u3QuoteOriginalTemplate).build();
		Quote u3QuoteRepostTemplate = Quote.quoteOriginalBuilder().createdAt(nowMinus14Hours).author(user3).content("Quote Repost content").repost(u3RepostOriginalTemplate).build();
        
        postRepository.save(u3OriginalTemplate);
        postRepository.save(u3RepostOriginalTemplate);
        postRepository.save(u3QuoteOriginalTemplate);
        postRepository.save(u3RepostQuoteTemplate);
        postRepository.save(u3QuoteRepostTemplate);


        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));

        mvc.perform(get("/posts/page/{pagenumber}", 0).contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.numberOfElements", is(10)))
        .andExpect(jsonPath("$.content.[0].createdAt", is(formatter.format(dateNow) + "+00:00")))
        .andExpect(jsonPath("$.content.[9].createdAt", is(formatter.format(nowMinus9Hours) + "+00:00")));

        mvc.perform(get("/posts/page/{pagenumber}", 1).contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.numberOfElements", is(5)))
        .andExpect(jsonPath("$.content.[0].createdAt", is(formatter.format(nowMinus10Hours) + "+00:00")))
        .andExpect(jsonPath("$.content.[4].createdAt", is(formatter.format(nowMinus14Hours) + "+00:00")));
    }

    @Test
    public void getPostsByUser() throws Exception{

        User user1 = User.userBuilder().username("user1").createdAt(new Date()).build();
        
        userRepository.save(user1);
        
        Date dateNow = new Date();

		Calendar c = Calendar.getInstance(); 
		c.setTime(dateNow);
		c.add(Calendar.HOUR, -1);
		Date nowMinus1Hours = c.getTime();
		c.add(Calendar.HOUR, -1);
		Date nowMinus2Hours = c.getTime();
		c.add(Calendar.HOUR, -1);
		Date nowMinus3Hours = c.getTime();
		c.add(Calendar.HOUR, -1);
		Date nowMinus4Hours = c.getTime();
        c.add(Calendar.HOUR, -1);
		Date nowMinus5Hours = c.getTime();
        c.add(Calendar.HOUR, -1);
		Date nowMinus6Hours = c.getTime();
                
        Original u1OriginalTemplate = Original.originalBuilder().createdAt(dateNow).author(user1).content("Original content").build();
		Repost u1RepostOriginalTemplate = Repost.repostOriginalBuilder().createdAt(nowMinus1Hours).author(user1).original(u1OriginalTemplate).build();
		Quote u1QuoteOriginalTemplate = Quote.quoteOriginalBuilder().createdAt(nowMinus2Hours).author(user1).content("Quote Original content").original(u1OriginalTemplate).build();
		Repost u1RepostQuoteTemplate = Repost.repostOriginalBuilder().createdAt(nowMinus3Hours).author(user1).quote(u1QuoteOriginalTemplate).build();
		Quote u1QuoteRepostTemplate = Quote.quoteOriginalBuilder().createdAt(nowMinus4Hours).author(user1).content("Quote Repost content").repost(u1RepostOriginalTemplate).build();
        Original u1OriginalTemplate2 = Original.originalBuilder().createdAt(nowMinus5Hours).author(user1).content("Original content 2").build();
        Original u1OriginalTemplate3 = Original.originalBuilder().createdAt(nowMinus6Hours).author(user1).content("Original content 3").build();

        postRepository.save(u1OriginalTemplate);
        postRepository.save(u1RepostOriginalTemplate);
        postRepository.save(u1QuoteOriginalTemplate);
        postRepository.save(u1RepostQuoteTemplate);
        postRepository.save(u1QuoteRepostTemplate);
        postRepository.save(u1OriginalTemplate2);
        postRepository.save(u1OriginalTemplate3);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));

        mvc.perform(get("/posts/user/{id}/page/{pageNumber}", String.valueOf(user1.getId()), 0).contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.numberOfElements", is(5)))
        .andExpect(jsonPath("$.content.[0].createdAt", is(formatter.format(dateNow) + "+00:00")))
        .andExpect(jsonPath("$.content.[4].createdAt", is(formatter.format(nowMinus4Hours) + "+00:00")));

        mvc.perform(get("/posts/user/{id}/page/{pageNumber}", String.valueOf(user1.getId()), 1).contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.numberOfElements", is(2)))
        .andExpect(jsonPath("$.content.[0].createdAt", is(formatter.format(nowMinus5Hours) + "+00:00")))
        .andExpect(jsonPath("$.content.[1].createdAt", is(formatter.format(nowMinus6Hours) + "+00:00")));
    }

    @Test
    public void getPostsByDate() throws Exception{

        User user1 = User.userBuilder().username("user1").createdAt(new Date()).build();
        
        userRepository.save(user1);
        
        Date dateNow = new Date();

		Calendar c = Calendar.getInstance(); 
		c.setTime(dateNow);
		c.add(Calendar.HOUR, -1);
		Date nowMinus1Hours = c.getTime();
		c.add(Calendar.HOUR, -1);
		Date nowMinus2Hours = c.getTime();
		c.add(Calendar.HOUR, -1);
		Date nowMinus3Hours = c.getTime();
		c.add(Calendar.HOUR, -1);
		Date nowMinus4Hours = c.getTime();
        c.add(Calendar.HOUR, -1);
		Date nowMinus5Hours = c.getTime();
        c.add(Calendar.HOUR, -1);
		Date nowMinus6Hours = c.getTime();
                
        Original u1OriginalTemplate = Original.originalBuilder().createdAt(dateNow).author(user1).content("Original content").build();
		Repost u1RepostOriginalTemplate = Repost.repostOriginalBuilder().createdAt(nowMinus1Hours).author(user1).original(u1OriginalTemplate).build();
		Quote u1QuoteOriginalTemplate = Quote.quoteOriginalBuilder().createdAt(nowMinus2Hours).author(user1).content("Quote Original content").original(u1OriginalTemplate).build();
		Repost u1RepostQuoteTemplate = Repost.repostOriginalBuilder().createdAt(nowMinus3Hours).author(user1).quote(u1QuoteOriginalTemplate).build();
		Quote u1QuoteRepostTemplate = Quote.quoteOriginalBuilder().createdAt(nowMinus4Hours).author(user1).content("Quote Repost content").repost(u1RepostOriginalTemplate).build();
        Original u1OriginalTemplate2 = Original.originalBuilder().createdAt(nowMinus5Hours).author(user1).content("Original content 2").build();
        Original u1OriginalTemplate3 = Original.originalBuilder().createdAt(nowMinus6Hours).author(user1).content("Original content 3").build();

        postRepository.save(u1OriginalTemplate);
        postRepository.save(u1RepostOriginalTemplate);
        postRepository.save(u1QuoteOriginalTemplate);
        postRepository.save(u1RepostQuoteTemplate);
        postRepository.save(u1QuoteRepostTemplate);
        postRepository.save(u1OriginalTemplate2);
        postRepository.save(u1OriginalTemplate3);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));

        SimpleDateFormat urlFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");

        //The URL timestamp has an accuracy down to the seconds, but the database has to the milisseconds, wich may result in the object on the boundary not being returned
        
        //Both times set
        mvc.perform(get("/posts/date/{start}/{end}", urlFormatter.format(nowMinus5Hours), urlFormatter.format(nowMinus2Hours)).contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$", hasSize(3)));

        //No start date
        mvc.perform(get("/posts/date/0/{end}", urlFormatter.format(nowMinus3Hours)).contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$", hasSize(3)));

        //No end date
        mvc.perform(get("/posts/date/{start}/0", urlFormatter.format(nowMinus3Hours)).contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$", hasSize(4)));

        
    }


}
