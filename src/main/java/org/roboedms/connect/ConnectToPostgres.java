/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.roboedms.connect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author shrv
 */
public class ConnectToPostgres {

    public static Connection getConnection() {
        Connection con = null;
        try {
            con = DriverManager.getConnection("jdbc:postgresql://10.1.0.2:5432/", "user", "pass");
            return con;
        } catch (SQLException ex) {
            Logger.getLogger(ConnectToPostgres.class.getName()).log(Level.SEVERE, null, ex);
        }
        return con;
    }
}
