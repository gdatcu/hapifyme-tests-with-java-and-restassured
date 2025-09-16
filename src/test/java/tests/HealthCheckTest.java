package tests;

import io.restassured.RestAssured;
import org.testng.annotations.Test;
import utils.BaseTest;

public class HealthCheckTest extends BaseTest {

    @Test
    public void systemIsHealthyTestApiKeyIsMissing() {
        // Given - (precondiții, setări inițiale)
//        RestAssured.baseURI = "https://test.hapifyme.com/api";

        // When - (acțiunea pe care o testăm)
        RestAssured.when()
                .get("/system/health_check.php")

                // Then - (validarea rezultatului)
                .then()
                .assertThat()
                .statusCode(401);
    }
}