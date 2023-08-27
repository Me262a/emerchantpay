package com.vvkozlov.emerchantpay.merchant;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.test.FluentTestsHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

/**
 * Keycloak integration test for {@link MerchantApplication}.
 *
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class MerchantResourceServerTest {


	@Autowired
	MockMvc mvc;

	private static FluentTestsHelper helper;

	@BeforeAll
	public static void onBeforeClass() {
		helper = new FluentTestsHelper(
				"http://localhost:5451",
				"dev_kc_admin",
				"dev_kc_password",
				"master",
				"admin-cli",
				"emerchantpay-test"
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

/*
	@Test
	void testValidBearerToken() throws Exception {
		this.mvc.perform(get("/api/admin/merchant").with(bearerTokenFor("emp_admin", "emp_admin_password")))
				.andExpect(status().isOk());
				//.andExpect(content().string(containsString("Hello, emp_admin!")));
	}

	@Test
	void testInvalidBearerToken() throws Exception {
		this.mvc.perform(get("/api/admin/merchant"))
				.andExpect(status().isForbidden());
	}*/

	private RequestPostProcessor bearerTokenFor(String username, String password) {
		String token = getToken(username, password);

		return request -> {
            request.addHeader("Authorization", "Bearer " + token);
            return request;
        };
	}

	public String getToken(String username, String password) {
		Keycloak keycloak = Keycloak.getInstance(
				"http://localhost:5451",
				"emerchantpay-test",
				username,
				password,
				"emerchantpay-client",
				"emerchantpay_client_secret");
		return keycloak.tokenManager().getAccessTokenString();
	}
}
