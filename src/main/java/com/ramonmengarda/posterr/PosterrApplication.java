package com.ramonmengarda.posterr;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import com.ramonmengarda.posterr.model.Original;
import com.ramonmengarda.posterr.model.User;
import com.ramonmengarda.posterr.repository.PostRepository;
import com.ramonmengarda.posterr.repository.UserRepository;

@SpringBootApplication
public class PosterrApplication {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PostRepository postRepository;

	@Component
	class DataSetup implements ApplicationRunner{
		@Override
		public void run(ApplicationArguments args) throws Exception{

			User user1 = User.userBuilder().username("user1").createdAt(new Date()).build();
			userRepository.save(user1);

			postRepository.save(Original.originalBuilder().createdAt(new Date()).author(user1).content("teste content").build());
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(PosterrApplication.class, args);
	}

}
