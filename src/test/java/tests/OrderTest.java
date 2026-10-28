package tests;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import pageobjects.HomePage;
import pageobjects.OrderPage;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class OrderTest {
    private final boolean topButton;
    private final String name, surname, address, station, phone, period, color, comment;
    private WebDriver driver;
    private HomePage homePage;

    public OrderTest(boolean topButton, String name, String surname, String address, String station,
                     String phone, String period, String color, String comment) {
        this.topButton = topButton;
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.station = station;
        this.phone = phone;
        this.period = period;
        this.color = color;
        this.comment = comment;
    }

    @Parameterized.Parameters(name = "Верхняя кнопка: {0}, клиент: {1} {2}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
            {true, "Иван", "Иванов", "Москва, улица Льва Толстого, 16", "Парк культуры", "89991234567", "сутки", "black", "Позвоните заранее"},
            {true, "Анна", "Петрова", "Москва, улица Тверская, 7", "Маяковская", "89997654321", "двое суток", "grey", "Домофон работает"},
            {false, "Иван", "Иванов", "Москва, улица Льва Толстого, 16", "Парк культуры", "89991234567", "сутки", "black", "Позвоните заранее"},
            {false, "Анна", "Петрова", "Москва, улица Тверская, 7", "Маяковская", "89997654321", "двое суток", "grey", "Домофон работает"}
        });
    }

    @Before
    public void setUp() {
        driver = "firefox".equalsIgnoreCase(System.getProperty("browser", "chrome"))
                ? new FirefoxDriver() : new ChromeDriver();
        driver.manage().window().maximize();
        homePage = new HomePage(driver);
        homePage.open();
    }

    @After
    public void tearDown() {
        if (driver != null) driver.quit();
    }

    @Test
    public void orderIsSuccessfullyCreated() {
        homePage.clickOrderButton(topButton);
        OrderPage orderPage = new OrderPage(driver);
        orderPage.fillCustomer(name, surname, address, station, phone);
        orderPage.goToRental();
        orderPage.fillRental(LocalDate.now().plusDays(3), period, color, comment);
        orderPage.submitAndConfirm();
        assertTrue(orderPage.getSuccessMessage().contains("Заказ оформлен"));
    }
}
