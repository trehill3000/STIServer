package com.stiserver.webAutomation.bLogic;
import com.stiserver.webAutomation.model.SensusNetworkReport;
import com.stiserver.webAutomation.model.SiteInfoSensus;
import com.stiserver.webAutomation.service.DirPathFinder;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WebSensus {

    private String siteName;
    private String url;
    private String username;
    private String password;
    private String path;
    private File sensusReport;
    private WebDriver driver;

    /**
     * Default
     *
     */
    public WebSensus() {}

    /**
     * GET OBJ INFOR
     * @param path F
     * @param site F
     * @throws InterruptedException F
     * @throws IOException F
     * @throws ParseException F
     */
    public void sensus(String path, SiteInfoSensus site) throws InterruptedException, IOException, ParseException {
        this.siteName = site.getSite_name();
        this.path = path;
        this.username = site.getUsername();
        this.password = site.getPassword();
        this.url = site.getRni();
       driver = new FirefoxDriver(getSettings());
       login();
       //getAdditionalReport();
       getSensusReport();
        driver.close();
    }

    /**LOGIN IN
     */
    private void login() throws InterruptedException, IOException, ParseException {
        try {
            //CLEAN UP
            driver.manage().deleteAllCookies();
            // driver.manage().timeouts().pageLoadTimeout(40, TimeUnit.SECONDS);
            driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

            //LOGIN
            driver.get(url);
            driver.findElement(By.id("username")).sendKeys(username);
            driver.findElement(By.id("password")).sendKeys(password);
            Thread.sleep(3000);

            driver.findElement(By.xpath("//input[@name='_eventId_proceed']")).click();

            //DOWNLOAD .CSV FILE
            WebElement element = driver.findElement(By.xpath("//a[@href='/reportgen']"));
            JavascriptExecutor executor = (JavascriptExecutor) driver;
            executor.executeScript("arguments[0].click();", element);
            Thread.sleep(40000);

        }catch ( NoSuchElementException | WebDriverException | InterruptedException e){
            driver.close();
            sensus(this.path, new SiteInfoSensus(this.siteName,this.url, this.username, this.password));
        }
    }

    /** Get file name
     */
    private void getSensusReport() throws ParseException, IOException, InterruptedException {
        try {
            //CLICK MENU
            driver.findElement(By.xpath("//div[@ng-if='grid.options.enableGridMenu']")).click();
            WebElement link4 = driver.findElement(By.xpath("//button[@ng-click='itemAction($event, title)']"));
            link4.click();

            //WAIT FOR DOWNLOAD AND CLOSE
       //     Thread.sleep(30000);

            //WAIT FOR DOWNLOAD MENU TO COMPLETE
            WebDriverWait wait = new WebDriverWait(driver, 100000);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'messageSpacing  alert alert-info') and text()='Export request completed.']")));
            Thread.sleep(3000);

            //CHECK IF REPORT WAS DOWNLOADED________________________________________________EXCEPTION<<<<<< >>>>>>>>>>
            if(!DirPathFinder.checkIfDownloaded(DirPathFinder.networkDownloadPath(siteName))){
                driver.navigate().refresh();
                getSensusReport();
                System.out.println(">>>>>>>>DID NOT DOWNLOAD REPORT ----> TRYING AGAIN.............");
            }
        }catch (NoSuchElementException | WebDriverException | InterruptedException e){
            driver.close();
            sensus(this.path, new SiteInfoSensus(this.siteName,this.url, this.username, this.password));
        }
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
