package com.vvkozlov.emerchantpay.transaction;

import com.vvkozlov.emerchantpay.transaction.api.rest.TransactionController;
import com.vvkozlov.emerchantpay.transaction.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@SpringBootTest
@AutoConfigureMockMvc
class AdminControllerTests {

    @InjectMocks
    private TransactionController adminController;

    @Mock
    private TransactionService adminService;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();
    }
}
