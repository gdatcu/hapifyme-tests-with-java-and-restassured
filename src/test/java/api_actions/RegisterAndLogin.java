package api_actions;

import org.testng.annotations.Test;
import utils.BaseTest;

public class RegisterAndLogin extends BaseTest {

    @Test
    public static void registerAndLoginUser() {
        String newUsername = registerNewUser();
        loginWithValidCredentials(newUsername);
    }
}
