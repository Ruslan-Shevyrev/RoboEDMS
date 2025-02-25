/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.roboedms.sql;

import java.awt.Color;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.RowId;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import org.roboedms.connect.ConnectToPostgres;
import org.roboedms.settings.Setting;

/**
 *
 * @author shrv
 */
public class LoginSql {

    private final String SQLLOGIN = "select * from  edms.employees where username = ?";
    private final String SQLIDORG = "select * from edms.roles where user_id = (select user_id from edms.employees where username = ?) and role = 'registrator'";

    public boolean login(String login, String password, JLabel errorLabel, JLabel loginLabel, JLabel passwordLabel) {
        boolean result = false;
        try (Connection conEDMS = ConnectToPostgres.getConnection()) {
            if (conEDMS != null) {
                PreparedStatement psLogin = conEDMS.prepareStatement(SQLLOGIN);
                psLogin.setString(1, login);
                ResultSet rs = psLogin.executeQuery();
                if (rs.next()) {
                    if (rs.getString("password").equals(password)) {
                        PreparedStatement psIdOrg = conEDMS.prepareStatement(SQLIDORG);
                        psIdOrg.setString(1, login);
                        ResultSet rsOrg = psIdOrg.executeQuery();
                        if (rsOrg.next()) {
                            Setting.orgID = rsOrg.getLong("group_id");
                        }
                        result = true;
                    } else {
                        loginLabel.setForeground(Color.BLACK);
                        passwordLabel.setForeground(Color.RED);
                        errorLabel.setText("<html> &nbsp &nbsp Неверный <br> &nbsp &nbsp &nbsp пароль </html>");
                    }
                } else {
                    loginLabel.setForeground(Color.RED);
                    passwordLabel.setForeground(Color.BLACK);
                    errorLabel.setText("<html> Пользователь <br> &nbsp &nbsp не найден </html>");
                }
            } else {
                errorLabel.setText("<html> &nbsp &nbsp &nbsp Ошибка <br> &nbsp соединения </html>");
            }
        } catch (SQLException ex) {
            Logger.getLogger(LoginSql.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

}
