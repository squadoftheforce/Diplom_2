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

import java.util.UUID;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class UserUpdateTest {

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
    @DisplayName("Изменение email с авторизацией")
    @Description("Проверка возможности изменения email авторизованным пользователем")
    public void updateEmailWithAuthTest() {
        String newEmail = UUID.randomUUID().toString() + "@test.com";
        User updatedUser = new User(newEmail, null, null);
        
        Response response = userClient.updateUserData(updatedUser, accessToken);
        
        response.then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("user.email", equalTo(newEmail));
    }
    
    @Test
    @DisplayName("Изменение пароля с авторизацией")
    @Description("Проверка возможности изменения пароля авторизованным пользователем")
    public void updatePasswordWithAuthTest() {
        // В API может быть ограничение на обновление только одного поля
        // Для обновления пароля может потребоваться текущий пароль
        // Поэтому создаём нового пользователя и пытаемся только обновить его данные
        User newUser = UserGenerator.getRandomUser();
        Response createResponse = userClient.createUser(newUser);
        String token = createResponse.as(UserResponse.class).getAccessToken();
        
        String newPassword = "newPassword" + UUID.randomUUID().toString().substring(0, 8);
        // Создаем полный объект пользователя с текущим email и name
        User updatedUser = new User(newUser.getEmail(), newPassword, newUser.getName());
        
        Response response = userClient.updateUserData(updatedUser, token);
        
        response.then()
                .log().ifValidationFails()
                .statusCode(200)
                .body("success", equalTo(true));
        
        // Проверяем, что с новым паролем можно авторизоваться
        User loginUser = new User(newUser.getEmail(), newPassword);
        Response loginResponse = userClient.loginUser(loginUser);
        
        loginResponse.then()
                .statusCode(200)
                .body("success", equalTo(true));
                
        // Удаляем созданного пользователя
        userClient.deleteUser(token);
    }
    
    @Test
    @DisplayName("Изменение имени с авторизацией")
    @Description("Проверка возможности изменения имени авторизованным пользователем")
    public void updateNameWithAuthTest() {
        String newName = "NewUser" + UUID.randomUUID().toString().substring(0, 5);
        // Создаем полный объект пользователя с текущим email
        User updatedUser = new User(user.getEmail(), null, newName);
        
        Response response = userClient.updateUserData(updatedUser, accessToken);
        
        response.then()
                .log().ifValidationFails()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("user.name", equalTo(newName));
    }
    
    @Test
    @DisplayName("Изменение email без авторизации")
    @Description("Проверка, что нельзя изменить email без авторизации")
    public void updateEmailWithoutAuthTest() {
        String newEmail = UUID.randomUUID().toString() + "@test.com";
        User updatedUser = new User(newEmail, null, null);
        
        Response response = userClient.updateUserDataWithoutAuth(updatedUser);
        
        response.then()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }
    
    @Test
    @DisplayName("Изменение пароля без авторизации")
    @Description("Проверка, что нельзя изменить пароль без авторизации")
    public void updatePasswordWithoutAuthTest() {
        String newPassword = "newPassword" + UUID.randomUUID().toString().substring(0, 8);
        User updatedUser = new User(null, newPassword, null);
        
        Response response = userClient.updateUserDataWithoutAuth(updatedUser);
        
        response.then()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }
    
    @Test
    @DisplayName("Изменение имени без авторизации")
    @Description("Проверка, что нельзя изменить имя без авторизации")
    public void updateNameWithoutAuthTest() {
        String newName = "NewUser" + UUID.randomUUID().toString().substring(0, 5);
        User updatedUser = new User(null, null, newName);
        
        Response response = userClient.updateUserDataWithoutAuth(updatedUser);
        
        response.then()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }
}