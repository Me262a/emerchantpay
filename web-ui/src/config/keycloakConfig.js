const keycloakConfig = {
    url: import.meta.env.VITE_KEYCLOAK_SERVER_URL,
    realm: "emerchantpay",
    clientId: "emerchantpay-web-client"
};

export default keycloakConfig;