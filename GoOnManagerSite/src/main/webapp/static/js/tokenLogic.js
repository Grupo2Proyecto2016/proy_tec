var TOKEN_KEY = "jwtToken"

// Acceso desde internet:
// var AppName = "http://proyecto.onmypc.org:8080/GoOnServices/"

// Acceso desde la red local:
// var AppName = "http://192.168.1.50:8080/GoOnServices/"

// Acceso local:
var AppName = "http://localhost:8080/GoOnServices/"

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

function setJwtToken(token) {
    localStorage.setItem(TOKEN_KEY, token);
}

function removeJwtToken() {
    localStorage.removeItem(TOKEN_KEY);
}