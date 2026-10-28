package pageobjects;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage {
    private static final String URL = "https://qa-scooter.praktikum-services.ru/";
    // Верхняя кнопка «Заказать»
    private final By topOrderButton = By.xpath("//div[starts-with(@class,'Header_Nav')]//button[text()='Заказать']");
    // Нижняя кнопка «Заказать»
    private final By bottomOrderButton = By.xpath("//div[starts-with(@class,'Home_FinishButton')]//button[text()='Заказать']");
    // Кнопка принятия cookies
    private final By cookieButton = By.id("rcc-confirm-button");
    // Кнопки вопросов FAQ, индекс добавляется к идентификатору
    private final String faqQuestionId = "accordion__heading-";
    // Панели ответов FAQ, индекс добавляется к идентификатору
    private final String faqAnswerId = "accordion__panel-";

    private final WebDriver driver;
    private final WebDriverWait wait;

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void open() {
        driver.get(URL);
        wait.until(ExpectedConditions.visibilityOfElementLocated(topOrderButton));
        if (!driver.findElements(cookieButton).isEmpty()) {
            wait.until(ExpectedConditions.elementToBeClickable(cookieButton)).click();
        }
    }

    private void scrollTo(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
    }

    public void clickOrderButton(boolean top) {
        By button = top ? topOrderButton : bottomOrderButton;
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(button));
        scrollTo(element);
        wait.until(ExpectedConditions.elementToBeClickable(element)).click();
    }

    public void openQuestion(int index) {
        By question = By.id(faqQuestionId + index);
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(question));
        scrollTo(element);
        wait.until(ExpectedConditions.elementToBeClickable(element)).click();
    }

    public boolean isAnswerVisible(int index) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(faqAnswerId + index))).isDisplayed();
    }

    public String getAnswerText(int index) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(faqAnswerId + index))).getText();
    }
}
