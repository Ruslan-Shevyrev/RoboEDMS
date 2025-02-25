/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.roboedms;

import com.sun.glass.events.KeyEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.roboedms.robot.StartRobot;
import org.roboedms.settings.LogInFile;
import org.roboedms.settings.Setting;
import org.roboedms.sql.LoginSql;

/**
 *
 * @author shrv
 */
public class RoboEdmsMain {

    /**
     * @param args the command line arguments
     * @throws java.lang.InterruptedException
     */
    public static void main(String[] args) throws InterruptedException, WebDriverException {
        String myJarPath = RoboEdmsMain.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        Setting.pathToJar = new File(myJarPath).getParent();
        ImageIcon icon;
        ImageIcon iconLogin;
        ImageIcon iconLoginError;
        if (args.length != 0) {
            Setting.edmsUser = args[0];
            Setting.edmsPassword = args[1];
            Setting.visibalityBrowser = Boolean.valueOf(args[2]);
            Setting.isLogging = Boolean.valueOf(args[3]);
            Setting.isDeleteMessage = Boolean.valueOf(args[4]);
            System.setProperty("webdriver.gecko.driver", Setting.pathToJar + "/driver/geckodriver.exe");
            icon = new ImageIcon(Setting.pathToJar + "/image/icon.png");
            iconLogin = new ImageIcon(Setting.pathToJar + "/image/iconLogin.png");
            iconLoginError = new ImageIcon(Setting.pathToJar + "/image/iconLoginError.png");
        } else {
            icon = new ImageIcon("icon.png");
            iconLogin = new ImageIcon("iconLogin.png");
            iconLoginError = new ImageIcon("iconLoginError.png");
        }
        LogInFile.log();
        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame("Выгрузка из ИС \"ДЕЛО-WEB\"");
        JLabel connectLabel = new JLabel("Соединение");
        JLabel iconLabel = new JLabel();
        frame.setIconImage(icon.getImage());
        iconLabel.setIcon(icon);
        connectLabel.setHorizontalAlignment(SwingConstants.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(600, 300));
        frame.setResizable(false);
        String[] columnNames = {
            "Сообщение №",
            "Статус"
        };
        DefaultTableModel mod = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable();
        table.setModel(mod);
        JScrollPane scrollPane = new JScrollPane(table);
        JButton buttonStart = new JButton("Старт");
        buttonStart.addActionListener((ActionEvent e) -> {
            Setting.start = true;
            Setting.startAnswer = false;
        });
        JButton buttonStartDownload = new JButton("Ответ");
        buttonStartDownload.addActionListener((ActionEvent e) -> {
            Setting.start = false;
            Setting.startAnswer = true;
        });
        if (!Setting.isDeleteMessage) {
            buttonStartDownload.setEnabled(false);
        }
        JButton buttonStop = new JButton("Стоп");
        buttonStop.addActionListener((ActionEvent e) -> {
            Setting.start = false;
            Setting.startAnswer = false;
        });
        frame.setLayout(null);
        connectLabel.setForeground(Color.BLUE);
        frame.add(scrollPane).setBounds(5, 5, 480, 250);
        frame.add(buttonStart).setBounds(487, 140, 100, 30);
        frame.add(buttonStartDownload).setBounds(487, 180, 100, 30);
        frame.add(buttonStop).setBounds(487, 220, 100, 30);
        frame.add(connectLabel).setBounds(487, 90, 100, 50);
        frame.add(iconLabel).setBounds(498, 5, 100, 100);
        frame.setLocationRelativeTo(null);
        //frame.pack();
        //frame.setVisible(true);

        JFrame loginFrame = new JFrame("Авторизация");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setPreferredSize(new Dimension(300, 150));
        JLabel loginLabel = new JLabel("Имя пользователя");
        JLabel loginPass = new JLabel("Пароль");
        JLabel ErrorLabel = new JLabel();
        JLabel iconLabelLogin = new JLabel();
        iconLabelLogin.setIcon(iconLogin);
        ErrorLabel.setForeground(Color.RED);
        JTextField loginEdoc = new JTextField();
        JPasswordField pass = new JPasswordField();
        JButton okButton = new JButton("Войти");
        okButton.addActionListener((ActionEvent e) -> {
            LoginSql logSql = new LoginSql();
            if (logSql.login(loginEdoc.getText(), pass.getText(), ErrorLabel, loginLabel, loginPass)) {
                frame.pack();
                frame.setVisible(true);
                loginFrame.setVisible(false);
            } else {
                loginFrame.remove(iconLabelLogin);
                iconLabelLogin.setIcon(iconLoginError);
                loginFrame.add(iconLabelLogin).setBounds(205, 0, 70, 70);
                loginFrame.revalidate();
                loginFrame.repaint();

            }
        });
        okButton.setMnemonic(KeyEvent.VK_ENTER);
        loginFrame.add(loginLabel).setBounds(5, 5, 150, 20);
        loginFrame.add(loginEdoc).setBounds(5, 30, 150, 20);
        loginFrame.add(loginPass).setBounds(5, 60, 150, 20);
        loginFrame.add(pass).setBounds(5, 85, 150, 20);
        loginFrame.add(okButton).setBounds(170, 85, 100, 20);
        loginFrame.add(ErrorLabel).setBounds(170, 55, 105, 30);
        loginFrame.add(iconLabelLogin).setBounds(195, 10, 70, 70);
        loginFrame.setLayout(null);
        loginFrame.setResizable(false);
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setIconImage(icon.getImage());
        JRootPane rootPane = SwingUtilities.getRootPane(okButton);
        rootPane.setDefaultButton(okButton);
        loginFrame.pack();
        loginFrame.setVisible(true);
        StartRobot startRobot = new StartRobot();
        FirefoxDriver driver = startRobot.start(connectLabel);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                driver.quit();
                System.exit(0);
            }
        });
        loginFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                driver.quit();
                System.exit(0);
            }
        });

        while (true) {
            if (Setting.start) {
                startRobot.selectData(driver, mod);
            }
            if (Setting.startAnswer) {
                startRobot.sendAnswer(driver, mod);
            }
        }
    }

}
