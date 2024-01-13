package com.georgeradu.bookstore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.georgeradu.bookstore.dto.ReviewRequest;
import com.georgeradu.bookstore.dto.ReviewResponse;
import com.georgeradu.bookstore.model.Book;
import com.georgeradu.bookstore.model.Review;
import com.georgeradu.bookstore.model.User;
import com.georgeradu.bookstore.service.ReviewService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
public class ReviewControllerTest {
    private final static LocalDateTime NOW = LocalDateTime.now();
    private Review review;
    private ReviewResponse reviewResponse;

    @MockBean
    private Clock clock;
    private Clock fixedClock;
    @Mock
    private ReviewService reviewService;
    @InjectMocks
    private ReviewController reviewController;
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
        mockMvc = MockMvcBuilders.standaloneSetup(reviewController).build();
        objectMapper = new ObjectMapper();
        var book = new Book();
        book.setId(1L);
        var user = new User();
        user.setId(1L);
        review = new Review(1L, book, user, 5, "reviewValue", LocalDateTime.now(clock), LocalDateTime.now(clock), null);
        reviewResponse = new ReviewResponse(1L, 1L, 1L, 5, "reviewValue");
    }

    @AfterEach
    void tearDown() {}

    @Nested
    @DisplayName("Test getReview endpoint")
    class TestGetReviewEndpoint {
        @Test
        @DisplayName("Should return review")
        void test_getReview_shouldReturnReview() throws Exception {
            // Arrange
            // Act
            doReturn(review).when(reviewService).getReview(1L);
            // Assert
            mockMvc.perform(get("/review/1"))
                   .andExpect(status().isOk())
                   .andExpect(content().contentType("application/json"))
                   .andExpect(jsonPath("$.id").value(reviewResponse.getId()))
                   .andExpect(jsonPath("$.bookId").value(reviewResponse.getBookId()))
                   .andExpect(jsonPath("$.userId").value(reviewResponse.getUserId()))
                   .andExpect(jsonPath("$.rating").value(reviewResponse.getRating()))
                   .andExpect(jsonPath("$.comment").value(reviewResponse.getComment()));
        }
    }

    @Nested
    @DisplayName("Test getReviewsForBook endpoint")
    class TestGetReviewsForBookEndpoint {
        @Test
        @DisplayName("Should return reviews for book")
        void test_getReviewsForBook_shouldReturnReviewsForBook() throws Exception {
            // Arrange
            // Act
            doReturn(List.of(review)).when(reviewService).getReviewsForBook(1L);
            // Assert
            mockMvc.perform(get("/review/for-book/1"))
                   .andExpect(status().isOk())
                   .andExpect(content().contentType("application/json"))
                   .andExpect(jsonPath("$[0].id").value(reviewResponse.getId()))
                   .andExpect(jsonPath("$[0].bookId").value(reviewResponse.getBookId()))
                   .andExpect(jsonPath("$[0].userId").value(reviewResponse.getUserId()))
                   .andExpect(jsonPath("$[0].rating").value(reviewResponse.getRating()))
                   .andExpect(jsonPath("$[0].comment").value(reviewResponse.getComment()));
        }
    }

    @Nested
    @DisplayName("Test getReviewsForUser endpoint")
    class TestGetReviewsForUserEndpoint {
        @Test
        @DisplayName("Should return reviews for user")
        void test_getReviewsForUser_shouldReturnReviewsForUser() throws Exception {
            // Arrange
            // Act
            doReturn(List.of(review)).when(reviewService).getReviewsForUser(1L);
            // Assert
            mockMvc.perform(get("/review/for-user/1"))
                   .andExpect(status().isOk())
                   .andExpect(content().contentType("application/json"))
                   .andExpect(jsonPath("$[0].id").value(reviewResponse.getId()))
                   .andExpect(jsonPath("$[0].bookId").value(reviewResponse.getBookId()))
                   .andExpect(jsonPath("$[0].userId").value(reviewResponse.getUserId()))
                   .andExpect(jsonPath("$[0].rating").value(reviewResponse.getRating()))
                   .andExpect(jsonPath("$[0].comment").value(reviewResponse.getComment()));
        }
    }

    @Nested
    @DisplayName("Test createReview endpoint")
    class TestCreateReviewEndpoint {
        @Test
        @DisplayName("Should create review")
        @WithMockUser(username = "emailValue", roles = {"USER"})
        void test_createReview_shouldCreateReview() throws Exception {
            // Arrange
            var reviewRequest = new ReviewRequest(1L, 5, "reviewValue");
            var reviewResponse = new ReviewResponse(1L, 1L, 1L, 5, "reviewValue");
            // Act
            doReturn(review).when(reviewService).createReview(reviewRequest, "emailValue");
            // Assert
            mockMvc.perform(post("/review")
                           .contentType("application/json")
                           .content(objectMapper.writeValueAsString(reviewRequest)))
                   .andExpect(status().isOk())
                   .andExpect(content().contentType("application/json"))
                   .andExpect(jsonPath("$.id").value(reviewResponse.getId()))
                   .andExpect(jsonPath("$.bookId").value(reviewResponse.getBookId()))
                   .andExpect(jsonPath("$.userId").value(reviewResponse.getUserId()))
                   .andExpect(jsonPath("$.rating").value(reviewResponse.getRating()))
                   .andExpect(jsonPath("$.comment").value(reviewResponse.getComment()));
        }
    }

    @Nested
    @DisplayName("Test updateReview endpoint")
    class TestUpdateReviewEndpoint {
        @Test
        @DisplayName("Should update review")
        @WithMockUser(username = "emailValue", roles = {"USER"})
        void test_updateReview_shouldUpdateReview() throws Exception {
            // Arrange
            var reviewRequest = new ReviewRequest(1L, 5, "reviewValue");
            var reviewResponse = new ReviewResponse(1L, 1L, 1L, 5, "reviewValue");
            // Act
            doReturn(review).when(reviewService).updateReview(1L, reviewRequest, "emailValue");
            // Assert
            mockMvc.perform(put("/review/{id}", 1L)
                           .contentType("application/json")
                           .content(objectMapper.writeValueAsString(reviewRequest)))
                   .andExpect(status().isOk())
                   .andExpect(content().contentType("application/json"))
                   .andExpect(jsonPath("$.id").value(reviewResponse.getId()))
                   .andExpect(jsonPath("$.bookId").value(reviewResponse.getBookId()))
                   .andExpect(jsonPath("$.userId").value(reviewResponse.getUserId()))
                   .andExpect(jsonPath("$.rating").value(reviewResponse.getRating()))
                   .andExpect(jsonPath("$.comment").value(reviewResponse.getComment()));
        }
    }

    @Nested
    @DisplayName("Test deleteReview endpoint")
    class TestDeleteReviewEndpoint {
        @Test
        @DisplayName("Should delete review")
        @WithMockUser(username = "emailValue", roles = {"USER"})
        void test_deleteReview_shouldDeleteReview() throws Exception {
            // Arrange
            // Act
            doNothing().when(reviewService).deleteReview(1L, "emailValue");
            // Assert
            mockMvc.perform(delete("/review/1"))
                   .andExpect(status().isOk());
        }
    }
}
