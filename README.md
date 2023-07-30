# TransPerfect-TechnicalAssignment

I created a RESTful endpoint **/movies** with [MockAPI](https://mockapi.io/), here is the link : https://64c3e89867cfdca3b6607388.mockapi.io/api/v1/movies

This Spring Boot application consumes the endpoint and exposes a new endpoint **/recommendations** that returns a list of recommended movies based on the user's preferences.

The **/recommendations** endpoint accepts a query parameter **genre** that contains the user's preferred movie genre, and it returns a list of movies that belong to the specified genre, sorted by release year in descending order.

The Spring Boot application also contains :

1. Error handling mechanism in case the user provides an empty genre, or in case the application was not able to get movies list with /movies endpoint.
2. Message Source strategy for messages internationalization(so far only for English and French).
3. Logs coverage.
4. Caching mechanism.
5. Swagger documentation ( link : http://localhost:8080/swagger-ui/index.html).
6. Unit Tests for different scenarios.

You can open the application with an IDE of your choice and run it. Alternatively, you can run the Spring Boot application directly using Maven's spring-boot plugin, with the command :
**mvn spring-boot:run**

(Environment requirements : maven, java 17 and above ).