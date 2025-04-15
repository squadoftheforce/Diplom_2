package api.model;

import java.util.List;

public class OrderResponse {
    private boolean success;
    private String name;
    private OrderDetails order;
    private List<OrderDetails> orders;
    private int total;
    private int totalToday;
    private String message;

    public OrderResponse() {
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OrderDetails getOrder() {
        return order;
    }

    public void setOrder(OrderDetails order) {
        this.order = order;
    }

    public List<OrderDetails> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderDetails> orders) {
        this.orders = orders;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotalToday() {
        return totalToday;
    }

    public void setTotalToday(int totalToday) {
        this.totalToday = totalToday;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class OrderDetails {
        private List<String> ingredients;
        private String _id;
        private String status;
        private int number;
        private String createdAt;
        private String updatedAt;

        public OrderDetails() {
        }

        public List<String> getIngredients() {
            return ingredients;
        }

        public void setIngredients(List<String> ingredients) {
            this.ingredients = ingredients;
        }

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }
    }
}