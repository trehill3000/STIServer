package com.stiserver.webAutomation.bLogic;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WebBadger {

    private String url;
    private String siteName;
    private String username;
    private String password;
    private String path;
    private WebDriver driver;

    /**
     * Default
     *
     */
    public WebBadger(){
    }

    /**
     * Receive Username, Password, Path
     * @param p path
     * @param un username
     * @param pw password
     */
    public void Badger(String p, String un, String pw, String siteName) throws InterruptedException, IOException, ParseException {
        this.path = p;
        this.username = un;
        this.password = pw;
        this.siteName = siteName;
        driver = new FirefoxDriver(getSettings());
        login();
        getAdditionalReport();
        getBadgerReports();
        driver.close();
    }

    /**LOGIN IN
     *
     * @throws InterruptedException E
     */
    public void login() throws InterruptedException {

        //CLEAN UP
        driver.manage().deleteAllCookies();
        driver.manage().timeouts().pageLoadTimeout(40, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

        //LOGIN
        driver.get("https://beaconama.net/signin");
        driver.findElement(By.id("username")).sendKeys(username);
        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.id("ba-submit")).click();
        Thread.sleep(3000);

    }

    /**
     *GET REPORTS
     */
    private void getBadgerReports() throws InterruptedException, IOException, ParseException {

        //DOWNLOAD PRE-PROVISIONED AND PROVISIONED REPORTS
        driver.findElement(By.id("snassets")).click();
        Thread.sleep(2000);
        driver.findElement(By.xpath("//a[@href='#admin-tab-endpoints']")).click();
        Thread.sleep(2000);
        driver.findElement(By.id("batch-actions-pre")).click();
        driver.findElement(By.id("action-export-pre-extended")).click();
        driver.findElement(By.id("asset-export-button")).click();

        //WAIT FOR ELEMENT AND CLICK
        WebDriverWait wait = new WebDriverWait(driver, 100000);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@id='export_result_url']")));
        Thread.sleep(3000);
        driver.findElement(By.id("export_result_url")).click();

        //GET NEXT REPORT
        driver.navigate().refresh();
        Thread.sleep(2000);
        driver.findElement(By.xpath("//a[@href='#admin-tab-endpoints']")).click();
        Thread.sleep(2000);
        driver.findElement(By.id("batch-actions")).click();
        driver.findElement(By.id("action-export-extended")).click();
        Thread.sleep(2000);
        driver.findElement(By.id("asset-export-button")).click();

        //WAIT FOR ELEMENT AND CLICK
        WebDriverWait wait2 = new WebDriverWait(driver, 100000);
        wait2.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@id='export_result_url']")));
        Thread.sleep(2000);
        driver.findElement(By.id("export_result_url")).click();

        //REFRESH
        driver.navigate().refresh();

    }

    /**
     *GET ADDITIONAL ANALYTICAL REPORTS
     */
    private void getAdditionalReport() throws InterruptedException, IOException, ParseException {

        //DID IT DOWNLOAD?
        boolean downloaded = false;
        Thread.sleep(2000);

        //DOWNLOAD ENDPOINT TAMPER REPORT______________________________________________________________________________________________________________________________
        driver.navigate().to("https://beaconama.net/admin/portfolio");
        try {
            driver.navigate().to("https://beaconama.net/admin/portfolio/monitor?i=aag%3Aexceptions%3ATamper%2CMagneticTamper%3Aall&view=map");

            Thread.sleep(2000);
            driver.findElement(By.id("batch-actions")).click();
            driver.findElement(By.id("action-export-data")).click();
            Thread.sleep(2000);
            driver.findElement(By.id("btn-export-new")).click();

            //WAIT FOR ELEMENT AND CLICK
            WebDriverWait wait = new WebDriverWait(driver, 100000);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@id='export_result_url']")));
            Thread.sleep(3000);
            driver.findElement(By.id("export_result_url")).click();
            downloaded = true;
        } catch (Exception e) {
            driver.navigate().back();
        }
        //FIND MOST RECENT MOD FILE AND MOVE TO ANOTHER DIR
        if (downloaded) {
            //Anonymous Async Function
            CompletableFuture<Void> future = CompletableFuture.runAsync(new Runnable() {

                @Override
                public void run() {
                    //GET MOST RECENT DOWNLOAD FILE FROM DOWNLOAD DIR
                    File lastModFile = getMostResModFile();
                    assert lastModFile != null;

                    //GET CURRENT DATE
                    SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
                    //http://tutorials.jenkov.com/java-nio/files.html#files-move
                    Path destination = Paths.get("C:\\Users\\UMS\\Documents\\z_New computer\\Sites\\Active\\" + siteName + "\\ANALYTICS\\LEAKS\\" + siteName + "_Tamper_Report_" + formatter.format(new Date()) + ".csv");

                    try {
                        Files.move(lastModFile.toPath(), destination);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            downloaded = false;
        }

        //DOWNLOAD METER/ENDCODER ALARMS  REPORT______________________________________________________________________________________________________________________________
        Thread.sleep(2000);
        driver.navigate().to("https://beaconama.net/admin/portfolio");
        //DOWNLOAD REPORT
        try {
            driver.navigate().to("https://beaconama.net/admin/portfolio/monitor?i=aag%3Aexceptions%3AEmptyPipe%2CWaterPressureSensorError%2CEncoderExceedingMaxFlow%2CWaterTemperatureSensorError%2CEncoderSensorError%2CEncoderTemperature%2CEndOfLife%2CEncoderDialChange%2CEncoderRemoval%2CEncoderProgrammed%2CEncoderMagneticTamper%2CMeterTemperatureSensorError%3Aall&view=map");

            Thread.sleep(2000);
            driver.findElement(By.id("batch-actions")).click();
            driver.findElement(By.id("action-export-data")).click();
            Thread.sleep(2000);
            driver.findElement(By.id("btn-export-new")).click();

            //WAIT FOR ELEMENT AND CLICK
            WebDriverWait wait = new WebDriverWait(driver, 100000);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@id='export_result_url']")));
            Thread.sleep(3000);
            driver.findElement(By.id("export_result_url")).click();
            downloaded = true;
        } catch (NumberFormatException ignored) {}
        //FIND MOST RECENT MOD FILE AND MOVE TO ANOTHER DIR
        if (downloaded) {
            //Anonymous Async Function
            CompletableFuture<Void> future = CompletableFuture.runAsync(new Runnable() {
                @Override
                public void run() {
                    //GET MOST RECENT DOWNLOAD FILE FROM DOWNLOAD DIR
                    File lastModFile = getMostResModFile();
                    assert lastModFile != null;

                    //GET CURRENT DATE
                    SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");

                    //http://tutorials.jenkov.com/java-nio/files.html#files-move
                    Path destination = Paths.get("C:\\Users\\UMS\\Documents\\z_New computer\\Sites\\Active\\" + siteName + "\\ANALYTICS\\LEAKS\\" +siteName +  "_Encoder_Report_" + formatter.format(new Date()) + ".csv");

                    try {
                        Files.move(lastModFile.toPath(), destination);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            downloaded = false;
        }

        //DOWNLOAD LEAK ALARMS  REPORT______________________________________________________________________________________________________________________________
        Thread.sleep(2000);
        driver.navigate().to("https://beaconama.net/admin/portfolio");
        try {
            driver.navigate().to("https://beaconama.net/admin/portfolio/monitor?i=aag%3Aflow%3Aleak%3Aall&view=map");

            Thread.sleep(2000);
            driver.findElement(By.id("batch-actions")).click();
            driver.findElement(By.id("action-export-data")).click();
            Thread.sleep(2000);
            driver.findElement(By.id("btn-export-new")).click();


            //WAIT FOR ELEMENT AND CLICK
            WebDriverWait wait = new WebDriverWait(driver, 100000);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@id='export_result_url']")));
            Thread.sleep(3000);
            driver.findElement(By.id("export_result_url")).click();
            downloaded = true;
        } catch (NumberFormatException ignored) {}
        //FIND MOST RECENT MOD FILE AND MOVE TO ANOTHER DIR
        if (downloaded) {
            //Anonymous Async Function
            CompletableFuture<Void> future3 = CompletableFuture.runAsync(new Runnable() {
                @Override
                public void run() {
                    //GET MOST RECENT DOWNLOAD FILE FROM DOWNLOAD DIR
                    File lastModFile = getMostResModFile();
                    assert lastModFile != null;

                    //GET CURRENT DATE
                    SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
                    //http://tutorials.jenkov.com/java-nio/files.html#files-move
                    Path destination = Paths.get("C:\\Users\\UMS\\Documents\\z_New computer\\Sites\\Active\\" + siteName + "\\ANALYTICS\\LEAKS\\" + siteName + "_Leak_Report_" + formatter.format(new Date()) + ".csv");

                    try {
                        Files.move(lastModFile.toPath(), destination);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            downloaded = false;
        }

        //DOWNLOAD BACK FLOW REPORT____________________________________________________________________________________________________________________________________
        Thread.sleep(2000);
        driver.navigate().to("https://beaconama.net/admin/portfolio");
        try {
            driver.navigate().to("https://beaconama.net/admin/portfolio/monitor?i=aag%3Aflow%3Aback_flow%3Aall&view=map");

            Thread.sleep(2000);
            driver.findElement(By.id("batch-actions")).click();
            driver.findElement(By.id("action-export-data")).click();
            Thread.sleep(2000);
            driver.findElement(By.id("btn-export-new")).click();

            //WAIT FOR ELEMENT AND CLICK
            WebDriverWait wait = new WebDriverWait(driver, 100000);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@id='export_result_url']")));
            Thread.sleep(3000);
            driver.findElement(By.id("export_result_url")).click();
            downloaded = true;
        } catch (NumberFormatException ignored) {}
        if (downloaded) {
            //Anonymous Async Function
            CompletableFuture.runAsync(() -> {

                    //GET MOST RECENT DOWNLOAD FILE FROM DOWNLOAD DIR
                    File lastModFile = getMostResModFile();
                    assert lastModFile != null;

                    //GET CURRENT DATE
                    SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
                    //http://tutorials.jenkov.com/java-nio/files.html#files-move
                    Path destination = Paths.get("C:\\Users\\UMS\\Documents\\z_New computer\\Sites\\Active\\" + siteName + "\\ANALYTICS\\BACK_FLOW\\" + siteName + "_Back_Flow_Report_" + formatter.format(new Date()) + ".csv");

                    try {
                        Files.move(lastModFile.toPath(), destination);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            });
            downloaded = false;
        }

        //REFRESH
        driver.navigate().refresh();
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
     * returns most recent mod file dir
     * @return FILE
     */
    private File getMostResModFile() {
        //GET STRING NAME OF FILES IN A DIR
        Stream<Path> walk = null;
        try {
            walk = Files.walk(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert walk != null;
        List<String> result = walk.filter(Files::isRegularFile).map(Path::toString).collect(Collectors.toList());
        //result.forEach(System.out::println);
        File lastModFile =  null;

        //SET STRING TO FILE obj
        ArrayList<File> tempFileName = new ArrayList<>();
        for(String s: result) {
            tempFileName.add(new File(s));
        }

        //GET THE LAST MODIFIED DATE
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date date1 = null;//<--default date
        try {
            date1 = sdf.parse(sdf.format(tempFileName.get(0).lastModified()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        for(File file: tempFileName) {
            //  System.out.println(" Date1: " + date1);
            //    System.out.println(" Date2: " + sdf.format(file.lastModified()));
            // System.out.println(" Format:" + file + " " + sdf.format(file.lastModified()));
            Date date2 = null;
            try {
                date2 = sdf.parse(sdf.format(file.lastModified()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            assert date1 != null;
            assert date2 != null;
            if (date1.compareTo(date2) < 0) {
                //System.out.println("--Date1 is before Date2");
                date1 = date2;
                lastModFile = file;
            } else if (date1.compareTo(date2) == 0) {
                //System.out.println("--Date1 is equal to Date2");
                date1 = date2;
                lastModFile = file;
            }
        }

        return lastModFile;
    }

    /**
     * Get path
     * @return get path
     */
    public String getPath(){return path;}
}
