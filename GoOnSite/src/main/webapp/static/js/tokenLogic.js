var TOKEN_KEY = "tenantJwtToken"

// Acceso desde internet:
// var AppName = "http://proyecto.onmypc.org:8080/GoOnServices/"

// Acceso desde la red local:
// var AppName = "http://192.168.1.50:8080/GoOnServices/"

// Acceso local:
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