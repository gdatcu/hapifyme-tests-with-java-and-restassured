package tests;

import io.restassured.RestAssured;
import org.testng.annotations.Test;
import utils.BaseTest;

import static io.restassured.RestAssured.given;

public class HealthCheckTest extends BaseTest {

    @Test
    public void systemIsHealthyTestApiKeyIsMissing() {
        // Given - (precondiții, setări inițiale)
//        RestAssured.baseURI = "https://test.hapifyme.com/api";

        // When - (acțiunea pe care o testăm)
        RestAssured.given()
                .spec(requestSpecification)
                .when()
                .get("/system/health_check.php")

                // Then - (validarea rezultatului)
                .then()
                .assertThat()
                .statusCode(401);
    }
}