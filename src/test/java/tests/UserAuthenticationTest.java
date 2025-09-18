package tests;

import api_clients.UserApiClient;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import models.LoginRequest; // <-- Importăm clasa POJO
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import utils.BaseTest;
import utils.ConfigManager;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class UserAuthenticationTest extends BaseTest {

    @DataProvider(name = "invalidLoginCredentialsProvider")
    public Object[][] invalidLoginCredentialsProvider() {
        return new Object[][] {
                { "john1757442014378_doe1757442014378", "password123", "Invalid username or password" },
                { "test.username", "5Zm7%hECD{@[", "Invalid username or password" },
                { "", "", "Invalid username or password" },
                { "john1757442014378_doe1757442014378john1757442014378_doe1757442014378john1757442014378_doe1757442014378john1757442014378_doe1757442014378", "password123password123password123password123password123password123password123password123password123password123password123password123password123", "Invalid username or password" }
        };
    }

    @Test(groups = {"smoke", "authentication"}, dataProvider = "invalidLoginCredentialsProvider")
    public void loginWithInvalidCredentialsFails(String username, String password, String expectedErrorMessage) {
        // 1. Creăm obiectul Java (POJO) care modelează body-ul
        LoginRequest loginRequestBody = new LoginRequest();
        loginRequestBody.setUsername(username);
        loginRequestBody.setPassword(password);

        // Given
        RestAssured.given()
                .spec(requestSpecification) // Specificăm că trimitem JSON
                .body(loginRequestBody) // <-- Aici se întâmplă magia!
                // When
                .when()
                .post("/user/login.php")
                // Then
                .then()
                .assertThat()
                .statusCode(401)
                .body("message", equalTo(expectedErrorMessage)); // Ne așteptăm la 401 Unauthorized
        System.out.println("Intr-adevar, testul a trecut. Logarea cu username " + "<<" + username + ">>" + " si parola " + "<<" + password +">>" + " a esuat, asa cum ne asteptam, cu mesajul " + "<<" + expectedErrorMessage + ">>");
    }
//    {
//        "username": "jane232_doe2123",
//            "password": "qazXSW@13"
//    }
    @Test
    public void loginWithValidCredentialsSucceeds() {
        // Presupunem că acest utilizator există în baza de date de test

        // Arrange: pregatim datele de test
        String username = ConfigManager.getProperty("user.username.valid");
        String userPassword = ConfigManager.getProperty("user.password.valid");

        LoginRequest loginRequestBody = new LoginRequest();
        loginRequestBody.setUsername(username);
        loginRequestBody.setPassword(userPassword);

        // Act: Executam actiunea folosind clientul API
        Response response = UserApiClient.loginUser(loginRequestBody);
        int statusCode = response.statusCode();

        System.out.println("Raspunsul este: " + response.asPrettyString());
        System.out.println("Raspunsul contine headers: " + response.headers());
        System.out.println("Codul de raspuns este: " + response.statusLine());

        // Assert: Validam/Testam raspunsul in clasa de test!
        response.then()
                .spec(successResponseSpecification) // <-- Folosim șablonul de succes
                .contentType("application/json")
                .body("status", equalTo("success"))
                .body("message", equalTo("Login successful"))
                .body("user.username", equalTo(username)) // Verificăm că datele sunt corecte
                .body("user.email", equalTo(ConfigManager.getProperty("user.email.valid"))) // Verificăm că email-ul este corect
                .body("token", notNullValue()) // Verificăm că token-ul există
                .body(matchesJsonSchemaInClasspath("schemas/login_response_schema.json"));

        System.out.println("Daca a ajuns aici, testul merge.");




//        String authToken = RestAssured.given()
//                .contentType("application/json")
//                .body(loginRequestBody)
//                .when()
//                .post("/user/login.php")
//                .then()
//                .assertThat()
//                .statusCode(200)
//                .contentType("application/json")
//                // Validăm conținutul body-ului
//                .body("message", equalTo("Login successful"))
//                .body("user.username", equalTo(username)) // Verificăm că datele sunt corecte
//                .body("token", notNullValue()) // Verificăm că token-ul există
//                .extract().path("token"); // <-- Aici extragem valoarea
//
//        System.out.println("Token extras: " + authToken);
    }

}

