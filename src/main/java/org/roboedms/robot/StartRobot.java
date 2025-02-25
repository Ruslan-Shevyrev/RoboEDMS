/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.roboedms.robot;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableModel;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.roboedms.settings.Setting;
import org.roboedms.sql.AnswerSql;
import org.roboedms.sql.insert;
import org.roboedms.valueobject.ListValue;

/**
 *
 * @author shrv
 */
public class StartRobot {

    ArrayList<String> newTab;
    static int errorCount = 0;

    public FirefoxDriver start(JLabel connectLabel) {
        FirefoxDriver driver = null;
        try {
            FirefoxOptions options = new FirefoxOptions();
            //ProfilesIni profileIni = new ProfilesIni();
            FirefoxProfile profile = new FirefoxProfile();
            //FirefoxProfile profile = profileIni.getProfile("SeleniumProfile");
            //profile.addExtension(new File(setting.pathToJar + pathYandexMetric));
            //profile.addExtension(new File(setting.pathToJar + pathGoogleMetric));
            options.setProfile(profile);
            options.addPreference("browser.download.folderList", 2);
            options.addPreference("browser.download.dir", "~/temp");
            options.addPreference("browser.download.useDownloadDir", "true");
            options.addPreference("browser.helperApps.neverAsk.saveToDisk", "application/pdf;text/xml");
            options.addPreference("browser.download.panel.shown", "false");
            options.addPreference("browser.download.lastDir.savePerSite", "false");
            options.addPreference("browser.helperApps.alwaysAsk.force", false);
            options.addPreference("browser.download.manager.showWhenStarting", false);

            //FirefoxBinary firefoxBinary = new FirefoxBinary();
            //firefoxBinary.addCommandLineOptions("--headless");
            //options.setBinary(firefoxBinary);
            if (!Setting.visibalityBrowser) {
                options.addArguments("--headless");
            }
            driver = new FirefoxDriver(options);
            driver.manage().timeouts().implicitlyWait(5000, TimeUnit.MILLISECONDS);
            login(driver, connectLabel);
        } catch (WebDriverException e) {
            connectLabel.setForeground(Color.RED);
            connectLabel.setText("<html> Ошибка <br> соединения</html>");
            Logger.getLogger(StartRobot.class.getName()).log(Level.SEVERE, null, e);
        }
        return driver;
    }

