# posterr
Strider Technical Assessment

Created by Ramon Mengarda.

<hr>

## Running the container

To run this project you need to have Docker installed

* In a terminal, run `docker run -dp 9090:8080 ramonmengarda/posterr`
* Docker will download the image and make the application available, running it on port 9090. If you want to use another port, you can change the `9090` on the last command to the most suited port.

## Running Locally

To run this application locally, you'll need:
* Maven 4.0.0
* Spring Boot 2.7.1
* Docker (if you want to build the image and run it locally)

### Running with Spring Boot
* On a terminal, navigate to the project's root folder
* Run `./mvnw spring-boot:run`
* The application will be available on port 8080

### Running with Docker
* On a terminal, navigate to the project's root folder
* Run `docker build -t ramonmengarda/posterr .`
* Run `docker run -p 9090:8080 ramonmengarda/posterr`
* The application will load and become available on port 9090. If you want to use another port, you can change the `9090` on the last command to the most suited port.

## Project overview and helpful tips

*This overview will assume the application is running on port 8080. If you changed the port bind, please change 8080 to the number of the port you used. A Postman collection is attached to help testing*

`http://localhost:8080/user/{id}`
This endpoint access the user details. There are already 4 Users seeded in the database to make testing easier, they're accessible via ids 1 to 4

`http://localhost:8080/posts/page/{pageNumber}`
The homepage will show a feed of the 10 latest posts and will load another 10 as the user reaches the bottom of the page. The *pageNumber*, starting on 0, divides the chunks.

`http://localhost:8080/posts/user/{id}/page/{pageNumber}`
If the user switches from "All Posts" to "Only Mine", the API returns a feed with 5 posts per page, containing only the user-written posts, ordered from most recent to oldest.
*id* is the User id.*pageNumber*, starting at 0, divides the pages.

`http://localhost:8080/posts/date/{start}/{end}`
This endpoint returns all the posts between *start* and *end*, formatted as `yyyy-MM-dd_HH:mm:ss`. Both parameters are optional, and the way to opt out is to pass a non-formattable string as the parameter, such as `0`, for example.

`http://localhost:8080/posts/count/{id}`
Count the number of posts written by the User with id=*id*.

Using a POST method on `http://localhost:8080/posts` allows the user to create a new Post, passing the object serialized as a JSON as the requisition's body.

The H2 database is accessible at `http://localhost:8080/h2-console`. ~~The credentials are on posterr/src/main/resources/application.properties~~, and give access to a simple yet very useful interface to run queries directly on the database.

Can't figure out if the application is running or not? `http://localhost:8080/actuator/health`

If for some reason you want to see on your console all the queries made by hibernate to the DB, uncomment the two lines at application.properties to enable that feature.

<hr>

## Critique

Posterr has been a really fun application to develop, and quite a challenging one too. In this section, I'll try to explain as best as I can what I think could be done better or differently, and I'll also describe which steps I'd take to scale it.

The first theme I'd like to address is the design pattern. It helps the developer put every snip of code where it belongs, reduces refactoring, helps to spot bugs quicker, and makes the code much easier to understand and maintain. Implementing a pattern and enforcing good practices like SOLID principles would be a huge step towards the scaling up of an application like Posterr. However, the size of the project and the time constraint at this point of the development are good enough justifications for not implementing a more robust design pattern, because it takes longer and adds complexity to the code, which is not needed yet.

Developing an app as small and straightforward as Posterr might lead us away from good practices when it comes to making it safe. For example, the database credentials are written in plain text in the "application.properties" file, and that's clearly not a good practice, amongst many other issues. Spring helps a lot because it has built-in security features, especially for session control and authentication, which were not on the STA scope, or cross-site request forgery, which I bypassed by allowing cross-origin requests. Those are serious concerns when scaling up, and should be addressed as such.

When it comes to databases, H2 is not the benchmark for a development-ready product, but it does a very good job working alongside Spring Boot. As the User pool increases and the Post quantity raises, it's necessary to think about a more robust environment for data-keeping. Running the DB inside a container could help the security and decentralization, but could also bring up other issues like latency, for example, and a more in-depth analysis would be needed to see if a solution like that would be worth it.

Another step that could be taken to scale Posterr would be the implementation of a CI/CD pipeline, like Jenkins or GithubActions, to generate a new build of the container and help version control, and better delivery strategies in the form of A/B or Blue/Green deployment, which would improve the availability and reliability, as well as reduce the system's downtime.
