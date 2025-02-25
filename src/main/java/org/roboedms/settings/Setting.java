/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.roboedms.settings;

/**
 *
 * @author shrv
 */
public class Setting {
    public volatile static boolean start = false;
    public volatile static boolean startAnswer = false;
    public static String deloURL = "url";
    public static String edmsUser = "username";
    public static String edmsPassword = "password";
    public static Long orgID;
    public static boolean isLogging = true;
    public static boolean visibalityBrowser = true;
    public static boolean isDeleteMessage = false;
    public static String pathToJar;
}
