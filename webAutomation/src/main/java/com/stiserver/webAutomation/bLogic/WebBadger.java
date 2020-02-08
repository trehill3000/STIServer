package com.stiserver.webAutomation.bLogic;
import com.stiserver.webAutomation.model.SiteInfoBadger;
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
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.concurrent.TimeUnit.SECONDS;

public class WebBadger {

    private String siteName;
    private String username;
    private String password;
    private String path;
    private String avail_report;
    private String prepro_report;
    private String pro_report;
    private String leak_report;
    private String tamper_report;
    private String encoder_report;
    private String additionalLogin;
    private WebDriver driver;

    /**
     * Default
     */
    public WebBadger(){
    }

    /**
     * CONSTRUCTOR
     * @param p F
     * @param site F
     * @throws ParseException F
     */
    public void Badger(String p, SiteInfoBadger site) throws ParseException {
        this.path = p;
        this.username = site.getUsername();
        this.password = site.getPassword();
        this.siteName = site.getSite_name();
        this.avail_report = site.getAvail_api();
        this.prepro_report = site.getPrepro_api();
        this.pro_report = site.getPro_api();
        this.leak_report = site.getLeak_api();
        this.tamper_report = site.getTamper_api();
        this.encoder_report = site.getEncoder_api();
        this.additionalLogin = site.getAdditional_login();
        driver = new FirefoxDriver(getSettings());

        login();
        getAdditionalReports();
        getPreProReport();
        getProReport();
        driver.close();
    }

    /**
     * GET ADDITIONAL REPORT
     */
    public void getAdditionalReports() throws ParseException {
        getTamperReport();
        getEncoderReport();
        getLeakReport();
        getBackflowReport();
    }

    /**LOGIN IN
     */
    public void login(){
        try{
        if(additionalLogin ==  null || additionalLogin.equals("")) {
            //CLEAN UP
            driver.manage().deleteAllCookies();
            driver.manage().timeouts().pageLoadTimeout(25, SECONDS);
            driver.manage().timeouts().implicitlyWait(6, SECONDS);

            //LOGIN
            driver.get("https://beaconama.net/signin");
            driver.findElement(By.id("username")).sendKeys(username);
            driver.findElement(By.id("password")).sendKeys(password);
            driver.findElement(By.id("ba-submit")).click();
            Thread.sleep(3000);
        }
        //EXTRA LOGIN STEP
        else {loginAdditional();}
        }
        catch (InterruptedException | NoSuchElementException | WebDriverException e){login();
        }
    }

    /**
     *ADDITIONAL LOGIN
\     */
    public void loginAdditional() {
        try {
            //CLEAN UP
            driver.manage().deleteAllCookies();
            driver.manage().timeouts().pageLoadTimeout(25, SECONDS);
            driver.manage().timeouts().implicitlyWait(6, SECONDS);

            //LOGIN
            driver.get("https://beaconama.net/signin");
            driver.findElement(By.id("username")).sendKeys(username);
            driver.findElement(By.id("password")).sendKeys(password);
            driver.findElement(By.id("ba-submit")).click();
            driver.findElement(By.xpath("//button[@value='" + additionalLogin + "']")).click();
            System.out.println(siteName +   " LOGGED IN...");

            Thread.sleep(3000);
        }catch (InterruptedException | NoSuchElementException | WebDriverException e){
            System.out.println(">>>>>>>>ERROR - LOGIN IN ----> TRYING AGAIN............");
            loginAdditional();}
    }

    /**
     * DOWNLOAD PRE-PROVISIONED REPORT
      * @throws ParseException D
     */
    public void getPreProReport() throws ParseException {
        try {
            driver.findElement(By.id("snassets")).click();
            Thread.sleep(2000);
            driver.findElement(By.xpath("//a[@href='#admin-tab-endpoints']")).click();

            //DOWNLOAD PRE-PROVISIONED
            Thread.sleep(2000);
            driver.findElement(By.id("batch-actions-pre")).click();
            driver.findElement(By.id("batch-actions-pre")).click();
            driver.findElement(By.id("action-export-pre-extended")).click();
            driver.findElement(By.id("asset-export-button")).click();

            //WAIT FOR DOWNLOAD MENU TO COMPLETE
            WebDriverWait wait = new WebDriverWait(driver, 100000);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@id='export_result_url']")));
            Thread.sleep(3000);
        }catch (InterruptedException | NoSuchElementException | WebDriverException e){ //NETWORK ERROR?
            System.out.println(">>>>>>>>ERROR {1} ----> TRYING AGAIN............." + e.getMessage());
            driver.navigate().refresh();
            getPreProReport();}

