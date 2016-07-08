var TOKEN_KEY = "tenantJwtToken"
var AppName = "http://localhost:8080/GoOnServices/"
var urlTenant = window.location.pathname.split('/')[2];

function createAuthorizationTokenHeader() 
{
    var token = getJwtToken();
    if (token) 
    {
        return {"Authorization": token + urlTenant};
    } else 
    {
        return {};
    }
}

function getJwtToken() {
    return localStorage.getItem(TOKEN_KEY + urlTenant.toLowerCase());
}

function setJwtToken(token) {
    localStorage.setItem(TOKEN_KEY + urlTenant.toLowerCase(), token);
}

function removeJwtToken() {
    localStorage.removeItem(TOKEN_KEY + urlTenant.toLowerCase());
}