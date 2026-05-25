# Shop Warehouse Management System

API built with Spring Boot 3.x, Java 17+, and Supabase (PostgreSQL).

## How to Run
1. Clone the repository.
2. Configure database credentials in `src/main/resources/application.yml` (currently pre-configured with a live Supabase sandbox instance for ease of testing).
3. Run the application: `./mvnw spring-boot:run`

## Interactive API Documentation
Once running, you can fully test and view all endpoints via Swagger UI:
 http://localhost:8080/swagger-ui/index.html

or:
https://durham-mesa-creativity-hanging.trycloudflare.com/swagger-ui/index.html

or if you have postman you can use postman. if you don't have postman, you can use this to import collection
https://dimasfath1994.github.io/postdimtrial

## Design Decisions
- **Java Records:** Used for DTOs to ensure data immutability and zero boilerplate.
- **RFC 7807 Problem Details:** Implemented modern REST exception handling via `ProblemDetail` for standardized API error responses.
- **Bidirectional Mapping:** Handled Item and ItemVariant lifecycle composition directly within the domain entities.
