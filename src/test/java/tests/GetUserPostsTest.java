package tests;

import io.restassured.RestAssured;
import org.testng.annotations.Test;
import utils.BaseTest;

import static org.hamcrest.Matchers.*;

public class GetUserPostsTest extends BaseTest {

    @Test
    public void getUserPostsWithValidTokenReturnsUserData() {
        // 1. Obținem token-ul
        String token = getAuthToken("jane232_doe2123", "qazXSW@13");

        // 2. Facem request-ul către endpoint-ul securizat
        RestAssured.given()
                .header("Authorization", "Bearer " + token) // <-- Aici folosim token-ul!
                .when()
                .get("/user/get_user_posts.php")
                .then()
                .assertThat()
                .statusCode(200)
                .body("status", notNullValue())
                .body("status", equalTo("success"));// Validăm că primim datele profilului
//                .body("email", equalTo("test.user@example.com"));
    }

    @Test
    public void getUserPostsWithoutTokenFails() {
        // Test negativ: încercăm să accesăm resursa FĂRĂ token
        RestAssured.given()
                // Nu adăugăm header-ul de autorizare
                .when()
                .get("/user/get_user_posts.php")
                .then()
                .assertThat()
                .statusCode(401)
                .body("status", equalTo("error")); // Ne așteptăm la eroare de neautorizat
    }
}