    private void login(FirefoxDriver driver, JLabel connectLabel) {
        try {
            driver.get(Setting.deloURL);
            driver.findElement(By.id("LOGIN")).sendKeys(Setting.edmsUser);
            driver.findElement(By.id("pass")).sendKeys(Setting.edmsPassword);
            driver.findElement(By.xpath("/html/body/form/table/tbody/tr[2]/td/table/tbody/tr[3]/td/table/tbody/tr[3]/td[2]/input")).click();
            if (!driver.findElements(By.id("btnTerminate")).isEmpty()) {
                driver.findElement(By.id("btnTerminate")).click();
            }
            driver.findElement(By.xpath("/html/body/div[1]/form/div[8]/table[2]/tbody/tr[1]/td[2]/a")).click();
            connectLabel.setForeground(Color.GREEN);
            connectLabel.setText("<html> Соединение <br> установлено</html>");
        } catch (WebDriverException e) {
            connectLabel.setForeground(Color.RED);
            connectLabel.setText("<html> &nbsp Ошибка <br> соединения</html>");
            Logger.getLogger(StartRobot.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void selectData(FirefoxDriver driver, DefaultTableModel mod) {
        String messageType;
        String annotat;
        String dataDoc;
        String corr;
        String access;
        String composition;
        String delivery;
        String planDate;
        String docNumb;
        String textDoc;
        String signer;
        String author_errand;
        String corrDocNumb;
        String corrDateDoc;
        ArrayList<String> fileIdList;
        Vector<String> newRow;
        try {
            ArrayList<String> firstTab = new ArrayList<String>(driver.getWindowHandles());
            while (firstTab.size() != 1) {
                driver.close();
                driver.switchTo().window(firstTab.get(firstTab.size() - 1));
            }
            driver.findElement(By.id("ctl00_ctl00_PageBody_ForContext_ctl00_Folder1")).click();
            //driver.findElement(By.xpath("/html/body/div[1]/form/div[8]/table[2]/tbody/tr[1]/td[2]/a")).click();
            //driver.findElement(By.xpath("/html/body/div[1]/form/div[8]/table[2]/tbody/tr[2]/td[2]/a")).click();
            //driver.findElement(By.xpath("/html/body/div[1]/form/div[11]/table/tbody/tr[4]/td[4]/div/a")).click();
            WebElement table = driver.findElement(By.xpath("/html/body/div[1]/form/div[11]/table/tbody"));
            List<WebElement> allRows = table.findElements(By.tagName("tr"));
            while (!allRows.isEmpty() && Setting.start) {
                WebElement row = allRows.get(errorCount);
                try {
                    //for (WebElement row : allRows) {
                    messageType = null;
                    annotat = null;
                    dataDoc = null;
                    corr = null;
                    access = null;
                    composition = null;
                    delivery = null;
                    planDate = null;
                    docNumb = null;
                    textDoc = null;
                    signer = null;
                    author_errand = null;
                    corrDocNumb = null;
                    corrDateDoc = null;
                    fileIdList = new ArrayList<String>();
                    newRow = new Vector<String>();
                    List<WebElement> columns = row.findElements(By.tagName("td"));
                    messageType = columns.get(1).findElements(By.tagName("span")).get(0).getAttribute("innerHTML");
                    /*String planDateFlag = columns.get(9).findElements(By.className("cCont")).get(0).getAttribute("innerHTML");
                 if (!"".equals(planDateFlag)) {
                    //  planDate = planDateList.get(0).getAttribute("innerHTML");
                    planDate = columns.get(9).findElements(By.tagName("span")).get(0).getAttribute("innerHTML");
                }*/
                    String textListFlag = columns.get(10).findElements(By.className("cCont")).get(0).getAttribute("innerHTML");
                    if (!"".equals(textListFlag)) {
                        textDoc = columns.get(10).findElements(By.tagName("a")).get(0).getAttribute("innerHTML");
                    }
                    String authorErrandListFlag = columns.get(7).findElements(By.className("cCont")).get(0).getAttribute("innerHTML");
                    if (!"".equals(authorErrandListFlag)) {
                        author_errand = columns.get(7).findElements(By.tagName("a")).get(0).getAttribute("innerHTML");
                    }
                    String fileCount = columns.get(12).findElements(By.className("cCont")).get(0).getAttribute("innerHTML");
                    if (messageType.equals("Ис")) {
                        corrDocNumb = columns.get(3).findElements(By.tagName("a")).get(0).getAttribute("innerHTML");
                        corrDateDoc = columns.get(4).findElements(By.tagName("div")).get(0).getAttribute("innerHTML");
                        newRow.add(corrDocNumb);
                    } else {
                        docNumb = columns.get(3).findElements(By.tagName("a")).get(0).getAttribute("innerHTML");
                        dataDoc = columns.get(4).findElements(By.tagName("div")).get(0).getAttribute("innerHTML");
                        newRow.add(docNumb);
                    }
                    newRow.add("Выгрузка из ИС \"ДЕЛО-WEB\"");
                    mod.addRow(newRow);
                    columns.get(3).findElements(By.tagName("a")).get(0).click();
                    newTab = new ArrayList<String>(driver.getWindowHandles());
                    /* while (newTab.size() <= 1) {
                    String h = linkDoc.get(0).getAttribute("href");
                    newTab = new ArrayList<String>(driver.getWindowHandles());
                    Actions actions = new Actions(driver);
                    actions.moveToElement(linkDoc.get(0)).contextClick().build().perform();
                    String k = new String();
                }*/
                    driver.switchTo().window(newTab.get(1));
                    if (messageType.equals("Ис")) {
                        WebElement planDataList = driver.findElements(By.className("blockcard")).get(0).findElements(By.tagName("div")).get(1);
                        if (!"".equals(planDataList.getAttribute("innerHTML").replace(" ", "").replace("\n", ""))) {
                            planDate = planDataList.findElement(By.id("ctl00_cph_Repeater1_ctl00_plan_date")).getAttribute("innerHTML");
                        }
                        annotat = driver.findElement(By.id("ctl00_cph_Repeater1_ctl00_annotation")).getAttribute("innerHTML");
                        access = driver.findElement(By.id("ctl00_cph_Repeater1_ctl00_grif_name")).getAttribute("innerHTML");
                        composition = driver.findElement(By.id("ctl00_cph_Repeater1_ctl00_consists")).getAttribute("innerHTML");
                        signer = driver.findElement(By.xpath("//*[@id=\"ctl00_cph_listSigners_ctl00_SignedBySigner\"]")).getAttribute("innerHTML");
                        driver.findElement(By.xpath("/html/body/form/div[3]/div/table[2]/tbody/tr/td[1]/button[1]")).click();
                        newTab = new ArrayList<String>(driver.getWindowHandles());
                        driver.switchTo().window(newTab.get(2));
                        List<WebElement> corrDoc = driver.findElement(By.id("ctl00_cph_SJournalView")).findElements(By.tagName("tr"));
                        corrDoc.remove(0);
                        for (WebElement docTr : corrDoc) {
                            List<WebElement> corrDocTd = docTr.findElements(By.tagName("td"));
                            String lastTd = corrDocTd.get(4).getAttribute("innerHTML");
                            if (lastTd.equals("Картотека регистрации")) {
                                corr = corrDocTd.get(0).getAttribute("innerHTML");
                            }
                        }
                        driver.close();
                        driver.switchTo().window(newTab.get(1));
                    } else {
                        WebElement planDataList = driver.findElements(By.className("blockcard")).get(0).findElements(By.tagName("div")).get(1);
                        if (!"".equals(planDataList.getAttribute("innerHTML").replace(" ", "").replace("\n", ""))) {
                            planDate = planDataList.findElement(By.id("ctl00_cph_Repeater1_ctl00_plan_date")).getAttribute("innerHTML");
                        }
                        access = driver.findElement(By.id("ctl00_cph_Repeater1_ctl00_grif_name")).getAttribute("innerHTML");
                        composition = driver.findElement(By.id("ctl00_cph_Repeater1_ctl00_consists")).getAttribute("innerHTML");
                        delivery = driver.findElement(By.id("ctl00_cph_Repeater1_ctl00_delivery")).getAttribute("innerHTML");
                        List<WebElement> corrsInfoList;
                        if (!fileCount.equals("")) {
                            corrsInfoList = driver.findElement(By.xpath("/html/body/form/div[3]/div/div[7]/div[3]/table/tbody/tr[1]/td[2]")).findElements(By.tagName("span"));
                        } else {
                            corrsInfoList = driver.findElement(By.xpath("/html/body/form/div[3]/div/div[6]/div[3]/table/tbody/tr[1]/td[2]")).findElements(By.tagName("span"));
                        }

                        for (WebElement corrsInfo : corrsInfoList) {
                            switch (corrsInfo.getAttribute("id")) {
                                case "ctl00_cph_listCorresp_ctl00_corSigner":
                                    signer = corrsInfo.getAttribute("innerHTML");
                                    break;
                                case "ctl00_cph_listCorresp_ctl00_corDate":
                                    corrDateDoc = corrsInfo.getAttribute("innerHTML");
                                    break;
                                case "ctl00_cph_listCorresp_ctl00_corNum":
                                    corrDocNumb = corrsInfo.getAttribute("innerHTML");
                                    break;
                            }
                        }
                        annotat = driver.findElement(By.id("ctl00_cph_Repeater1_ctl00_annotation")).getAttribute("innerHTML");
                        corr = driver.findElement(By.id("ctl00_cph_listCorresp_ctl00_CorCorr")).getAttribute("innerHTML");
                    }
                    if (!fileCount.equals("")) {
                        driver.findElement(By.xpath("/html/body/form/div[3]/div/div[6]/div[2]/a[2]/img")).click();
                        newTab = new ArrayList<String>(driver.getWindowHandles());
                        driver.switchTo().window(newTab.get(2));
                        List<WebElement> linkFiles = driver.findElement(By.xpath("/html/body/form/div[3]/table/tbody/tr[2]/td[1]/div/div/div[2]/table")).findElements(By.tagName("tr"));
                        int numberRows = 0;
                        for (WebElement file : linkFiles) {
                            numberRows++;
                            if ((numberRows % 5) == 1) {
                                String fileId = file.findElements(By.tagName("a")).get(0).getAttribute("href");
                                fileId = fileId.substring(fileId.lastIndexOf('/') + 1);
                                fileIdList.add(fileId);
                            }
                        }
                        driver.close();
                        driver.switchTo().window(newTab.get(1));
                    }
                    /*   SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
                dateDoc_ = format.parse(dataDoc);
                if (planDate != null) {
                    plandate_ = format.parse(planDate);
                }*/
                    insert in = new insert();
                    in.insertSql(messageType,
                            dataDoc,
                            access,
                            composition,
                            delivery,
                            planDate,
                            annotat,
                            corr,
                            docNumb,
                            signer,
                            textDoc,
                            author_errand,
                            corrDateDoc,
                            corrDocNumb,
                            fileIdList);

                    driver.close();
                    driver.switchTo().window(newTab.get(0));
                    if (Setting.isDeleteMessage) {
                        columns.get(0).click();
                        driver.findElement(By.id("ctl00_ctl00_PageBody_ForActions_grOp_kart")).click();
                        driver.findElement(By.id("ctl00_ctl00_PageBody_ForActions_grOp_kart")).click();
                        List<WebElement> actions = driver.findElement(By.id("ctl00_ctl00_PageBody_ForActions_grOp_group")).findElements(By.tagName("li"));
                        clickOnInvisibleElement(actions.get(1).findElements(By.tagName("a")).get(0), driver);
                        Alert alert = (new WebDriverWait(driver, 10)).until(ExpectedConditions.alertIsPresent());
                        alert.dismiss(); // исправить на подтвердить
                        // alert.accept();
                    }
                    mod.setValueAt("Загружено", mod.getRowCount() - 1, 1);
                } catch (NoSuchElementException | IndexOutOfBoundsException e) {
                    if (Setting.isDeleteMessage) {
                        errorCount++;
                    }
                    mod.setValueAt("Ошибка", mod.getRowCount() - 1, 1);
                    Logger.getLogger(StartRobot.class.getName()).log(Level.SEVERE, null, e);
                }
                driver.navigate().refresh();
                Thread.sleep(2000);
                table = driver.findElement(By.xpath("/html/body/div[1]/form/div[11]/table/tbody"));
                allRows = table.findElements(By.tagName("tr"));
                if (!Setting.isDeleteMessage) {
                    errorCount++;
                }
            }
        } catch (NoSuchElementException | IndexOutOfBoundsException | InterruptedException e) {
            Logger.getLogger(StartRobot.class.getName()).log(Level.SEVERE, null, e);
        }
        Setting.start = false;
    }

    public void sendAnswer(FirefoxDriver driver, DefaultTableModel mod) {
        try {
            Vector<String> newRow;
            ArrayList<ListValue> listValues = new ArrayList<ListValue>();
            ArrayList<String> firstTab = new ArrayList<String>(driver.getWindowHandles());
            while (firstTab.size() != 1) {
                driver.close();
                driver.switchTo().window(firstTab.get(firstTab.size() - 1));
            }

            driver.findElement(By.id("ctl00_ctl00_PageBody_ForContext_ctl00_Folder2")).click();

            AnswerSql sql = new AnswerSql();
            listValues = sql.answer();
            for (ListValue value : listValues) {
                try {
                    newRow = new Vector<String>();
                    newRow.add(value.doc_numb);
                    newRow.add("Передача в ИС \"ДЕЛО-WEB\"");
                    mod.addRow(newRow);
                    List<WebElement> actions = driver.findElement(By.id("ctl00_ctl00_PageBody_ForActions_fMenu_folder")).findElements(By.tagName("li"));
                    clickOnInvisibleElement(actions.get(0).findElements(By.tagName("a")).get(0), driver);
                    Thread.sleep(1000);
                    newTab = new ArrayList<String>(driver.getWindowHandles());
                    driver.switchTo().window(newTab.get(1));
                    value.annotat = value.annotat.replace(' ', '%');
                    driver.findElement(By.id("ctl00_cph_Criteries_DOC__ANNOTAT")).clear();
                    driver.findElement(By.id("ctl00_cph_Criteries_DOC__ANNOTAT")).sendKeys(value.annotat);
                    driver.findElement(By.id("ctl00_cph_Criteries_DOC__REF_ACCESS_CARD__DATE_CARD_From")).clear();
                    driver.findElement(By.id("ctl00_cph_Criteries_DOC__REF_ACCESS_CARD__DATE_CARD_From")).sendKeys(value.date);
                    driver.findElement(By.id("ctl00_cph_Criteries_DOC__REF_ACCESS_CARD__DATE_CARD_To")).clear();
                    driver.findElement(By.id("ctl00_cph_Criteries_DOC__REF_ACCESS_CARD__DATE_CARD_To")).sendKeys(value.date);
                    driver.findElement(By.id("ctl00_buttons_Apply")).click();
                    driver.switchTo().window(newTab.get(0));
                    Thread.sleep(2000);
                    WebElement table = driver.findElement(By.xpath("/html/body/div[1]/form/div[11]/table/tbody"));
                    List<WebElement> allRows = table.findElements(By.tagName("tr"));
                    boolean flag = false;
                    for (WebElement row : allRows) {
                        List<WebElement> columns = row.findElements(By.tagName("td"));
                        if (columns.get(3).findElements(By.tagName("a")).get(0).getAttribute("innerHTML").equals(value.doc_numb) && !(columns.get(11).findElements(By.tagName("a")).isEmpty())) {
                            columns.get(11).findElements(By.tagName("a")).get(0).click();
                            newTab = new ArrayList<String>(driver.getWindowHandles());
                            driver.switchTo().window(newTab.get(1));
                            driver.findElement(By.xpath("/html/body/form/div[3]/table/tbody/tr[2]/td[1]/div/div/table/tbody/tr/td/table/tbody/tr/td[1]/img[1]")).click();
                            driver.findElement(By.xpath("/html/body/div[2]/div[2]/button")).click();
                            driver.findElement(By.id("ctl00_cph_replyView_STATUS_REPLY")).sendKeys("Окончательный");
                            driver.findElement(By.id("ctl00_cph_replyView_ContentTitle")).sendKeys(value.answer);
                            driver.close(); // Изменить на записать
                            driver.findElement(By.xpath("/html/body/form/div[3]/table/tbody/tr[2]/td[2]/div/a[3]")).click();
                            Thread.sleep(1000);
                            driver.switchTo().window(newTab.get(0));
                            sql.update(value.id_doc);
                            flag = true;
                        }
                    }
                    if (flag) {
                        mod.setValueAt("Передано", mod.getRowCount() - 1, 1);
                    } else {
                        mod.setValueAt("Ошибка", mod.getRowCount() - 1, 1);
                    }
                } catch (WebDriverException | InterruptedException e) {
                    mod.setValueAt("Ошибка", mod.getRowCount() - 1, 1);
                    Logger.getLogger(StartRobot.class.getName()).log(Level.SEVERE, null, e);
                }
            }

        } catch (WebDriverException e) {
            Logger.getLogger(StartRobot.class.getName()).log(Level.SEVERE, null, e);
        }
        Setting.startAnswer = false;
    }

    public void clickOnInvisibleElement(WebElement element, FirefoxDriver driver) {

        String script = "var object = arguments[0];"
                + "var theEvent = document.createEvent(\"MouseEvent\");"
                + "theEvent.initMouseEvent(\"click\", true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);"
                + "object.dispatchEvent(theEvent);";

        ((JavascriptExecutor) driver).executeScript(script, element);
    }
}
