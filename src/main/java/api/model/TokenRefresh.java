package api.model;

public class TokenRefresh {
    private String token;

    public TokenRefresh() {
    }

    public TokenRefresh(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}