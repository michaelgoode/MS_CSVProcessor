/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ms_csvprocessor;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Properties;

/**
 *
 * @author michaelgoode
 */
public class ConfigProperties {

    String host;
    String user;
    String password;
    String ftpFolder;
    String filename;
    String tempfolder;
    String emailList;
    String getFileFromFTP;

    InputStream inputStream;

    public ConfigProperties(String routefilename) {
        try {

            Properties props = new Properties();
            String propFileName = routefilename;

            inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

            if (inputStream != null) {
                props.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + routefilename + "' not found in the classpath");
            }

            host = props.getProperty("host");
            user = props.getProperty("user");
            password = props.getProperty("password");
            ftpFolder = props.getProperty("ftpfolder");
            filename = props.getProperty("filename");
            tempfolder = props.getProperty("tempfilename");
            emailList = props.getProperty("email");
            getFileFromFTP = props.getProperty("getFileFromFTP");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFtpFolder() {
        return ftpFolder;
    }

    public void setFtpFolder(String ftpFolder) {
        this.ftpFolder = ftpFolder;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getTempfolder() {
        return tempfolder;
    }

    public void setTempfolder(String tempfolder) {
        this.tempfolder = tempfolder;
    }

    public String getGetFileFromFTP() {
        return getFileFromFTP;
    }

    public void setGetFileFromFTP(String getFileFromFTP) {
        this.getFileFromFTP = getFileFromFTP;
    }

}
