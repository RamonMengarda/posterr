# posterr
Strider Technical Assessment

Created by Ramon Mengarda.

<hr>

## Running the container

In order to run this project you need to have Docker installed

* In a terminal, run `docker run -dp 9090:8080 ramonmengarda/posterr`
* Docker will download the image and make the application available, running it on port 9090. If you want to use another port, you can change the `9090` on the last command to the most suited port.

## Running Locally

To run this application locally, you'll need:
* Maven 4.0.0
* Spring Boot 2.7.1
* Docker (if you want to build the image and run locally)

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
This endpoint access the user details. There are already 4 Users seeded in the database in order to make testing easier, they're accessible via tha ids 1 to 4

`http://localhost:8080/posts/page/{pageNumber}`
The homepage will show a feed of the 10 latest posts and will load more 10 as the user reaches the bottom of the page. Tha *pageNumber*, starting on 0, divides the chunks.

`http://localhost:8080/posts/user/{id}/page/{pageNumber}`
If the user switches from "All Posts" to "Only Mine", the API returns a feed with 5 posts per page, containing only the user written posts, ordered from most recent to oldest.
*id* is the User id.*pageNumber*, starting at 0, divides the pages.

`http://localhost:8080/posts/date/{start}/{end}`
This endpoint return all the posts between *start* and *end*, formatted as `yyyy-MM-dd_HH:mm:ss`. Both parameters are optional, and the way to opt out is to pass a non formattable string as the parameter, such as `0`, for example.

`http://localhost:8080/posts/count/{id}`
Count the number of posts written by the User with id=*id*.

Using a POST method on `http://localhost:8080/posts` allows the user to create a new Post, passing the object serialized as a JSON as the requisition's body.

The H2 database is accessible at `http://localhost:8080/h2-console`. ~~The credentials are on posterr/src/main/resources/application.properties~~, and give access to a simple yet very useful interface to run queries directly on the database.

Can't figure out if the application is running or not? `http://localhost:8080/actuator/health`

If for some reason you want to see on your console all the queries made by hibernate to the DB, uncomment the two lines at application.properties to enable that feature.
