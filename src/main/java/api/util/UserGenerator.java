package api.util;

import api.model.User;
import java.util.UUID;

public class UserGenerator {
    
    public static User getRandomUser() {
        String email = UUID.randomUUID().toString() + "@test.com";
        String password = "password" + UUID.randomUUID().toString().substring(0, 8);
        String name = "User" + UUID.randomUUID().toString().substring(0, 5);
        
        return new User(email, password, name);
    }
    
    public static User getUserWithoutEmail() {
        String password = "password" + UUID.randomUUID().toString().substring(0, 8);
        String name = "User" + UUID.randomUUID().toString().substring(0, 5);
        
        return new User(null, password, name);
    }
    
    public static User getUserWithoutPassword() {
        String email = UUID.randomUUID().toString() + "@test.com";
        String name = "User" + UUID.randomUUID().toString().substring(0, 5);
        
        return new User(email, null, name);
    }
    
    public static User getUserWithoutName() {
        String email = UUID.randomUUID().toString() + "@test.com";
        String password = "password" + UUID.randomUUID().toString().substring(0, 8);
        
        return new User(email, password, null);
    }
}