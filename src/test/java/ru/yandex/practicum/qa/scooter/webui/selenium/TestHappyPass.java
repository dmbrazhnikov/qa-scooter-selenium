package ru.yandex.practicum.qa.scooter.webui.selenium;

import ru.yandex.practicum.qa.scooter.webui.model.ClientInfo;
import ru.yandex.practicum.qa.scooter.webui.model.ScooterColor;
import ru.yandex.practicum.qa.scooter.webui.selenium.pom.ClientInfoPage;
import ru.yandex.practicum.qa.scooter.webui.selenium.pom.ConfirmModal;
import ru.yandex.practicum.qa.scooter.webui.selenium.pom.HomePage;
import ru.yandex.practicum.qa.scooter.webui.selenium.pom.RentParamsPage;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebElement;

import static ru.yandex.practicum.qa.scooter.webui.selenium.pom.ClientInfoPage.NEXT_BTN;
import static org.junit.jupiter.api.Assertions.*;

/* Заказ самоката. Весь флоу позитивного сценария. */

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("E2E: happy pass")
public class TestHappyPass extends BaseUITest {

    @BeforeAll
    static void setUp() {
        configureDriver(false);
        driver.get(BASE_URL);
        HomePage homePage = new HomePage(driver);
        homePage.initiateOrderWithSmallBtn();
    }

    @Test
    @Order(1)
    @DisplayName("Correct client info is accepted")
    void shouldAcceptCorrectClientInfo() throws InterruptedException {
        // Arrange
        ClientInfoPage clientInfoPage = new ClientInfoPage(driver);
        ClientInfo clientInfo = new ClientInfo("Митхун", "Чакраборти",
                "Одесса, ул. Малая Арнаутская", "+79991112233");
        // Act
        clientInfoPage.fillInClientInfo(clientInfo);
        System.out.println("Selected subway station name is " + clientInfo.getSubwayStationName());
        clientInfoPage.doClick(NEXT_BTN);
        Thread.sleep(2000L);
        WebElement header = driver.findElement(RentParamsPage.HEADER);
        // Assert
        assertTrue(header.isDisplayed(), "Rent parameters page didn't appear after clicking Next button");
    }

    @Test
    @Order(2)
    @DisplayName("Rent parameters are accepted")
    void shouldAcceptRentParams() throws InterruptedException {
        // Arrange
        RentParamsPage rentParamsPage = new RentParamsPage(driver);
        // Act
        rentParamsPage.setCurrentDateAsDeliveryDate();
        rentParamsPage.setRentalPeriod("сутки");
        rentParamsPage.setColorCheckbox(ScooterColor.GREY_HOPELESSNESS);
        rentParamsPage.doSendKeys(RentParamsPage.COMMENTARY_INPUT, "Комментарий");
        rentParamsPage.doClick(RentParamsPage.PLACE_ORDER_BTN);
        Thread.sleep(2000L);
        assertTrue(
                driver.findElement(ConfirmModal.CONFIRM_HEADER).isDisplayed(),
                "Expected message didn't appear after clicking Place Order button"
        );
    }

    @Test
    @Order(3)
    @DisplayName("Order is confirmed")
    void shouldConfirmOrder() {
        // Arrange
        ConfirmModal confirmModal = new ConfirmModal(driver);
        // Act
        confirmModal.doClick(ConfirmModal.CONFIRM_BTN);
        // Assert
        assertTrue(
                driver.findElement(ConfirmModal.CONFIRMED_HEADER).isDisplayed(),
                "Order is not confirmed"
        );
    }
}