        //ACTUALLY CLICK DOWNLOAD BUTTON
        try { driver.findElement(By.id("export_result_url")).click();}
        catch (NoSuchElementException | WebDriverException e){
            System.out.println(">>>>>>>>ERROR {2} ----> TRYING AGAIN............");
            driver.navigate().refresh();
            getPreProReport();
        }
        if (!DirPathFinder.checkIfDownloaded(DirPathFinder.networkDownloadPath(siteName))){ //CHECK IF REPORT WAS DOWNLOADED_______________________________________________________________EXCEPTION<<<<<< >>>>>>>>>>
            System.out.println(">>>>>>>>DID NOT DOWNLOAD PRE PROVISIONED REPORT{1} ----> REFRESHING AND TRYING AGAIN.............");
            driver.navigate().refresh();
            getPreProReport();
        }
    }

    /**
     * DOWNLOAD PROVISIONED REPORT
     * @throws ParseException D
     */
    public void getProReport() throws ParseException {
        //GET NEXT REPORT
        try {
            driver.navigate().refresh();
            driver.findElement(By.id("snassets")).click();
            Thread.sleep(2000);
            driver.findElement(By.xpath("//a[@href='#admin-tab-endpoints']")).click();
            Thread.sleep(2000);
            driver.findElement(By.id("batch-actions")).click();
            driver.findElement(By.id("action-export-extended")).click();
            Thread.sleep(2000);
            driver.findElement(By.id("asset-export-button")).click();

            //WAIT FOR DOWNLOAD MENU TO COMPLETE
            WebDriverWait wait2 = new WebDriverWait(driver, 100000);
            wait2.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@id='export_result_url']")));
            Thread.sleep(2000);
        } catch (InterruptedException | NoSuchElementException | WebDriverException e) {
            System.out.println(">>>>>>>>ERROR {1} ----> TRYING AGAIN............." + e.getMessage());
            driver.navigate().refresh();
            getProReport();
        }

        //ACTUALLY CLICK DOWNLOAD BUTTON
        try {
            driver.findElement(By.id("export_result_url")).click();
        } catch (NoSuchElementException | WebDriverException e) { //CHECK IF REPORT WAS DOWNLOADED_______________________________________________________________EXCEPTION<<<<<< >>>>>>>>>>
            System.out.println(">>>>>>>>ERROR {2} ----> TRYING AGAIN.............");
            driver.navigate().refresh();
            getProReport();
        }

        //CHECK IF THE FILE WAS DOWNLOADED
        if (!DirPathFinder.checkIfDownloaded(DirPathFinder.networkDownloadPath(siteName))) {
            System.out.println(">>>>>>>>DID NOT DOWNLOAD PROVISIONED REPORT{2} ----> REFRESHING AND TRYING AGAIN.............");
            driver.navigate().refresh();
            getProReport();
        }
    }

    /**
     * GET TAMPER REPORT
     */
    public void getTamperReport() throws ParseException {

        //DOWNLOAD ENDPOINT TAMPER REPORT
        driver.navigate().to("https://beaconama.net/admin/portfolio");
        try {
            //BADGER API
            driver.navigate().to("https://beaconama.net/admin/portfolio/monitor?i=aag%3Aexceptions%3ATamper%2CMagneticTamper%3Aall&view=map");

            //MENU
            Thread.sleep(2000);
            driver.findElement(By.id("batch-actions")).click();
            Thread.sleep(2000);
            driver.findElement(By.id("action-export-data")).click();
            Thread.sleep(2000);
            driver.findElement(By.id("btn-export-new")).click();

            //WAIT FOR DOWNLOAD MENU TO COMPLETE
            WebDriverWait wait2 = new WebDriverWait(driver, 100000);
            wait2.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@id='export_result_url']")));
            Thread.sleep(2000);
        }catch (InterruptedException | NoSuchElementException | WebDriverException e){
            System.out.println(">>>>>>>>ERROR {1} ----> TRYING AGAIN............." + e.getMessage());
            driver.navigate().refresh();
            getTamperReport();
        }

        //ACTUALLY CLICK DOWNLOAD BUTTON
        try { driver.findElement(By.id("export_result_url")).click();}
        catch (NoSuchElementException | WebDriverException e) { //CHECK IF REPORT WAS DOWNLOADED_______________________________________________________________EXCEPTION<<<<<< >>>>>>>>>>
            System.out.println(">>>>>>>>ERROR {2} ----> TRYING AGAIN.............");
            driver.navigate().refresh();
            getTamperReport();
        }

        //CHECK IF THE FILE WAS DOWNLOADED
        if (!DirPathFinder.checkIfDownloaded(path, 1)) {
            System.out.println(">>>>>>>>DID NOT DOWNLOAD TAMPER REPORT{2} ----> REFRESHING AND TRYING AGAIN.............");
            driver.navigate().refresh();
            getTamperReport();}
        else {
            System.out.println("TAMPER REPORT DOWNLOADED");
            //FIND MOST RECENT MOD FILE AND MOVE TO ANOTHER DIR
            CompletableFuture.runAsync(() -> {

                //GET MOST RECENT DOWNLOAD FILE FROM DOWNLOAD DIR
                File lastModFile = null;
                try {
                    lastModFile = DirPathFinder.getLastModFile(path);
                } catch (ParseException | IOException e) {
                    e.printStackTrace();
                }
                assert lastModFile != null;//System.out.println(lastModFile);

                //ADD FILE TO NEW PATH DESTINATION
                Path destination = Paths.get(DirPathFinder.tamperSendPath(siteName) + siteName + "_Tamper_Report_" + new SimpleDateFormat("MM-dd-yyyy").format(new Date()) + ".csv");
                try {
                    Files.move(lastModFile.toPath(), destination);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    /**
     * GET ENCODER REPORT
     */
    public void getEncoderReport() throws ParseException {

        //DOWNLOAD METER/ENCODER ALARMS REPORT
        driver.navigate().to("https://beaconama.net/admin/portfolio");
        //DOWNLOAD REPORT
        try {
            driver.navigate().to("https://beaconama.net/admin/portfolio/monitor?i=aag%3Aexceptions%3AEmptyPipe%2CWaterPressureSensorError%2CEncoderExceedingMaxFlow%2CWaterTemperatureSensorError%2CEncoderSensorError%2CEncoderTemperature%2CEndOfLife%2CEncoderDialChange%2CEncoderRemoval%2CEncoderProgrammed%2CEncoderMagneticTamper%2CMeterTemperatureSensorError%3Aall&view=map");

            //MENU
            Thread.sleep(2000);
            driver.findElement(By.id("batch-actions")).click();
            Thread.sleep(2000);
            driver.findElement(By.id("action-export-data")).click();
            Thread.sleep(2000);
            driver.findElement(By.id("btn-export-new")).click();

            //WAIT FOR DOWNLOAD MENU TO COMPLETE
            WebDriverWait wait2 = new WebDriverWait(driver, 100000);
            wait2.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@id='export_result_url']")));
            Thread.sleep(2000);
        } catch (InterruptedException | NoSuchElementException | WebDriverException e) {
            System.out.println(">>>>>>>>ERROR {1} ----> TRYING AGAIN............." + e.getMessage());
            driver.navigate().refresh();
            getEncoderReport();
        }

        //ACTUALLY CLICK DOWNLOAD BUTTON
        try {
            driver.findElement(By.id("export_result_url")).click();
        } catch (NoSuchElementException | WebDriverException e) { //CHECK IF REPORT WAS DOWNLOADED_______________________________________________________________EXCEPTION<<<<<< >>>>>>>>>>
            System.out.println(">>>>>>>>ERROR {2} ----> TRYING AGAIN.............");
            driver.navigate().refresh();
            getTamperReport();
        }

        //CHECK IF THE FILE WAS DOWNLOADED
        if (!DirPathFinder.checkIfDownloaded(path, 1)) {
            System.out.println(">>>>>>>>DID NOT DOWNLOAD ENCODER REPORT{2} ----> REFRESHING AND TRYING AGAIN.............");
            driver.navigate().refresh();
            getTamperReport();
        } else {
            System.out.println("ENCODER REPORT DOWNLOADED");

            //Anonymous Async Function
            CompletableFuture.runAsync(() -> {
                //GET MOST RECENT DOWNLOAD FILE FROM DOWNLOAD DIR
                File lastModFile = null;
                try {
                    lastModFile = DirPathFinder.getLastModFile(path);
                } catch (ParseException | IOException e) {
                    e.printStackTrace();
                }
                assert lastModFile != null;//System.out.println(lastModFile);

                //ADD FILE TO NEW PATH DESTINATION
                Path destination = Paths.get(DirPathFinder.encoderSendPath(siteName) + siteName + "_Encoder_Report_" + new SimpleDateFormat("MM-dd-yyyy").format(new Date()) + ".csv");
                try {
                    Files.move(lastModFile.toPath(), destination);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    /**
     * GET LEAKS REPORT
     */
    public void getLeakReport() throws ParseException {

        //DOWNLOAD LEAK ALARMS  REPORT
        driver.navigate().to("https://beaconama.net/admin/portfolio");
        try {
            driver.navigate().to("https://beaconama.net/admin/portfolio/monitor?i=aag%3Aflow%3Aleak%3Aall&view=map");

            //MENU
            Thread.sleep(2000);
            driver.findElement(By.id("batch-actions")).click();
            Thread.sleep(2000);
            driver.findElement(By.id("action-export-data")).click();
            Thread.sleep(2000);
            driver.findElement(By.id("btn-export-new")).click();

            //WAIT FOR DOWNLOAD MENU TO COMPLETE
            WebDriverWait wait2 = new WebDriverWait(driver, 100000);
            wait2.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@id='export_result_url']")));
            Thread.sleep(2000);
        } catch (InterruptedException | NoSuchElementException | WebDriverException e) {
            System.out.println(">>>>>>>>ERROR {1} ----> TRYING AGAIN............." + e.getMessage());
            driver.navigate().refresh();
            getEncoderReport();
        }

        //ACTUALLY CLICK DOWNLOAD BUTTON
        try {
            driver.findElement(By.id("export_result_url")).click();
        } catch (NoSuchElementException | WebDriverException e) { //CHECK IF REPORT WAS DOWNLOADED_______________________________________________________________EXCEPTION<<<<<< >>>>>>>>>>
            System.out.println(">>>>>>>>ERROR {2} ----> TRYING AGAIN.............");
            driver.navigate().refresh();
            getTamperReport();
        }

        //CHECK IF THE FILE WAS DOWNLOADED
        if (!DirPathFinder.checkIfDownloaded(path, 1)) {
            System.out.println(">>>>>>>>DID NOT DOWNLOAD BACKFLOW REPORT{2} ----> REFRESHING AND TRYING AGAIN.............");
            driver.navigate().refresh();
            getLeakReport();
        } else {
            System.out.println("LEAKS REPORT DOWNLOADED");

            //Anonymous Async Function
            CompletableFuture.runAsync(() -> {
                //GET MOST RECENT DOWNLOAD FILE FROM DOWNLOAD DIR
                File lastModFile = null;
                try {
                    lastModFile = DirPathFinder.getLastModFile(path);
                } catch (ParseException | IOException e) {
                    e.printStackTrace();
                }
                assert lastModFile != null;//System.out.println(lastModFile);

                //ADD FILE TO NEW PATH DESTINATION
                Path destination = Paths.get(DirPathFinder.leakSendPath(siteName) + siteName + "_Leak_Report_" + new SimpleDateFormat("MM-dd-yyyy").format(new Date()) + ".csv");

                try {
                    Files.move(lastModFile.toPath(), destination);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    /**
     * GET BACKFLOW REPORT
     */
    public void getBackflowReport() throws ParseException {
        //DOWNLOAD BACK FLOW REPORT
        driver.navigate().to("https://beaconama.net/admin/portfolio");
        try {
            driver.navigate().to("https://beaconama.net/admin/portfolio/monitor?i=aag%3Aflow%3Aback_flow%3Aall&view=map");

            //MENU
            Thread.sleep(2000);
            driver.findElement(By.id("batch-actions")).click();
            Thread.sleep(2000);
            driver.findElement(By.id("action-export-data")).click();
            Thread.sleep(2000);
            driver.findElement(By.id("btn-export-new")).click();

            //WAIT FOR DOWNLOAD MENU TO COMPLETE
            WebDriverWait wait2 = new WebDriverWait(driver, 100000);
            wait2.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@id='export_result_url']")));
            Thread.sleep(2000);
        } catch (InterruptedException | NoSuchElementException | WebDriverException e) {
            System.out.println(">>>>>>>>ERROR {1} ----> TRYING AGAIN............." + e.getMessage());
            driver.navigate().refresh();
            getEncoderReport();
        }

        //ACTUALLY CLICK DOWNLOAD BUTTON
        try {
            driver.findElement(By.id("export_result_url")).click();
        } catch (NoSuchElementException | WebDriverException e) { //CHECK IF REPORT WAS DOWNLOADED_______________________________________________________________EXCEPTION<<<<<< >>>>>>>>>>
            System.out.println(">>>>>>>>ERROR {2} ----> TRYING AGAIN.............");
            driver.navigate().refresh();
            getBackflowReport();
        }

        //CHECK IF THE FILE WAS DOWNLOADED
        if (!DirPathFinder.checkIfDownloaded(path, 1)) {
            System.out.println(">>>>>>>>DID NOT DOWNLOAD LEAKS REPORT{2} ----> REFRESHING AND TRYING AGAIN.............");
            driver.navigate().refresh();
            getLeakReport();
        } else {
            System.out.println("BACK FLOW REPORT DOWNLOADED");

            CompletableFuture.runAsync(() -> {
                //GET MOST RECENT DOWNLOAD FILE FROM DOWNLOAD DIR
                File lastModFile = null;
                try {
                    lastModFile = DirPathFinder.getLastModFile(path);
                } catch (ParseException | IOException e) {
                    e.printStackTrace();
                }
                assert lastModFile != null;//System.out.println(lastModFile);

                //ADD FILE TO NEW PATH DESTINATION
                Path destination = Paths.get(DirPathFinder.backFlowSendPath(siteName) +siteName +"_Back_Flow_Report_" + new SimpleDateFormat("MM-dd-yyyy").format(new Date()) + ".csv");

                try {
                    Files.move(lastModFile.toPath(), destination);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

        //REFRESH
        driver.navigate().refresh();
    }

    /**
     *GET REPORTS USING API
     */
    private void getBadgerReports() {

        //  driver.manage().timeouts().pageLoadTimeout(5000, SECONDS);
        try{driver.get("https://beaconama.net/" + avail_report);}catch (Exception ignored){
            driver.get("https://beaconama.net/" + avail_report);
        };
        try{driver.get("https://beaconama.net/" + prepro_report);}catch (Exception ignored){
            driver.get("https://beaconama.net/" + prepro_report);
        };
        try{driver.get("https://beaconama.net/" + pro_report);}catch (Exception ignored){
            driver.get("https://beaconama.net/" + pro_report);
        };

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

    /**
     * Get path
     * @return get path
     */
    public String getPath(){return path;}

    //test
   /*   public void clickyyy() throws InterruptedException {

        Thread.sleep(3000);


        //DOWNLOAD PRE-PROVISIONED AND PROVISIONED REPORTS_______________________________________________________________________________________
        driver.navigate().refresh();
        driver.findElement(By.id("snassets")).click();
        Thread.sleep(2000);
        driver.findElement(By.xpath("//a[@href='#admin-tab-endpoints']")).click();
        Thread.sleep(2000);
        driver.findElement(By.id("batch-actions-pre")).click();
        driver.findElement(By.id("action-export-pre-extended")).click();
        Thread.sleep(2000);
        driver.findElement(By.id("asset-export-button")).click();

        //WAIT FOR ELEMENT AND CLICK
        Wait<WebDriver> wait12 = new FluentWait<>(driver)
                .withTimeout(500000, SECONDS)
                .pollingEvery(30000, SECONDS)
                .ignoring(Exception.class);

        WebElement clicked1 = wait12.until(new Function<WebDriver, WebElement>() {
            @Override
            public WebElement apply(WebDriver webDriver) {
                return wait12.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@id='export_result_url']")));

            }
        });
        clicked1.click();


        //GET NEXT REPORT_____________________________________________________________________________________________________________________
        driver.navigate().refresh();
        Thread.sleep(2000);
        driver.findElement(By.xpath("//a[@href='#admin-tab-endpoints']")).click();
        Thread.sleep(2000);
        driver.findElement(By.id("batch-actions")).click();
        driver.findElement(By.id("action-export-extended")).click();
        Thread.sleep(2000);
        driver.findElement(By.id("asset-export-button")).click();

        //WAIT FOR ELEMENT AND CLICK
        Wait<WebDriver> wait113 = new FluentWait<>(driver)
                .withTimeout(500000, SECONDS)
                .pollingEvery(30000, SECONDS)
                .ignoring(Exception.class);

        WebElement clicked2 = wait113.until(new Function<WebDriver, WebElement>() {
            @Override
            public WebElement apply(WebDriver webDriver) {
                return wait113.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@id='export_result_url']")));

            }
        });
        clicked2.click();
    }*/
}
