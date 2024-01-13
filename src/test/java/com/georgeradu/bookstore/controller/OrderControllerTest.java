package com.georgeradu.bookstore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.georgeradu.bookstore.service.OrderService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Clock;
import java.time.LocalDateTime;

import static org.mockito.Mockito.doReturn;

@ExtendWith(SpringExtension.class)
public class OrdersController {
    private final static LocalDateTime NOW = LocalDateTime.now();

    @MockBean
    private Clock clock;
    private Clock fixedClock;
    @Mock
    private OrderService orderService;
    @InjectMocks
    private OrderController orderController;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        fixedClock = Clock.fixed(NOW.toLocalDate().atStartOfDay().toInstant(Clock.systemDefaultZone().getZone().getRules().getOffset(NOW)), Clock.systemDefaultZone().getZone());
        doReturn(fixedClock.instant()).when(clock).instant();
        doReturn(fixedClock.getZone()).when(clock).getZone();
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
        objectMapper = new ObjectMapper();
    }

    @AfterEach
    void tearDown() {}
}
