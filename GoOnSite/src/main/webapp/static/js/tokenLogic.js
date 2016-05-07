var TOKEN_KEY = "jwtToken"
var TENANT_KEY = "c_tenant"
var AppName = "http://localhost:8080/GoOnServices/"
var urlTenant = window.location.pathname.split('/').pop();

function createAuthorizationTokenHeader() {
    var token = getJwtToken();
    if (token) {
        return {"Authorization": token};
    } else {
        return {};
    }
}

function getJwtToken() {
    return localStorage.getItem(TOKEN_KEY);
}

function getTenant() {
    return localStorage.getItem(TENANT_KEY);
}

function setJwtToken(token, tenant) {
    localStorage.setItem(TOKEN_KEY, token);
    localStorage.setItem(TENANT_KEY, tenant);
}

function removeJwtToken() {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(TENANT_KEY);
}