import api.client.OrderClient;
import api.client.UserClient;
import api.model.Order;
import api.model.OrderResponse;
import api.model.User;
import api.model.UserResponse;
import api.util.OrderGenerator;
import api.util.UserGenerator;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class OrderCreationTest {

    private OrderClient orderClient;
    private UserClient userClient;
    private User user;
    private String accessToken;

    @Before
    public void setUp() {
        orderClient = new OrderClient();
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
    @DisplayName("Создание заказа с авторизацией")
    @Description("Проверка возможности создания заказа авторизованным пользователем")
    public void createOrderWithAuthTest() {
        Order order = OrderGenerator.getRandomOrder();
        
        Response response = orderClient.createOrder(order, accessToken);
        
        response.then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("name", notNullValue())
                .body("order.number", notNullValue());
    }
    
    @Test
    @DisplayName("Создание заказа без авторизации")
    @Description("Проверка возможности создания заказа без авторизации")
    public void createOrderWithoutAuthTest() {
        Order order = OrderGenerator.getRandomOrder();
        
        Response response = orderClient.createOrderWithoutAuth(order);
        
        response.then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("name", notNullValue())
                .body("order.number", notNullValue());
    }
    
    @Test
    @DisplayName("Создание заказа с ингредиентами")
    @Description("Проверка создания заказа с корректными ингредиентами")
    public void createOrderWithIngredientsTest() {
        Order order = OrderGenerator.getRandomOrder();
        
        Response response = orderClient.createOrder(order, accessToken);
        
        response.then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("name", notNullValue())
                .body("order.number", notNullValue());
    }
    
    @Test
    @DisplayName("Создание заказа без ингредиентов")
    @Description("Проверка, что нельзя создать заказ без ингредиентов")
    public void createOrderWithoutIngredientsTest() {
        Order order = OrderGenerator.getOrderWithoutIngredients();
        
        Response response = orderClient.createOrder(order, accessToken);
        
        response.then()
                .statusCode(400)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }
    
    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов")
    @Description("Проверка, что при передаче неверного хеша ингредиента возвращается ошибка")
    public void createOrderWithInvalidIngredientsTest() {
        Order order = OrderGenerator.getOrderWithInvalidIngredients();
        
        Response response = orderClient.createOrder(order, accessToken);
        
        response.then()
                .statusCode(500);
    }
}