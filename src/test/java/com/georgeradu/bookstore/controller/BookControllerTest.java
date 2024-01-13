package com.georgeradu.bookstore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.georgeradu.bookstore.dto.BookRequest;
import com.georgeradu.bookstore.dto.BookResponse;
import com.georgeradu.bookstore.model.Book;
import com.georgeradu.bookstore.model.BookCategory;
import com.georgeradu.bookstore.service.BookService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
public class BookControllerTest {
    private final static LocalDateTime NOW = LocalDateTime.now();
    private BookCategory BOOK_CATEGORY1, BOOK_CATEGORY2;
    private Book BOOK1, BOOK2;
    private BookResponse BOOK_RESPONSE1, BOOK_RESPONSE2;

    @MockBean
    private Clock clock;
    private Clock fixedClock;
    @Mock
    private BookService bookService;
    @InjectMocks
    private BookController bookController;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        fixedClock = Clock.fixed(NOW
                        .toLocalDate()
                        .atStartOfDay()
                        .toInstant(Clock.systemDefaultZone().getZone().getRules().getOffset(NOW)),
                Clock.systemDefaultZone().getZone());
        doReturn(fixedClock.instant()).when(clock).instant();
        doReturn(fixedClock.getZone()).when(clock).getZone();
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        BOOK_CATEGORY1 = new BookCategory(1L, "nameValue1", "descriptionValue1",
                LocalDateTime.now(clock), LocalDateTime.now(clock), null);
        BOOK_CATEGORY2 = new BookCategory(2L, "nameValue2", "descriptionValue2",
                LocalDateTime.now(clock), LocalDateTime.now(clock), null);
        BOOK1 = new Book(1L, "titleValue1", "authorValue1", "descriptionValue1", 1, "imageUrl1",
                1, "language1", "publisher1", LocalDateTime.now(clock), "isbn101", "isbn131", "dimensions1", 0,
                BOOK_CATEGORY1, LocalDateTime.now(clock), LocalDateTime.now(clock), null);
        BOOK2 = new Book(2L, "titleValue2", "authorValue2", "descriptionValue2", 2, "imageUrl2",
                2, "language2", "publisher2", LocalDateTime.now(clock), "isbn102", "isbn132", "dimensions2", 0,
                BOOK_CATEGORY1, LocalDateTime.now(clock), LocalDateTime.now(clock), null);
        BOOK_RESPONSE1 = new BookResponse(1L, "titleValue1", "authorValue1",
                "descriptionValue1", 1, "imageUrl1", 1, "language1", "publisher1", LocalDateTime.now(clock), "isbn101",
                "isbn131", "dimensions1", 1L);
        BOOK_RESPONSE2 = new BookResponse(2L, "titleValue2", "authorValue2",
                "descriptionValue2", 2, "imageUrl2", 2, "language2", "publisher2", LocalDateTime.now(clock), "isbn102",
                "isbn132", "dimensions2", 1L);
    }

    @AfterEach
    void tearDown() {}

    @Nested
    @DisplayName("Test getBook endpoint")
    class TestGetBookEndpoint {
        @Test
        @DisplayName("Should return book")
        void test_getBook_shouldReturnBook() throws Exception {
            // Arrange
            var book = BOOK1;
            var bookResponse = BOOK_RESPONSE1;

            // Act
            when(bookService.getBook(book.getId())).thenReturn(book);

            // Assert
            MvcResult actualResult = mockMvc
                    .perform(get("/book/{id}", bookResponse.getId()))
                    .andExpect(status().isOk())
                    .andReturn();

            Assertions.assertEquals(objectMapper.writeValueAsString(bookResponse),
                    actualResult.getResponse().getContentAsString());
        }
    }

    @Nested
    @DisplayName("Test getBooksByCategoryId endpoint")
    class TestGetBooksByCategoryIdEndpoint {
    }

    @Nested
    @DisplayName("Test searchBooks endpoint")
    class TestSearchBooksEndpoint {
        @Test
        @DisplayName("Should return books")
        void test_searchBooks_shouldReturnBooks() throws Exception {
            // Arrange
            var searchString = BOOK1.getTitle();
            var book1 = BOOK1;
            var book2 = BOOK2;
            book2.setAuthor(searchString);
            var bookResponse1 = BOOK_RESPONSE1;
            var bookResponse2 = BOOK_RESPONSE2;
            bookResponse2.setAuthor(searchString);

            // Act
            when(bookService.searchBooks(Optional.of(searchString))).thenReturn(List.of(book1, book2));

            // Assert
            MvcResult actualResult = mockMvc
                    .perform(get("/book/search?searchQuery=" + searchString))
                    .andExpect(status().isOk())
                    .andReturn();


            Assertions.assertEquals(objectMapper.writeValueAsString(List.of(bookResponse1, bookResponse2)),
                    actualResult.getResponse().getContentAsString());
        }
    }

    @Nested
    @DisplayName("Test createBook endpoint")
    class TestCreateBookEndpoint {
        @Test
        @DisplayName("Should return created book")
        void test_createBook_shouldReturnCreatedBook() throws Exception {
            // Arrange
            var book = BOOK1;
            var bookRequest = new BookRequest(book.getTitle(), book.getAuthor(), book.getDescription(),
                    book.getPrice(), book.getImageUrl(), book.getQuantity(), book.getLanguage(), book.getPublisher(),
                    book.getPublicationDate(), book.getIsbn10(), book.getIsbn13(), book.getDimensions(),
                    book.getCategory().getId());
            var bookResponse = BOOK_RESPONSE1;

            // Act
            when(bookService.createBook(bookRequest)).thenReturn(book);

            // Assert
            MvcResult actualResult = mockMvc
                    .perform(post("/book/")
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(bookRequest)))
                    .andExpect(status().isOk())
                    .andReturn();

            Assertions.assertEquals(objectMapper.writeValueAsString(bookResponse),
                    actualResult.getResponse().getContentAsString());
        }
    }

    @Nested
    @DisplayName("Test updateBook endpoint")
    class TestUpdateBookEndpoint {
        @Test
        @DisplayName("Should return updated book")
        void test_updateBook_shouldReturnUpdatedBook() throws Exception {
            // Arrange
            var book = BOOK1;
            var bookRequest = new BookRequest(book.getTitle(), book.getAuthor(), book.getDescription(),
                    book.getPrice(), book.getImageUrl(), book.getQuantity(), book.getLanguage(), book.getPublisher(),
                    book.getPublicationDate(), book.getIsbn10(), book.getIsbn13(), book.getDimensions(),
                    book.getCategory().getId());
            var bookResponse = BOOK_RESPONSE1;

            // Act
            when(bookService.updateBook(book.getId(), bookRequest)).thenReturn(book);

            // Assert
            MvcResult actualResult = mockMvc
                    .perform(put("/book/{id}", book.getId())
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(bookRequest)))
                    .andExpect(status().isOk())
                    .andReturn();

            Assertions.assertEquals(objectMapper.writeValueAsString(bookResponse),
                    actualResult.getResponse().getContentAsString());
        }
    }

    @Nested
    @DisplayName("Test deleteBook endpoint")
    class TestDeleteBookEndpoint {
        @Test
        @DisplayName("Should return ok")
        void test_deleteBook_shouldReturnOk() throws Exception {
            // Arrange
            var book = BOOK1;

            // Act
            doNothing().when(bookService).deleteBook(book.getId());

            // Assert
            mockMvc
                    .perform(delete("/book/{id}", book.getId()))
                    .andExpect(status().isOk());
        }
    }
}
