package com.georgeradu.bookstore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.georgeradu.bookstore.dto.BookCategoryRequest;
import com.georgeradu.bookstore.dto.BookCategoryResponse;
import com.georgeradu.bookstore.model.BookCategory;
import com.georgeradu.bookstore.service.BookCategoryService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
public class BookCategoryControllerTest {
    private final static LocalDateTime NOW = LocalDateTime.now();

    @MockBean
    private Clock clock;
    private Clock fixedClock;
    @Mock
    private BookCategoryService bookCategoryService;
    @InjectMocks
    private BookCategoryController bookCategoryController;
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
        mockMvc = MockMvcBuilders.standaloneSetup(bookCategoryController).build();
        objectMapper = new ObjectMapper();
    }

    @AfterEach
    void tearDown() {}

    @Nested
    @DisplayName("Test getBookCategory endpoint")
    class TestGetBookCategoryEndpoint {
        @Test
        @DisplayName("Should return book category")
        @WithMockUser(username = "emailValue", roles = {"USER"})
        void test_getBookCategory_shouldReturnBookCategory() throws Exception {
            // Arrange
            var bookCategory = new BookCategory(1L, "nameValue", "descriptionValue", LocalDateTime.now(clock),
                    LocalDateTime.now(clock), null);
            var bookCategoryResponse = new BookCategoryResponse(1L, "nameValue", "descriptionValue");

            // Act
            when(bookCategoryService.getBookCategory(1L)).thenReturn(bookCategory);

            // Assert
            mockMvc
                    .perform(get("/book-category/1"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("application/json"))
                    .andExpect(jsonPath("$.id").value(bookCategoryResponse.getId()))
                    .andExpect(jsonPath("$.name").value(bookCategoryResponse.getName()))
                    .andExpect(jsonPath("$.description").value(bookCategoryResponse.getDescription()));
        }
    }

    @Nested
    @DisplayName("Test getBookCategories endpoint")
    class TestGetBookCategoriesEndpoint {
        @Test
        @DisplayName("Should return all book categories")
        @WithMockUser(username = "emailValue", roles = {"USER"})
        void test_getBookCategories_shouldReturnBookCategories() throws Exception {
            // Arrange
            var bookCategory1 = new BookCategory(1L, "nameValue1", "descriptionValue1", LocalDateTime.now(clock),
                    LocalDateTime.now(clock), null);
            var bookCategory2 = new BookCategory(2L, "nameValue2", "descriptionValue2", LocalDateTime.now(clock),
                    LocalDateTime.now(clock), null);
            var bookCategoryResponse1 = new BookCategoryResponse(1L, "nameValue1", "descriptionValue1");
            var bookCategoryResponse2 = new BookCategoryResponse(2L, "nameValue2", "descriptionValue2");

            // Act
            when(bookCategoryService.getBookCategories()).thenReturn(
                    List.of(new BookCategory[]{bookCategory1, bookCategory2}));

            // Assert
            mockMvc
                    .perform(get("/book-category"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("application/json"))
                    .andExpect(jsonPath("$[0].id").value(bookCategoryResponse1.getId()))
                    .andExpect(jsonPath("$[0].name").value(bookCategoryResponse1.getName()))
                    .andExpect(jsonPath("$[0].description").value(bookCategoryResponse1.getDescription()))
                    .andExpect(jsonPath("$[1].id").value(bookCategoryResponse2.getId()))
                    .andExpect(jsonPath("$[1].name").value(bookCategoryResponse2.getName()))
                    .andExpect(jsonPath("$[1].description").value(bookCategoryResponse2.getDescription()));
        }
    }

    @Nested
    @DisplayName("Test createBookCategory endpoint")
    class TestCreateBookCategoryEndpoint {
        @Test
        @DisplayName("Should create book category")
        @WithMockUser(username = "emailValue", roles = {"ADMIN"})
        void test_createBookCategory_shouldCreateBookCategory() throws Exception {
            // Arrange
            var bookCategoryRequest = new BookCategoryRequest("nameValue", "descriptionValue");
            var bookCategory = new BookCategory(1L, "nameValue", "descriptionValue", LocalDateTime.now(clock),
                    LocalDateTime.now(clock), null);
            var bookCategoryResponse = new BookCategoryResponse(1L, "nameValue", "descriptionValue");

            // Act
            when(bookCategoryService.createBookCategory(bookCategoryRequest)).thenReturn(bookCategory);

            // Assert
            mockMvc
                    .perform(post("/book-category")
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(bookCategoryRequest)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("application/json"))
                    .andExpect(jsonPath("$.id").value(bookCategoryResponse.getId()))
                    .andExpect(jsonPath("$.name").value(bookCategoryResponse.getName()))
                    .andExpect(jsonPath("$.description").value(bookCategoryResponse.getDescription()));
        }
    }

    @Nested
    @DisplayName("Test updateBookCategory endpoint")
    class TestUpdateBookCategoryEndpoint {
        @Test
        @DisplayName("Should update book category")
        @WithMockUser(username = "emailValue", roles = {"ADMIN"})
        void test_updateBookCategory_shouldUpdateBookCategory() throws Exception {
            // Arrange
            var bookCategoryRequest = new BookCategoryRequest("nameValue", "descriptionValue");
            var bookCategory = new BookCategory(1L, "nameValue", "descriptionValue", LocalDateTime.now(clock),
                    LocalDateTime.now(clock), null);
            var bookCategoryResponse = new BookCategoryResponse(1L, "nameValue", "descriptionValue");

            // Act
            when(bookCategoryService.updateBookCategory(1L, bookCategoryRequest)).thenReturn(bookCategory);

            // Assert
            mockMvc
                    .perform(put("/book-category/1")
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(bookCategoryRequest)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("application/json"))
                    .andExpect(jsonPath("$.id").value(bookCategoryResponse.getId()))
                    .andExpect(jsonPath("$.name").value(bookCategoryResponse.getName()))
                    .andExpect(jsonPath("$.description").value(bookCategoryResponse.getDescription()));
        }
    }

    @Nested
    @DisplayName("Test deleteBookCategory endpoint")
    class TestDeleteBookCategoryEndpoint {
        @Test
        @DisplayName("Should delete book category")
        @WithMockUser(username = "emailValue", roles = {"ADMIN"})
        void test_deleteBookCategory_shouldDeleteBookCategory() throws Exception {
            // Arrange

            // Act
            doNothing().when(bookCategoryService).deleteBookCategory(any());

            // Assert
            mockMvc
                    .perform(delete("/book-category/1"))
                    .andExpect(status().isOk());
        }
    }
}
