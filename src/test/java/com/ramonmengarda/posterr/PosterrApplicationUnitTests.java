package com.ramonmengarda.posterr;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.util.StreamUtils;

import com.ramonmengarda.posterr.model.Original;
import com.ramonmengarda.posterr.model.Quote;
import com.ramonmengarda.posterr.model.Repost;
import com.ramonmengarda.posterr.model.User;
import com.ramonmengarda.posterr.repository.PostRepository;
import com.ramonmengarda.posterr.repository.UserRepository;

@SpringBootTest
class PosterrApplicationUnitTests {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PostRepository postRepository;

	@Test
	//Validate constraints and business rules about User creation
	void userCreation() {

		//Validate constraint: Only alphanumeric characters can be used for username
		assertThrows(TransactionSystemException.class, () -> {
			userRepository.save(User.userBuilder().username("specialch@r").createdAt(new Date()).build());
		});

		//Validate constraint: Maximum 14 characters for username
		assertThrows(TransactionSystemException.class, () -> {
			userRepository.save(User.userBuilder().username("usernametoolong").createdAt(new Date()).build());
		});
		
		//Validate constraint: Usernames should be unique values 
		assertThrows(DataIntegrityViolationException.class, () -> {
			userRepository.save(User.userBuilder().username("user1").createdAt(new Date()).build());
			userRepository.save(User.userBuilder().username("user1").createdAt(new Date()).build());
		});

		//Validate correct multiple user creation
		assertDoesNotThrow(() -> {
			userRepository.save(User.userBuilder().username("user2").createdAt(new Date()).build());
			userRepository.save(User.userBuilder().username("user3").createdAt(new Date()).build());
			userRepository.save(User.userBuilder().username("user4").createdAt(new Date()).build());
		});
	}

	@Test
	//Validate constraints and business rules about Post creation
	void postCreation() {

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
		c.add(Calendar.HOUR, -19);
		c.add(Calendar.MINUTE, -59);
		c.add(Calendar.SECOND, -59);
		Date nowMinus235959 = c.getTime();

		final User user = User.userBuilder().username("user").createdAt(new Date()).build();
		userRepository.save(user);
		
		Original originalTemplate = Original.originalBuilder().createdAt(nowMinus235959).author(user).content("Original content").build();

		Repost repostOriginalTemplate = Repost.repostOriginalBuilder().createdAt(nowMinus4Hours).author(user).original(originalTemplate).build();

		Quote quoteOriginalTemplate = Quote.quoteOriginalBuilder().createdAt(nowMinus3Hours).author(user).content("Quote Original content").original(originalTemplate).build();

		Repost repostQuoteTemplate = Repost.repostOriginalBuilder().createdAt(nowMinus2Hours).author(user).quote(quoteOriginalTemplate).build();

		Quote quoteRepostTemplate = Quote.quoteOriginalBuilder().createdAt(nowMinus1Hours).author(user).content("Quote Original content").repost(repostOriginalTemplate).build();

		try {
			final String longContent = StreamUtils.copyToString(new ClassPathResource("loremIpsum778chars.txt").getInputStream(), Charset.defaultCharset());

			//Validate constraint: Posts can have a maximum of 777 characters
			assertThrows(TransactionSystemException.class, () -> {			
				postRepository.save(Original.originalBuilder().createdAt(new Date()).author(user).content(longContent).build());
			});
		} catch (IOException e) {
			e.printStackTrace();
		}

		//Validate correct Original creation
		assertDoesNotThrow(() -> {
			postRepository.save(originalTemplate);
		});

		//Validate correct Repost Original creation
		assertDoesNotThrow(() -> {
			postRepository.save(repostOriginalTemplate);
		});

		//Validate correct Repost Quote creation
		assertDoesNotThrow(() -> {
			postRepository.save(repostQuoteTemplate);
		});

		//Validate correct Quote Original creation
		assertDoesNotThrow(() -> {
			postRepository.save(quoteOriginalTemplate);
		});

		//Validate correct Quote Repost creation
		assertDoesNotThrow(() -> {
			postRepository.save(quoteRepostTemplate);
		});

		//Validate constraint: A user is not allowed to post more than 5 posts in one day (including reposts and quote posts)
		//Using canCreatePost method, wich has the same logic as the PostController createPost method that receives the requisition's body as a parameter
		assertFalse(canCreatePost(user, dateNow));
	}

	private boolean canCreatePost(User user, Date date){
		//Check if user has 5 or more posts
		if(postRepository.findAllByUserId_IdOrderByCreatedAtDesc(user.getId()).size() >= 5){
		
			//Get the 24 hour interval
			Calendar c = Calendar.getInstance(); 
			c.setTime(date); 
			c.add(Calendar.HOUR, -24);
			Date twentyFourHoursAgo = c.getTime();
			
			//Check if the fifth post has been published less than 24 hours ago
			if(postRepository.findAllByUserId_IdOrderByCreatedAtDesc(user.getId()).get(4).getCreatedAt().after(twentyFourHoursAgo)){
				return false;
			}
		}

		return true;
	}
}
	
