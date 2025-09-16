package utils;

import api_clients.UserApiClient;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import models.LoginRequest;
import models.RegisterRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;

import java.time.Instant;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class BaseTest {

    private static final Logger logger =
            LoggerFactory.getLogger(BaseTest.class);

    public static RequestSpecification requestSpecification;
    public static ResponseSpecification successResponseSpecification;
    public static ResponseSpecification unauthorizedResponseSpecification;

    @BeforeClass
    public void setup() {
        logger.info("Inițializare configurare globală pentru teste...");

        // Această metodă va rula o singură dată, înainte de orice test din clasa care o moștenește
        requestSpecification = new RequestSpecBuilder()
                .setBaseUri(ConfigManager.getProperty("base.url"))
                .setContentType("application/json")
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .build();

        successResponseSpecification = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .build();

        unauthorizedResponseSpecification = new ResponseSpecBuilder()
                .expectStatusCode(401)
                .build();



        // Nu mai setăm RestAssured.baseURI sau RestAssured.filters global
        // Totul este acum în specificație

    }

    protected String getAuthToken(String username, String userPassword) {

        LoginRequest loginRequestBody = new LoginRequest();
        loginRequestBody.setUsername(username);
        loginRequestBody.setPassword(userPassword);

       return RestAssured.given()
                .contentType("application/json")
                .body(loginRequestBody)
                .when()
                .post("/user/login.php")
                .then()
                .assertThat()
                .statusCode(200)
                .contentType("application/json")
                // Validăm conținutul body-ului
                .body("token", notNullValue()) // Verificăm că token-ul există
                .extract().path("token"); // <-- Aici extragem valoarea
    }

    protected void deleteUser(String username, String password) {
// Pas 1: Obține token-ul pentru utilizatorul pe care vrem să-l ștergem
        LoginRequest loginBody = new LoginRequest();
        loginBody.setUsername(username);
        loginBody.setPassword(password);
        String token;
        try {
            token = RestAssured.given()
                    .contentType("application/json")
                    .body(loginBody)
                    .when().post("/user/login.php")
                    .then().extract().path("token");
            System.out.println("Utilizatorul cu username-ul " + username + " si parola ... a fost sters cu succes!" );
        } catch (Exception e) {
            // Dacă login-ul eșuează (ex: userul nu a fost creat), nu facem nimic
            System.out.println("Nu am putut șterge utilizatorul, probabil nu a fost creat.");
            return;
        }

        // Pas 2: Trimite request-ul de ștergere autentificat
        RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .when()
                .delete("/user/delete_profile.php")
                .then()
                .assertThat()
                .statusCode(200)
                .body("message", equalTo("User profile deleted successfully."));
    }

    public static String registerNewUser() {
        RegisterRequest registerRequestBody = new RegisterRequest();

        String timestamp = String.valueOf(Instant.now());
        String first_name = "firstname" + timestamp;
        String last_name = "lastname" + timestamp;
        String email = "testuser_" + timestamp + "@yumi.com";
        String username = first_name + "_" + last_name;

        registerRequestBody.setFirstName(first_name);
        registerRequestBody.setLastName(last_name);
        registerRequestBody.setEmail(email);
        registerRequestBody.setPassword(ConfigManager.getProperty("user.password"));


        return  RestAssured.given()
                .contentType("application/json")
                .body(registerRequestBody)
                .when()
                .post("/user/register.php")
                .then()
                .assertThat()
                .statusCode(201)
                .body("status", equalTo("success"))
                .body("user_id", notNullValue())
//                .body("username", equalTo(username))
                .extract().path("username"); // <-- Aici extragem valoarea
    }

    public static void loginWithValidCredentials(String usernameRegistered) {
        // Presupunem că acest utilizator există în baza de date de test

        // Arrange: pregatim datele de test
        String username = usernameRegistered;
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
                .assertThat()
                .statusCode(statusCode)
                .contentType("application/json")
                .body("status", equalTo("success"))
                .body("message", equalTo("Login successful"))
                .body("user.username", equalTo(username)) // Verificăm că datele sunt corecte
//                .body("user.email", equalTo(ConfigManager.getProperty("user.email.valid"))) // Verificăm că email-ul este corect
                .body("token", notNullValue()); // Verificăm că token-ul există

        System.out.println("Daca a ajuns aici, testul merge.");
    }
}