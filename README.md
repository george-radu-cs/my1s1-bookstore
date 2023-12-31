# Bookstore

Radu George-Mihai - SAL 410

Project for Java course from uni.

## Swagger

Check [here](http://localhost:8080/swagger-ui/index.html#/)

## Business Requirements

1. User Registration and Authentication:
   - [ ] Users should be able to register for an account.
   - [ ] Users must be able to log in and log out of their accounts.
   - [ ] The system must support admin accounts in addition to regular user accounts.
2. Browse and Search:
   - [ ] Users should be able to browse books by categories.
   - [ ] The system must provide a search functionality for users to find specific books.
3. Product Listings:
   - [ ] The platform must display detailed information about each book, including the title, author, price, and description.
   - [ ] Users should be able to view book ratings and reviews.
4. Shopping Cart and Checkout:
   - [ ] Users must be able to add books to their shopping cart.
5. Order Management:
   - [ ] Users should have access to their order history.
   - [ ] The order history must include the status of each order.
   - [ ] Users must be able to cancel an order if it has not been shipped.
6. User Reviews and Ratings:
   - [ ] Users should be able to leave reviews and ratings for books.
   - [ ] The system must display average ratings for each book.
   - [ ] Users should be able to edit or delete their reviews.
7. Admin Panel:
   - [ ] The admin panel must provide insights into user activities and sales.
   - [ ] The admin panel must provide a way to manage books.
   - [ ] The admin panel must provide a way to manage orders.
8. Performance:
    - [ ] The system must be able to handle a large number of active users.
    - [ ] The system must be able to handle a large number of orders.
9. Security and Privacy:
    - [ ] The system must ensure the security and privacy of user data.
10. Reliability and Testing:
    - [ ] The system must be highly reliable, ensuring minimal downtime and efficient error handling. 
    - [ ] All critical components of the system must be thoroughly tested through unit tests, integration tests. 
    - [ ] Test coverage should be maintained at a minimum of 90% for core functionalities. 

## MVP Features Document

1. User Authentication and Registration Controller:
   - This controller handles user registration and authentication.
   - User registration and login with email & password. Should contain code for admin login. Should provide either a JWT or a session cookie for authentication.
2. BookController:
   - Manages the retrieval of book information. 
   - Endpoint to get a list of books by category. 
   - Search endpoint for finding books by title or author.
   - Admin endpoints for adding, updating, and deleting books.
3. ShoppingCartController:
   - Manages shopping cart functionality. 
   - Update the state of the shopping cart (add/remove books). 
   - Retrieve and update the contents of the shopping cart.
4. OrderController:
   - Manages user orders. 
   - Retrieve order history for a user.
   - Cancel an order if it has not been shipped.
5. ReviewController:
   - Handles user reviews and ratings. 
   - Allow users to submit reviews and ratings for books. 
   - Retrieve average ratings for each book.

## Data model

1. User:
   - Attributes: Id, Username, Email, Password (hashed), Role, CreatedAt, UpdatedAt, DeletedAt 
   - Relationships:
     - Has one or more Orders
     - Writes zero to many Reviews
     - Has one ShoppingCart
2. Category:
   - Attributes: Id, Name, Description, CreatedAt, UpdatedAt, DeletedAt
   - Relationships:
     - Contains one or more Books
3. Book:
   - Attributes: Id, Title, Author, Description, Price, AverageRating, ...other metadata, Stock
   - Relationships: 
     - Belongs to one or more Categories
     - Can have zero to many Reviews
     - Can be in one or more Orders
     - Can be in one or more ShoppingCarts
4. Review:
   - Attributes: Id, UserId, BookId, Rating, Comment, CreatedAt, UpdatedAt, DeletedAt
   - Relationships:
     - Belongs to one User 
     - Relates to one Book
5. ShoppingCart:
   - Attributes: Id, UserId 
   - Relationships:
     - Belongs to one User
     - Contains zero to many ShoppingCartItems
6. ShoppingCartItem:
   - Attributes: Id, ShoppingCartId, BookId, Quantity 
   - Relationships:
     - Belongs to one ShoppingCart
     - References one Book 
7. Order:
   - Attributes: Id, UserId, OrderDate, Status, DeliveryDate, DeliveryAddress, TotalPrice
   - Relationships:
     - Belongs to one User
     - Contains one or more OrderItems
8. OrderItem:
    - Attributes: Id, OrderId, BookId, Quantity, Price
    - Relationships:
      - Belongs to one Order
      - References one Book

## Getting Started

### Reference Documentation

For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.2.0/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.2.0/maven-plugin/reference/html/#build-image)
* [Spring Web](https://docs.spring.io/spring-boot/docs/3.2.0/reference/htmlsingle/index.html#web)

### Guides

The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)

