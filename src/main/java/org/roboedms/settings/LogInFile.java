/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.roboedms.settings;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import org.roboedms.RoboEdmsMain;

/**
 *
 * @author shrv
 */
public class LogInFile {

    public static void log() {
        if (Setting.isLogging) {
            try {
                Logger logger = Logger.getLogger("");
                LocalDateTime curdate = LocalDateTime.now();
                String data = curdate.toString();
                data = data.substring(0, data.lastIndexOf('.'));
                data = data.replace('T', '_');
                data = data.replace(':', '.');
                FileHandler handler = new FileHandler(Setting.pathToJar + "/logs/" + data + ".log", 1048576, 1, true);
                handler.setLevel(Level.ALL);
                handler.setFormatter(new SimpleFormatter());
                logger.addHandler(handler);
            } catch (IOException | SecurityException ex) {
                Logger.getLogger(RoboEdmsMain.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }
}
