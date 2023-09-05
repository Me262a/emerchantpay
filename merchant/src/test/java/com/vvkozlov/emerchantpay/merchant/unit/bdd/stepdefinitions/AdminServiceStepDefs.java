package com.vvkozlov.emerchantpay.merchant.unit.bdd.stepdefinitions;

import com.vvkozlov.emerchantpay.merchant.service.AdminService;
import com.vvkozlov.emerchantpay.merchant.service.contract.OAuthServerAdminClient;
import com.vvkozlov.emerchantpay.merchant.service.model.AdminViewDTO;
import com.vvkozlov.emerchantpay.merchant.service.util.OperationResult;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class AdminServiceStepDefs {

    @InjectMocks
    private AdminService adminService;

    @Mock
    private OAuthServerAdminClient oAuthServerAdminClient;

    private OperationResult<List<AdminViewDTO>> result;

    private AutoCloseable closeable;

    @Before
    public void setup() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @After
    public void tearDown() {
        try {
            closeable.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Given("OAuthServer is working properly")
    public void oauthserver_is_working_properly() {
        when(oAuthServerAdminClient.addUser(anyString(), anyList()))
                .thenReturn(OperationResult.success("123456"));
    }

    @Given("OAuthServer throws an exception")
    public void oauthserver_throws_an_exception() {
        when(oAuthServerAdminClient.addUser(anyString(), anyList()))
                .thenThrow(new RuntimeException("Failed to add user"));
    }

    @When("I import admins from CSV")
    public void i_import_admins_from_csv() {
        result = adminService.importAdminsFromCsv();
    }

    @Then("the import is successful and returns a list of 2 admins")
    public void the_import_is_successful_and_returns_a_list_of_2_admins() {
        assertTrue(result.isSuccess());
        assertEquals(2, result.getResult().size());
    }

    @Then("the import fails and an error is returned")
    public void the_import_fails_and_an_error_is_returned() {
        assertFalse(result.isSuccess());
        assertTrue(result.getErrors().stream().findFirst().orElse("")
                .contains("An error occurred while importing admins"));
    }
}

