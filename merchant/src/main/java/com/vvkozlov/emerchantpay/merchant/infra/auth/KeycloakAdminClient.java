package com.vvkozlov.emerchantpay.merchant.infra.auth;

import com.vvkozlov.emerchantpay.merchant.domain.constants.UserRoles;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The Keycloak server admin client to manage users registered at keycloak.
 */
@Service
public class KeycloakAdminClient implements OAuthServerAdminClient {

    private static final String REALM_NAME = "emerchantpay";
    private static final String PASSWORD_TYPE = CredentialRepresentation.PASSWORD;
    private static final String DEFAULT_PASSWORD = "password";
    private final Keycloak keycloak;

    public KeycloakAdminClient(Keycloak keycloak) {
        this.keycloak = keycloak;
    }

    /**
     * Adds a user to keycloak
     * Sets password - not for real production usage
     *
     * @param email email to be used as user login
     * @param realmRoles keycloak realm role names to be added for user
     * @return keycloak id of added user
     */
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

    /**
     * Removes a user with specified keycloak id
     *
     * @param kcUserId id of user to be removed
     * @return result of operation
     */
    @Override
    public OperationResult<Void> removeMerchantById(String kcUserId) {
        RealmResource realmResource = keycloak.realm(REALM_NAME);
        UsersResource usersResource = realmResource.users();
        UserResource userResource = usersResource.get(kcUserId);

        RoleRepresentation merchantRole = realmResource.roles().get(UserRoles.ROLE_MERCHANT).toRepresentation();
        List<RoleRepresentation> userRoles = userResource.roles().realmLevel().listEffective();

        if (userRoles.contains(merchantRole)) {
            try {
                userResource.remove();
                return OperationResult.success(null);
            } catch (Exception e) {
                return OperationResult.failure("Failed to remove user with ID: " + kcUserId + ". Error: " + e.getMessage());
            }
        } else {
            return OperationResult.failure("User with ID: " + kcUserId + " does not have the ROLE_MERCHANT role.");
        }
    }

    /**
     * Removes all users with specified rolename
     * Dangerous - not for real production app
     * Requires optimization (deletes users one by one - may be limitation of keycloak api)
     *
     * @param roleName rolename of users to be removed
     * @return result of operation
     */
    @Override
    public OperationResult<List<String>> removeAllUsersWithRole(String roleName) {
        RealmResource realmResource = keycloak.realm(REALM_NAME);
        UsersResource usersResource = realmResource.users();

        RoleRepresentation removalRole = realmResource.roles().get(roleName).toRepresentation();
        if (removalRole == null) {
            return OperationResult.failure("Role not found: " + roleName);
        }

        List<UserRepresentation> usersWithMerchantRole = usersResource.search(null, null, null, null, 0, Integer.MAX_VALUE)
                .stream()
                .filter(user -> hasRole(user, removalRole))
                .toList();

        List<String> removedUserIds = new ArrayList<>();

        for (UserRepresentation user : usersWithMerchantRole) {
            String userId = user.getId();
            usersResource.get(userId).remove();
            removedUserIds.add(userId);
        }

        return OperationResult.success(removedUserIds);
    }

    private boolean hasRole(UserRepresentation user, RoleRepresentation role) {
        RealmResource realmResource = keycloak.realm(REALM_NAME);
        List<RoleRepresentation> userRoles = realmResource.users().get(user.getId()).roles().realmLevel().listAll();
        return userRoles.stream().anyMatch(userRole -> userRole.getName().equals(role.getName()));
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
