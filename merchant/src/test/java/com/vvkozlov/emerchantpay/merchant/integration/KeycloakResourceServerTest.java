package com.vvkozlov.emerchantpay.merchant.integration;

import com.vvkozlov.emerchantpay.merchant.MerchantApplication;
import com.vvkozlov.emerchantpay.merchant.controller.ui.MerchantUIController;
import com.vvkozlov.emerchantpay.merchant.integration.config.UnitTestSecurityConfig;
import com.vvkozlov.emerchantpay.merchant.integration.config.WebMvcConfig;
import com.vvkozlov.emerchantpay.merchant.service.MerchantService;
import com.vvkozlov.emerchantpay.merchant.service.model.MerchantViewDTO;
import com.vvkozlov.emerchantpay.merchant.service.util.OperationResult;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.test.FluentTestsHelper;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Keycloak integration test for {@link MerchantApplication}.
 * This test requires running dev keycloak instance
 */
@ExtendWith(MockitoExtension.class)
@WebMvcTest(MerchantUIController.class)
@AutoConfigureMockMvc
@Import({WebMvcConfig.class, UnitTestSecurityConfig.class})
public class KeycloakResourceServerTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	private MerchantService merchantService;

	private static FluentTestsHelper helper;

	@BeforeAll
	public static void onBeforeClass() {
		helper = new FluentTestsHelper(
				"http://localhost:5451",
				"dev_kc_admin",
				"dev_kc_password",
				"master",
				"admin-cli",
				"master"
		);

		try {
			helper.init();
			helper.importTestRealm("/keycloak/keycloak-emerchantpay-realm.json");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@AfterAll
	public static void cleanUp() throws Exception {
		helper.deleteRealm("emerchantpay-test");
	}

	@Test
	void testValidBearerTokenAndRoleFromKeycloak() throws Exception {
		String testID = UUID.randomUUID().toString();
		MerchantViewDTO mockMerchant = new MerchantViewDTO();
		mockMerchant.setId(testID);
		when(merchantService.getMerchant(testID)).thenReturn(OperationResult.success(mockMerchant));

		RequestPostProcessor bearerToken = bearerTokenFor("emp_admin", "emp_admin_password");
		this.mockMvc.perform(get("/ui/merchants/" + testID).with(bearerToken))
				.andExpect(status().isOk());
	}

	@Test
	void testNoToken() throws Exception {
		this.mockMvc.perform(get("/ui/merchants/testID"))
				.andExpect(status().isUnauthorized());
	}

	private RequestPostProcessor bearerTokenFor(String username, String password) {
		String token = getToken(username, password);

		return request -> {
            request.addHeader("Authorization", "Bearer " + token);
            return request;
        };
	}

	private String getToken(String username, String password) {
		Keycloak keycloak = Keycloak.getInstance(
				"http://localhost:5451",
				"emerchantpay-test",
				username,
				password,
				"emerchantpay-test-client",
				"emerchantpay_test_client_secret");
		return keycloak.tokenManager().getAccessTokenString();
	}
}
