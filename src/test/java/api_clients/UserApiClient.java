
package api_clients;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import models.LoginRequest;
import models.RegisterRequest;

public class UserApiClient {

    public static Response registerUser(RegisterRequest registerBody) {
        return RestAssured.given()
                .contentType("application/json")
                .body(registerBody)
                .when()
                .post("/user/register.php");
    }

    public static Response loginUser(LoginRequest loginBody) {
        return RestAssured.given()
                .contentType("application/json")
                .body(loginBody)
                .when()
                .post("/user/login.php");
    }

    public static Response deleteUser(String token) {
        return RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .when()
                .delete("/user/delete_profile.php");
    }
}