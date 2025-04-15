package api.client;

import api.model.Order;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class OrderClient extends RestClient {

    private static final String ORDERS_PATH = "/orders";
    private static final String INGREDIENTS_PATH = "/ingredients";

    @Step("Создание заказа с авторизацией")
    public Response createOrder(Order order, String token) {
        return given()
                .spec(getAuthSpec(token))
                .body(order)
                .when()
                .post(ORDERS_PATH);
    }

    @Step("Создание заказа без авторизации")
    public Response createOrderWithoutAuth(Order order) {
        return given()
                .spec(getBaseSpec())
                .body(order)
                .when()
                .post(ORDERS_PATH);
    }

    @Step("Получение заказов пользователя с авторизацией")
    public Response getUserOrders(String token) {
        return given()
                .spec(getAuthSpec(token))
                .when()
                .get(ORDERS_PATH);
    }

    @Step("Получение заказов пользователя без авторизации")
    public Response getUserOrdersWithoutAuth() {
        return given()
                .spec(getBaseSpec())
                .when()
                .get(ORDERS_PATH);
    }

    @Step("Получение всех заказов")
    public Response getAllOrders() {
        return given()
                .spec(getBaseSpec())
                .when()
                .get(ORDERS_PATH + "/all");
    }

    @Step("Получение списка ингредиентов")
    public Response getIngredients() {
        return given()
                .spec(getBaseSpec())
                .when()
                .get(INGREDIENTS_PATH);
    }
}