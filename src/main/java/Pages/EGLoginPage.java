package Pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utils.BaseUtils;

public class EGLoginPage extends BaseUtils {

    public EGLoginPage(WebDriver driver) {
        BaseUtils.driver = driver;
        PageFactory.initElements(driver, this);
    }


    @FindBy(xpath = "//*[@placeholder='Enter Username']")
    public WebElement usernameField;


    @FindBy(xpath = "//*[@placeholder='Enter Password']")
    public WebElement passwordField;

    @FindBy(xpath = "(//*[text()='Login'])[4]")
    public WebElement loginButton;

    @FindBy(xpath = "//*[text()='Academic Year']")
    public WebElement AcademicYeartext;


    public void enterUsername(String username) throws InterruptedException {
//        elementExistAndVisible(usernameField,10,10);
        sendTextOnUI(usernameField,username);
    }

    public void enterPassword(String password)
    {
        sendTextOnUI(passwordField,password);
    }

    public void clickonLoginButton()
    {
        click(loginButton);
    }


    public boolean isEnabled(String element)
    {
        switch (element)
        {
            case "usernameField" : return isElementEnabled(usernameField);
            case "passwordField" : return isElementEnabled(passwordField);
            case "loginButton" : return isElementEnabled(loginButton);
            default: logStep("Invalid Locator" + element);
        }
        return false;
    }

    public boolean isDisplayed(String element)
    {
        switch (element)
        {
            case "AcademicYeartext" : return isElementDisplayed(AcademicYeartext);
            default: logStep("Invalid Locator" + element);
        }
        return false;
    }


}
