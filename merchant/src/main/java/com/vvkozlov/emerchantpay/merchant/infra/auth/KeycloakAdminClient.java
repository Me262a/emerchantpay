package com.vvkozlov.emerchantpay.merchant.infra.auth;

import com.vvkozlov.emerchantpay.merchant.service.contract.OAuthServerAdminClient;
import com.vvkozlov.emerchantpay.merchant.service.util.OperationResult;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class KeycloakAdminClient implements OAuthServerAdminClient {

    private static final String REALM_NAME = "emerchantpay";
    private static final String PASSWORD_TYPE = CredentialRepresentation.PASSWORD;
    private static final String DEFAULT_PASSWORD = "password";
    private final Keycloak keycloak;

    public KeycloakAdminClient(Keycloak keycloak) {
        this.keycloak = keycloak;
    }

    @Override
    public OperationResult<String> addUser(String email, List<String> realmRoles) {
        RealmResource realmResource = keycloak.realm(REALM_NAME);
        UsersResource usersResource = realmResource.users();
        UserRepresentation user = buildUserRepresentation(email);

        try (Response createUserResponse = usersResource.create(user)) {
            if (!Response.Status.CREATED.equals(createUserResponse.getStatusInfo().toEnum())) {
                return OperationResult.failure("Failed to create user in Keycloak");
            }

            String userId = extractUserIdFromResponse(createUserResponse);
            UserResource createdUser = usersResource.get(userId);

            //Keycloak password and roles can be set only after the user is created - this is how api currently works.
            //In real application, merchant must be asked to set his password, 2FA, etc.
            setDefaultPasswordForUser(createdUser);
            assignRealmRolesToUser(createdUser, realmRoles, realmResource);

            return OperationResult.success(userId);
        }
    }

    private UserRepresentation buildUserRepresentation(String email) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(email);
        user.setEmail(email);
        user.setEnabled(true);
        return user;
    }

    private String extractUserIdFromResponse(Response createUserResponse) {
        URI location = createUserResponse.getLocation();
        String[] pathSegments = location.getPath().split("/");
        return pathSegments[pathSegments.length - 1];
    }

    private void setDefaultPasswordForUser(UserResource userResource) {
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(PASSWORD_TYPE);
        credential.setValue(KeycloakAdminClient.DEFAULT_PASSWORD);
        credential.setTemporary(false);
        userResource.resetPassword(credential);
    }

    private void assignRealmRolesToUser(UserResource userResource,
                                        List<String> realmRoles,
                                        RealmResource realmResource) {
        List<RoleRepresentation> desiredRoles = realmResource.roles().list();
        List<RoleRepresentation> rolesToAssign = desiredRoles.stream()
                .filter(role -> realmRoles.contains(role.getName()))
                .collect(Collectors.toList());
        userResource.roles().realmLevel().add(rolesToAssign);
    }
}
