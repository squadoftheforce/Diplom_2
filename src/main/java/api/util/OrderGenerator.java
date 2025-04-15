package api.util;

import api.client.OrderClient;
import api.model.Order;
import io.restassured.response.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderGenerator {
    
    private static OrderClient orderClient = new OrderClient();
    
    public static Order getRandomOrder() {
        Response response = orderClient.getIngredients();
        List<String> ingredients = response.then()
                .extract()
                .body()
                .jsonPath()
                .getList("data._id", String.class);
        
        List<String> orderIngredients = new ArrayList<>();
        orderIngredients.add(ingredients.get(0));
        orderIngredients.add(ingredients.get(1));
        
        return new Order(orderIngredients);
    }
    
    public static Order getOrderWithoutIngredients() {
        return new Order(new ArrayList<>());
    }
    
    public static Order getOrderWithInvalidIngredients() {
        List<String> invalidIngredients = new ArrayList<>();
        invalidIngredients.add("invalid_ingredient_id");
        
        return new Order(invalidIngredients);
    }
}