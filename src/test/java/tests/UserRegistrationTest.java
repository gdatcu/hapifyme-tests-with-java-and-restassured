package tests;

import io.restassured.RestAssured;
import models.RegisterRequest;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import utils.BaseTest;
import utils.ConfigManager;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class UserRegistrationTest extends BaseTest {
    @Test
    public void registerNewUserSucceeds() {
        RegisterRequest registerRequestBody = new RegisterRequest();


        registerRequestBody.setFirstName("Popescu");
        registerRequestBody.setLastName("Ion");
        registerRequestBody.setEmail(ConfigManager.getProperty("user.username"));
        registerRequestBody.setPassword(ConfigManager.getProperty("user.password"));


        String username = RestAssured.given()
                .spec(requestSpecification)
                .body(registerRequestBody)
                .when()
                .post("/user/register.php")
                .then()
                .assertThat()
                .statusCode(201)
                .body("status", equalTo("success"))
                .body("user_id", notNullValue())
                .extract().path("token"); // <-- Aici extragem valoarea
    }

    @AfterMethod
    public void cleanup() {
        // Curățăm utilizatorul creat în test
        if (ConfigManager.getProperty("user.username") != null) {
            deleteUser(ConfigManager.getProperty("user.username"), ConfigManager.getProperty("user.password"));
        }
    }
}
