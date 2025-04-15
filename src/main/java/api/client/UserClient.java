package api.client;

import api.model.TokenRefresh;
import api.model.User;
import api.model.UserResponse;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class UserClient extends RestClient {

    private static final String REGISTER_PATH = "/auth/register";
    private static final String LOGIN_PATH = "/auth/login";
    private static final String USER_PATH = "/auth/user";
    private static final String LOGOUT_PATH = "/auth/logout";
    private static final String TOKEN_PATH = "/auth/token";

    @Step("Создание пользователя")
    public Response createUser(User user) {
        return given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post(REGISTER_PATH);
    }

    @Step("Логин пользователя")
    public Response loginUser(User user) {
        return given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post(LOGIN_PATH);
    }

    @Step("Получение данных пользователя")
    public Response getUserData(String token) {
        return given()
                .spec(getAuthSpec(token))
                .when()
                .get(USER_PATH);
    }

    @Step("Обновление данных пользователя")
    public Response updateUserData(User user, String token) {
        return given()
                .spec(getAuthSpec(token))
                .body(user)
                .when()
                .patch(USER_PATH);
    }

    @Step("Обновление данных пользователя без авторизации")
    public Response updateUserDataWithoutAuth(User user) {
        return given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .patch(USER_PATH);
    }

    @Step("Выход из системы")
    public Response logoutUser(String refreshToken) {
        return given()
                .spec(getBaseSpec())
                .body(new TokenRefresh(refreshToken))
                .when()
                .post(LOGOUT_PATH);
    }

    @Step("Удаление пользователя для очистки после теста")
    public void deleteUser(String token) {
        given()
                .spec(getAuthSpec(token))
                .when()
                .delete(USER_PATH)
                .then()
                .statusCode(202);
    }
}