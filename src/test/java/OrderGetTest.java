import api.client.OrderClient;
import api.client.UserClient;
import api.model.Order;
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

public class OrderGetTest {

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
        
        // Создаем заказ для пользователя
        Order order = OrderGenerator.getRandomOrder();
        orderClient.createOrder(order, accessToken);
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.deleteUser(accessToken);
        }
    }

    @Test
    @DisplayName("Получение заказов авторизованного пользователя")
    @Description("Проверка возможности получения списка заказов авторизованным пользователем")
    public void getUserOrdersWithAuthTest() {
        Response response = orderClient.getUserOrders(accessToken);
        
        response.then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("orders", notNullValue())
                .body("total", notNullValue())
                .body("totalToday", notNullValue());
    }
    
    @Test
    @DisplayName("Получение заказов без авторизации")
    @Description("Проверка, что нельзя получить список заказов без авторизации")
    public void getUserOrdersWithoutAuthTest() {
        Response response = orderClient.getUserOrdersWithoutAuth();
        
        response.then()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }
    
    @Test
    @DisplayName("Получение всех заказов")
    @Description("Проверка возможности получения списка всех заказов")
    public void getAllOrdersTest() {
        Response response = orderClient.getAllOrders();
        
        response.then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("orders", notNullValue())
                .body("total", notNullValue())
                .body("totalToday", notNullValue());
    }
}