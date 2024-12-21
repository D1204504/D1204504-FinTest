package bmi;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Chrome 瀏覽器將會被啟動，執行測試
 */

public class GymTestPage {
    private WebDriver driver;
    private final String testedURL = "https://nlhsueh.github.io/iecs-gym/";

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
//        options.addArguments("--headless");

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(4000));
        driver.manage().window().setSize(new Dimension(1200, 800));

    }

    @AfterEach
    void tearDown() throws InterruptedException {
        if (driver != null) {
            Thread.sleep(4000); // 延遲 4 秒
            driver.quit();
        }
    }

    @Test
    void testPageInteractions() {
        // 打开测试页面
        driver.get(testedURL);

        // 验证页面标题
        String expectedTitle = "逢大資工健身房";
        assertEquals(expectedTitle, driver.getTitle(), "页面标题不正确");

        // 选择星期和时间
        WebElement daySelect = driver.findElement(By.id("day"));
        daySelect.sendKeys("星期六");

        WebElement timeSelect = driver.findElement(By.id("time"));
        timeSelect.sendKeys("正常時段");

        // 输入年龄
        WebElement ageInput = driver.findElement(By.id("age"));
        ageInput.sendKeys("25");

        // 选择非会员
        WebElement memberNoRadio = driver.findElement(By.id("member-no"));
        memberNoRadio.click();

        // 点击计算按钮
        WebElement calculateButton = driver.findElement(By.id("calculate"));
        calculateButton.click();

        // 验证输出
        WebElement output = driver.findElement(By.id("output"));
        assertTrue(output.isDisplayed(), "输出结果未显示");
        assertTrue(output.getText().contains("250"), "票价计算不正确");
    }

    @Test
    void testMemberDiscount() {
        driver.get(testedURL);

        // 选择星期和时间
        WebElement daySelect = driver.findElement(By.id("day"));
        daySelect.sendKeys("星期三");

        WebElement timeSelect = driver.findElement(By.id("time"));
        timeSelect.sendKeys("早場：早上七點以前");

        // 输入年龄
        WebElement ageInput = driver.findElement(By.id("age"));
        ageInput.sendKeys("30");

        // 选择会员并输入会员编号
        WebElement memberYesRadio = driver.findElement(By.id("member-yes"));
        memberYesRadio.click();

        WebElement memberIdInput = driver.findElement(By.id("member-id"));
        memberIdInput.sendKeys("IECS-12345");

        // 点击计算按钮
        WebElement calculateButton = driver.findElement(By.id("calculate"));
        calculateButton.click();

        // 验证输出
        WebElement output = driver.findElement(By.id("output"));
        assertTrue(output.isDisplayed(), "输出结果未显示");
        assertTrue(output.getText().contains("100"), "会员票价计算不正确");
    }

    @Test
    void testAgeValidation() {
        driver.get(testedURL);

        // 输入无效年龄
        WebElement ageInput = driver.findElement(By.id("age"));
        ageInput.sendKeys("2");

        // 点击计算按钮
        WebElement calculateButton = driver.findElement(By.id("calculate"));
        calculateButton.click();

        // 验证错误信息
        WebElement ageError = driver.findElement(By.id("age-error"));
        assertTrue(ageError.isDisplayed(), "年龄错误信息未显示");
        assertEquals("年齡應介於 3 與 75 之間", ageError.getText(), "错误信息内容不正确");
    }
    @Test
    void testVisibleElements() {
        driver.get(testedURL);

        WebElement daySelect = driver.findElement(By.id("day"));
        assertTrue(daySelect.isDisplayed(), "星期选择框未显示");

        WebElement timeSelect = driver.findElement(By.id("time"));
        assertTrue(timeSelect.isDisplayed(), "时间选择框未显示");

        WebElement ageInput = driver.findElement(By.id("age"));
        assertTrue(ageInput.isDisplayed(), "年龄输入框未显示");

        WebElement memberYesRadio = driver.findElement(By.id("member-yes"));
        assertTrue(memberYesRadio.isDisplayed(), "会员单选按钮（是）未显示");

        WebElement memberNoRadio = driver.findElement(By.id("member-no"));
        assertTrue(memberNoRadio.isDisplayed(), "会员单选按钮（否）未显示");

        WebElement calculateButton = driver.findElement(By.id("calculate"));
        assertTrue(calculateButton.isDisplayed(), "计算按钮未显示");

        WebElement resetButton = driver.findElement(By.id("reset"));
        assertTrue(resetButton.isDisplayed(), "重置按钮未显示");

        WebElement output = driver.findElement(By.id("output"));
        assertFalse(output.isDisplayed(), "输出结果初始状态应隐藏");
    }

}
