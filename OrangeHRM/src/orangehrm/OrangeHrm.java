package orangehrm;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class OrangeHrm {

    public static void main(String[] args) {
        // Set the path to the ChromeDriver executable
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\VishalOshada\\Downloads\\chromedriver-win64 (1)\\chromedriver-win64\\chromedriver.exe");

        WebDriver driver = null;
        WebDriverWait wait = null;

        try {
            //WebDriver and WebDriverWait
            driver = new ChromeDriver();
            wait = new WebDriverWait(driver, Duration.ofSeconds(30)); // Increase wait time if needed

            // login page
            driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");

            // Step 1: Log in with valid credentials
            WebElement usernameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username")));
            WebElement passwordInput = driver.findElement(By.name("password"));
            WebElement loginButton = driver.findElement(By.cssSelector("button[type='submit']"));

            usernameInput.sendKeys("Admin");
            passwordInput.sendKeys("admin123");
            loginButton.click();

            // Verify login
            WebElement dashboardTextElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h6[text()='Dashboard']")));
            String dashboardText = dashboardTextElement.getText();

            if ("Dashboard".equals(dashboardText)) {
                System.out.println("Login successful. Dashboard is visible.");
            } else {
                System.out.println("Login failed or Dashboard not visible.");
            }

            // Log out
            WebElement profileMenu = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("li.oxd-userdropdown")));
            profileMenu.click();

            WebElement logoutButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href='/web/index.php/auth/logout']")));
            logoutButton.click();

            // Validate redirection to the login page
            wait.until(ExpectedConditions.urlToBe("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login"));

            // Validate the presence of the login button after logout
            WebElement loginButtonAfterLogout = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("button[type='submit']")));

            if (loginButtonAfterLogout.isDisplayed()) {
                System.out.println("User has been successfully logged out and redirected to the login page.");
            } else {
                System.out.println("Login button not found on the redirected login page.");
            }

            // Step 3: Attempt to log in with invalid credentials
            usernameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username")));
            passwordInput = driver.findElement(By.name("password"));
            loginButton = driver.findElement(By.cssSelector("button[type='submit']"));

            usernameInput.sendKeys("InvalidUser");
            passwordInput.sendKeys("InvalidPass");
            loginButton.click();

            // Verify login failure
            try {
                WebElement errorMessageElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.oxd-alert-content.oxd-alert-content--error p.oxd-text.oxd-text--p.oxd-alert-content-text")));
                String errorMessage = errorMessageElement.getText();
                if ("Invalid credentials".equals(errorMessage)) {
                    System.out.println("Error message displayed correctly for invalid credentials.");
                } else {
                    System.out.println("Error message not displayed or incorrect.");
                }
            } catch (Exception e) {
                System.out.println("Error message element not found.");
                System.out.println("Page source: " + driver.getPageSource());
            }

            // Step 4: Navigate to "Forgot your password?" screen
            try {
                WebElement forgotPasswordLink = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#app > div.orangehrm-login-layout > div > div.orangehrm-login-container > div > div.orangehrm-login-slot > div.orangehrm-login-form > form > div.orangehrm-login-forgot > p")));
                forgotPasswordLink.click();

                // Validate "Reset Password" screen
                WebElement resetPasswordTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h6.orangehrm-forgot-password-title")));
                WebElement resetPasswordButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("button.orangehrm-forgot-password-button.orangehrm-forgot-password-button--reset")));

                if (resetPasswordTitle.isDisplayed() && resetPasswordButton.isDisplayed()) {
                    System.out.println("Reset Password screen is visible with the title and button.");
                } else {
                    System.out.println("Reset Password screen validation failed.");
                }

                // Click on "Cancel" button
                WebElement cancelButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.orangehrm-forgot-password-button.orangehrm-forgot-password-button--cancel")));
                cancelButton.click();

                // Validate redirection to the login page
                wait.until(ExpectedConditions.urlToBe("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login"));

                // Validate the presence of the login button
                WebElement loginButtonAfterCancel = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("button[type='submit']")));

                if (loginButtonAfterCancel.isDisplayed()) {
                    System.out.println("User has been redirected back to the login page after clicking 'Cancel', and the 'Login' button is visible.");
                } else {
                    System.out.println("Login button not found on the redirected login page.");
                }

            } catch (Exception e) {
                System.out.println("Exception occurred while trying to navigate to 'Forgot your password?' or validate the screen.");
                System.out.println("Page source: " + driver.getPageSource());
                e.printStackTrace();
            }

        } catch (Exception e) {
            System.out.println("Exception during the test case: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Ensure the browser is closed after tests
            if (driver != null) {
                driver.quit();
            }
        }
    }
}
