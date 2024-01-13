package com.georgeradu.bookstore.services;

import com.georgeradu.bookstore.repository.BookRepository;
import com.georgeradu.bookstore.service.BookCategoryService;
import com.georgeradu.bookstore.service.BookService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.Clock;
import java.time.LocalDateTime;

import static org.mockito.Mockito.doReturn;

@SpringBootTest
public class BookServiceTest {
    private final static LocalDateTime NOW = LocalDateTime.now();

    @MockBean
    private Clock clock;
    private Clock fixedClock;
    @MockBean
    private BookRepository bookRepository;
    @MockBean
    private BookCategoryService bookCategoryService;
    @Autowired
    private BookService bookService;

    @BeforeEach
    public void setUp() {
        fixedClock = Clock.fixed(NOW.toLocalDate().atStartOfDay().toInstant(Clock.systemDefaultZone().getZone().getRules().getOffset(NOW)), Clock.systemDefaultZone().getZone());
        doReturn(fixedClock.instant()).when(clock).instant();
        doReturn(fixedClock.getZone()).when(clock).getZone();
    }

    @AfterEach
    void tearDown() {}
}
