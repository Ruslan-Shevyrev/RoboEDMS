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
public class ConnectToOracle {
    public static Connection getConnection() {
        Connection con = null;
        try {
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            con = DriverManager.getConnection("jdbc:oracle:thin:@10.1.0.9:1521:orcl", "user", "pass");
            return con;
        } catch (SQLException ex) {
                Logger.getLogger(ConnectToOracle.class.getName()).log(Level.SEVERE, null, ex);
        }
        return con;
    }
    
}
