var TOKEN_KEY = "tenantJwtToken"
var TENANT_KEY = "c_tenant"
var AppName = "http://localhost:8080/GoOnServices/"
var urlTenant = window.location.pathname.split('/')[2];

function createAuthorizationTokenHeader() {
    var token = getJwtToken();
    if (token) {
        return {"Authorization": token + urlTenant};
    } else {
        return {};
    }
}

function getJwtToken() {
    return localStorage.getItem(TOKEN_KEY + urlTenant.toLowerCase());
}

function getTenant() {
    return localStorage.getItem(TENANT_KEY);
}

function setJwtToken(token, tenant) {
    localStorage.setItem(TOKEN_KEY + urlTenant.toLowerCase(), token);
    localStorage.setItem(TENANT_KEY, tenant.toLowerCase());
}

function removeJwtToken() {
    localStorage.removeItem(TOKEN_KEY + urlTenant);
    localStorage.removeItem(TENANT_KEY);
}