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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UserCreationTest {

    private UserClient userClient;
    private User user;
    private String accessToken;

    @Before
    public void setUp() {
        userClient = new UserClient();
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.deleteUser(accessToken);
        }
    }

    @Test
    @DisplayName("Создание уникального пользователя")
    @Description("Проверка, что можно создать нового уникального пользователя и получить успешный ответ")
    public void createUniqueUserTest() {
        user = UserGenerator.getRandomUser();
        
        Response response = userClient.createUser(user);
        
        response.then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("user.email", equalTo(user.getEmail()))
                .body("user.name", equalTo(user.getName()))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue());
        
        UserResponse userResponse = response.as(UserResponse.class);
        accessToken = userResponse.getAccessToken();
    }
    
    @Test
    @DisplayName("Создание пользователя, который уже зарегистрирован")
    @Description("Проверка, что нельзя создать пользователя с существующим email")
    public void createExistingUserTest() {
        user = UserGenerator.getRandomUser();
        
        Response successResponse = userClient.createUser(user);
        UserResponse userResponse = successResponse.as(UserResponse.class);
        accessToken = userResponse.getAccessToken();
        
        Response duplicateResponse = userClient.createUser(user);
        
        duplicateResponse.then()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"));
    }
    
    @Test
    @DisplayName("Создание пользователя без email")
    @Description("Проверка, что нельзя создать пользователя без обязательного поля email")
    public void createUserWithoutEmailTest() {
        user = UserGenerator.getUserWithoutEmail();
        
        Response response = userClient.createUser(user);
        
        response.then()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }
    
    @Test
    @DisplayName("Создание пользователя без пароля")
    @Description("Проверка, что нельзя создать пользователя без обязательного поля password")
    public void createUserWithoutPasswordTest() {
        user = UserGenerator.getUserWithoutPassword();
        
        Response response = userClient.createUser(user);
        
        response.then()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }
    
    @Test
    @DisplayName("Создание пользователя без имени")
    @Description("Проверка, что нельзя создать пользователя без обязательного поля name")
    public void createUserWithoutNameTest() {
        user = UserGenerator.getUserWithoutName();
        
        Response response = userClient.createUser(user);
        
        response.then()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }
}