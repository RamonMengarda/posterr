package com.ramonmengarda.posterr;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import com.ramonmengarda.posterr.model.User;
import com.ramonmengarda.posterr.repository.UserRepository;

@SpringBootApplication
public class PosterrApplication {

	@Autowired
	private UserRepository userRepository;

	@Component
	class DataSetup implements ApplicationRunner{
		@Override
		public void run(ApplicationArguments args) throws Exception{
			Date dateNow = new Date();

			Calendar c = Calendar.getInstance(); 
			c.setTime(dateNow);
			c.add(Calendar.HOUR, -1);
			Date nowMinus1Hours = c.getTime();
			c.add(Calendar.HOUR, -1);
			Date nowMinus2Hours = c.getTime();
			c.add(Calendar.HOUR, -1);
			Date nowMinus3Hours = c.getTime();
			
			userRepository.save(User.userBuilder().username("user1").createdAt(dateNow).build());
			userRepository.save(User.userBuilder().username("user2").createdAt(nowMinus1Hours).build());
			userRepository.save(User.userBuilder().username("user3").createdAt(nowMinus2Hours).build());
			userRepository.save(User.userBuilder().username("user4").createdAt(nowMinus3Hours).build());
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(PosterrApplication.class, args);
	}

}
