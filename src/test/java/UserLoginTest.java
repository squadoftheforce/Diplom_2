import api.client.UserClient;
import api.model.User;
import api.model.UserResponse;
import api.util.UserGenerator;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class UserLoginTest {

    private UserClient userClient;
    private User user;
    private String accessToken;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.getRandomUser();
        Response response = userClient.createUser(user);
        UserResponse userResponse = response.as(UserResponse.class);
        accessToken = userResponse.getAccessToken();
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.deleteUser(accessToken);
        }
    }

    @Test
    @DisplayName("Логин под существующим пользователем")
    @Description("Проверка возможности авторизации с корректными учетными данными")
    public void loginExistingUserTest() {
        Response response = userClient.loginUser(user);
        
        response.then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("user.email", equalTo(user.getEmail()))
                .body("user.name", equalTo(user.getName()))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue());
    }
    
    @Test
    @DisplayName("Логин с неверным логином")
    @Description("Проверка, что нельзя авторизоваться с неверным логином")
    public void loginWithInvalidEmailTest() {
        User invalidUser = new User("invalid" + user.getEmail(), user.getPassword());
        
        Response response = userClient.loginUser(invalidUser);
        
        response.then()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }
    
    @Test
    @DisplayName("Логин с неверным паролем")
    @Description("Проверка, что нельзя авторизоваться с неверным паролем")
    public void loginWithInvalidPasswordTest() {
        User invalidUser = new User(user.getEmail(), "invalid" + user.getPassword());
        
        Response response = userClient.loginUser(invalidUser);
        
        response.then()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }
}