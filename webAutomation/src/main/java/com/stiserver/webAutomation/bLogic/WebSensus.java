package com.stiserver.webAutomation.bLogic;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import java.io.File;
import java.util.concurrent.TimeUnit;

public class WebSensus {

    private String url;
    private String username;
    private String password;
    private String path;
    private File sensusReport;
    private WebDriver driver;

    /**
     * Default
     */
    public WebSensus() {}

    /**
     * Receive Username, Password, Path
     * @param path path
     * @param un username
     * @param pw password
     */
    public void sensus(String path, String un, String pw, String url) throws InterruptedException {
        this.path = path;
        this.username = un;
        this.password = pw;
        this.url = url;
     //  driver = new FirefoxDriver(getSettings());
      // login();
      // getAdditionalReport();
      // getSensusReport();
     // driver.close();

    }

    /**LOGIN IN
     */
    private void login() {
        //CLEAN UP
        driver.manage().deleteAllCookies();
        // driver.manage().timeouts().pageLoadTimeout(40, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

        //INITIAL COMMANDS FOR SENSUS.
        //LOGIN
        driver.get(url);
        driver.findElement(By.id("username")).sendKeys(username);
        driver.findElement(By.id("password")).sendKeys(password);

        driver.findElement(By.xpath("//input[@name='_eventId_proceed']")).click();
    }

    /**
     *  Get file name
     */
    private void getSensusReport() throws InterruptedException {

        //DOWNLOAD .CSV FILE
        WebElement element = driver.findElement(By.xpath("//a[@href='/reportgen']"));
        JavascriptExecutor executor = (JavascriptExecutor)driver;
        executor.executeScript("arguments[0].click();", element);
        Thread.sleep(40000);

        //CLICK MENU
        driver.findElement(By.xpath("//div[@ng-if='grid.options.enableGridMenu']")).click();
        WebElement link4 = driver.findElement(By.xpath("//button[@ng-click='itemAction($event, title)']"));
        link4.click();

        //WAIT FOR DOWNLOAD AND CLOSE
        Thread.sleep(30000);
        //  WebDriverWait wait2 = new WebDriverWait(driver, 100000);
        //   wait2.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='messageSpacing alert alert-info']")));

    }

    private void getAdditionalReport() throws InterruptedException {

    }

    /**
     * FireFox Settings
     * @return fire fox options
     */
    private FirefoxOptions getSettings() {
        //THIS IS THE WEB DRIVER OF FIREFOX. --will not work with out it.
        System.setProperty("webdriver.gecko.driver", "C:\\Program Files\\Java\\WEB TOOLS\\FIRE FOX WEB DRIVER\\geckodriver.exe");

        //SET FIREFOX PROFILE --used to save files to dir
        FirefoxProfile profile = new FirefoxProfile();

        //REMOVE DIALOGUE BOX
        profile.setPreference("browser.helperApps.alwaysAsk.force", false);
        profile.setPreference("browser.download.manager.focusWhenStarting", false);
        profile.setPreference("browser.download.manager.useWindow", false);
        profile.setPreference("browser.download.manager.showAlertOnComplete", false);

        //SET LOCATION TO DOWNLOAD FILES
        profile.setPreference("browser.download.dir", path); //Set the last directory used for saving a file from the "What should (browser) do with this file?" dialog.
        profile.setPreference("browser.download.folderList", 2); //Use for the default download directory the last folder specified for a download

        //SET PREFERENCE TO NOT SHOW FILE DOWNLOAD DIALOGUE USING MIME TYPES.
        profile.setPreference("browser.helperApps.neverAsk.openFile", "application/csv, text/csv, application/binary/octet-stream, application/octet-stream");
        profile.setPreference("browser.helperApps.neverAsk.saveToDisk", "application/csv, text/csv, application/binary/octet-stream, application/octet-stream");

        profile.setPreference("devtools.console.stdout.content", true);

        //ADD PROFILE TO OPTIONS AND CREATE WEB DRIVER
        FirefoxOptions options = new FirefoxOptions();
        //options.setLogLevel(FirefoxDriverLogLevel.DEBUG); //<--Allows you to see Status codes.

        options.setProfile(profile);

        return options;
    }

    /** Get file name
     *
     * @return sensus report
     */
    public File getSensusFile(){return sensusReport;}

    /**
     * Get path
     * @return get path
     */
    public String getPath(){return path;}
}
