package pageobjects;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class OrderPage {
    // Поле имени
    private final By firstName = By.cssSelector("input[placeholder='* Имя']");
    // Поле фамилии
    private final By lastName = By.cssSelector("input[placeholder='* Фамилия']");
    // Поле адреса
    private final By address = By.cssSelector("input[placeholder='* Адрес: куда привезти заказ']");
    // Поле станции метро
    private final By metro = By.cssSelector("input[placeholder='* Станция метро']");
    // Станция в выпадающем списке
    private final String metroOption = "//div[contains(@class,'select-search__select')]//*[text()='%s']";
    // Поле телефона
    private final By phone = By.cssSelector("input[placeholder='* Телефон: на него позвонит курьер']");
    // Кнопка перехода ко второму шагу
    private final By nextButton = By.xpath("//button[text()='Далее']");
    // Поле даты доставки
    private final By deliveryDate = By.cssSelector("input[placeholder='* Когда привезти самокат']");
    // Раскрывающийся список срока аренды
    private final By rentalPeriod = By.cssSelector(".Dropdown-control");
    // Вариант срока аренды
    private final String rentalOption = "//div[contains(@class,'Dropdown-option') and text()='%s']";
    // Поле комментария курьеру
    private final By comment = By.cssSelector("input[placeholder='Комментарий для курьера']");
    // Кнопка отправки формы заказа
    private final By submitButton = By.xpath("//div[starts-with(@class,'Order_Buttons')]//button[text()='Заказать']");
    // Кнопка подтверждения заказа
    private final By confirmButton = By.xpath("//div[starts-with(@class,'Order_Modal')]//button[text()='Да']");
    // Сообщение об успешно созданном заказе
    private final By successMessage = By.xpath("//div[starts-with(@class,'Order_ModalHeader') and contains(text(),'Заказ оформлен')]");

    private final WebDriverWait wait;

    public OrderPage(WebDriver driver) {
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    private WebElement visible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    private void click(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    public void fillCustomer(String name, String surname, String deliveryAddress, String station, String number) {
        visible(firstName).sendKeys(name);
        visible(lastName).sendKeys(surname);
        visible(address).sendKeys(deliveryAddress);
        visible(metro).sendKeys(station);
        click(By.xpath(String.format(metroOption, station)));
        visible(phone).sendKeys(number);
    }

    public void goToRental() {
        click(nextButton);
    }

    public void fillRental(LocalDate date, String period, String color, String courierComment) {
        WebElement dateInput = visible(deliveryDate);
        dateInput.sendKeys(date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        dateInput.sendKeys(Keys.ENTER);
        click(rentalPeriod);
        click(By.xpath(String.format(rentalOption, period)));
        // Флажок цвета самоката по идентификатору
        click(By.id(color));
        if (courierComment != null) {
            visible(comment).sendKeys(courierComment);
        }
    }

    public void submitAndConfirm() {
        click(submitButton);
        click(confirmButton);
    }

    public String getSuccessMessage() {
        return visible(successMessage).getText();
    }
}
