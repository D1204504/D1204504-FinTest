package bmi.cucumber;

import io.cucumber.java.Before;
import io.cucumber.java.After;
import io.cucumber.java.en.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.*;
import java.time.Duration;

public class GymSteps {

    private WebDriver driver;
    private final String testedURL = "https://nlhsueh.github.io/iecs-gym/";

    @Before
    public void setUp() {
        // Use WebDriverManager to manage ChromeDriver automatically
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get(testedURL);
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Given("I have opened the {string} webpage")
    public void i_have_opened_the_webpage(String url) {
        driver.navigate().to(url);
    }

    @Given("I select the day {string}")
    public void i_select_the_day(String day) {
        Select daySelect = new Select(driver.findElement(By.id("day")));
        daySelect.selectByVisibleText(day);
    }

    @Given("I select the time {string}")
    public void i_select_the_time(String time) {
        Select timeSelect = new Select(driver.findElement(By.id("time")));
        timeSelect.selectByVisibleText(time);
    }

    @Given("I enter the age {string}")
    public void i_enter_the_age(String age) {
        WebElement ageInput = driver.findElement(By.id("age"));
        ageInput.clear();
        ageInput.sendKeys(age);
    }

    @Given("I select {string} for membership")
    public void i_select_for_membership(String isMember) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        if (isMember.equalsIgnoreCase("是")) {
            WebElement yesRadio = driver.findElement(By.id("member-yes"));
            js.executeScript("arguments[0].click();", yesRadio); // 點擊是會員
            js.executeScript("arguments[0].dispatchEvent(new Event('change'));", yesRadio); // 觸發 change 事件

            // 等待會員欄位顯示
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("member-id-section")));
            assertTrue(yesRadio.isSelected(), "Failed to select '是' for membership.");
        } else {
            WebElement noRadio = driver.findElement(By.id("member-no"));
            js.executeScript("arguments[0].click();", noRadio);
            assertTrue(noRadio.isSelected(), "Failed to select '否' for membership.");
        }
    }



    @Given("I enter the membership code {string}")
    public void i_enter_the_membership_code(String code) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement memberIdInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("member-id")));
        memberIdInput.clear();
        memberIdInput.sendKeys(code);
    }

    @Then("I should see the membership code as {string}")
    public void i_should_see_the_membership_code_as(String expectedCode) {
        WebElement memberIdInput = driver.findElement(By.id("member-id"));
        String actualCode = memberIdInput.getAttribute("value");
        assertEquals(expectedCode, actualCode, "Membership code does not match!");
    }



    @When("I click the \"Calculate Price\" button")
    public void i_click_the_calculate_price_button() {
        driver.findElement(By.id("calculate")).click();
    }

    @When("I click the \"Reset\" button")
    public void i_click_the_reset_button() {
        driver.findElement(By.id("reset")).click();
    }

    @Then("I should see the fee as {string} dollars")
    public void i_should_see_the_fee_as_dollars(String expectedPrice) {
        WebElement output = driver.findElement(By.id("output"));
        assertTrue(output.isDisplayed(), "The result field is not displayed.");
        String text = output.getText();
        assertTrue(text.contains("$" + expectedPrice), "The displayed fee does not match expected: " + text);
    }

    @Then("I should see an age error message {string}")
    public void i_should_see_an_age_error_message(String expectedMsg) {
        WebElement ageError = driver.findElement(By.id("age-error"));
        assertEquals(expectedMsg, ageError.getText(), "The age error message does not match the expected text.");
    }

    @Then("the age field should be cleared")
    public void the_age_field_should_be_cleared() {
        WebElement ageInput = driver.findElement(By.id("age"));
        assertEquals("", ageInput.getAttribute("value"), "The age field was not cleared.");
    }

    @Then("I should see a membership code error message {string}")
    public void i_should_see_a_membership_code_error_message(String expectedMsg) {
        WebElement memberIdError = driver.findElement(By.id("member-id-error"));
        assertEquals(expectedMsg, memberIdError.getText(), "The membership code error message does not match the expected text.");
    }

    @Then("the fee field should not be displayed")
    public void the_fee_field_should_not_be_displayed() {
        WebElement output = driver.findElement(By.id("output"));
        // Check if the fee field is hidden
        assertFalse(output.isDisplayed(), "The fee field is still displayed.");
    }

    @Then("the day field should be reset to {string}")
    public void the_day_field_should_be_reset_to(String expectedDay) {
        Select daySelect = new Select(driver.findElement(By.id("day")));
        String actualDay = daySelect.getFirstSelectedOption().getText();
        assertEquals(expectedDay, actualDay, "The day field was not reset to the expected value.");
    }


    @Then("the time field should be reset to {string}")
    public void the_time_field_should_be_reset_to(String expectedTime) {
        Select timeSelect = new Select(driver.findElement(By.id("time")));
        String actualTime = timeSelect.getFirstSelectedOption().getText();
        assertEquals(expectedTime, actualTime, "The time field was not reset to the expected value.");
    }


    @Then("the membership selection should be reset to {string}")
    public void the_membership_selection_should_be_reset_to(String expectedMembership) {
        WebElement yesRadio = driver.findElement(By.id("member-yes"));
        WebElement noRadio = driver.findElement(By.id("member-no"));

        if (expectedMembership.equalsIgnoreCase("是")) {
            assertTrue(yesRadio.isSelected(), "The membership selection was not reset to '是'.");
        } else if (expectedMembership.equalsIgnoreCase("不是")) {
            assertTrue(noRadio.isSelected(), "The membership selection was not reset to '不是'.");
        } else {
            fail("Unexpected membership value: " + expectedMembership);
        }
    }


    @Then("the result field should be hidden")
    public void the_result_field_should_be_hidden() {
        WebElement output = driver.findElement(By.id("output"));
        String displayStyle = output.getCssValue("display");
        assertEquals("none", displayStyle, "The result field was not hidden.");
    }

}
