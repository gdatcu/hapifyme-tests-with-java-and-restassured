
package api_clients;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import models.LoginRequest;
import models.RegisterRequest;
import static utils.BaseTest.requestSpecification;

public class UserApiClient {

    public static Response registerUser(RegisterRequest registerBody) {
        return RestAssured.given()
                .spec(requestSpecification) // <-- Aici este magia!
                .body(registerBody)
                .when()
                .post("/user/register.php");
    }

    public static Response loginUser(LoginRequest loginBody) {
        return RestAssured.given()
                .spec(requestSpecification)
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