/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ms_csvprocessor;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.mapforce.MappingMapToEPSupport;
//import com.mapforce.TraceTargetConsole;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author michaelgoode
 */
public class MS_CSVProcessor {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // which process are we going to run ? CMS or PLM ?
        String version = "MS_CSVProcessor Version 2.0 14th April 2016 - Michael Goode";
        System.out.println(version);
        if (args.length > 0) {
            process(args[0]);
        } else {
            System.out.println("Parameter required");
        }
    }

    private static void process(String route) {

        if (route.toUpperCase().equals("CMS")) {
            // run CMS
            processCMS(route, readCMSProperties());
        } else if (route.toUpperCase().equals("PLM")) {
            // run PLM
            processPLM(route, readPLMProperties());
        } else {
            System.out.println("Sorry parameter is invalid, must be CMS or PLM");
        }
    }

    private static void processCMS(String route, ConfigProperties props) {
        //getCMSfromSFTP(props);
        CSVLookup csv = CSVLookup.getInstance();
        csv.loadDB(props.getTempfolder() + props.getFilename(), route);
        sendEmail(route, props.getFilename());
    }

    private static void processPLM(String route, ConfigProperties props) {
        getPLMfromSFTP(props);
        CSVLookup csv = CSVLookup.getInstance();
        csv.loadDB(props.getTempfolder() + props.getFilename(), route);
        sendEmail(route, props.getFilename());
    }

    private static void sendEmail(String route, String filename) {
        com.sml.utils.EmailUtil email = com.sml.utils.EmailUtil.getInstance();
        email.sendMail(route, "Processed CSV data for " + route + "       file:" + filename, "garypringle@sml.com, michaelgoode@sml.com, kellyduffy@sml.com, charlottekinder@sml.com");
    }

    public static boolean getCMSfromSFTP(ConfigProperties props) {
        /*String hostname = "203.193.50.133";
         String login = "MSCMSDATA";
         String password = "7oA_xxxE";
         String directory = "MSCMSDATA_Production";
         String filename = "MSCMSDATA.TXT";*/
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        JSch ssh = new JSch();
        Session session;
        try {
            session = ssh.getSession(props.getUser(), props.getHost(), 22);
            session.setConfig(config);
            session.setPassword(props.password);
            session.connect();
            Channel channel = session.openChannel("sftp");
            channel.connect();
            ChannelSftp sftp = (ChannelSftp) channel;
            sftp.cd(props.ftpFolder);
            Vector<ChannelSftp.LsEntry> files = sftp.ls("*");
            for (ChannelSftp.LsEntry file : files) {
                if (!file.getAttrs().isDir()) {
                    String sftpfilename = file.getFilename().toUpperCase();
                    if (sftpfilename.equals(props.filename.toUpperCase())) {
                        BufferedWriter bos = new BufferedWriter(new FileWriter(props.getTempfolder() + file.getFilename()));
                        BufferedReader bis = new BufferedReader(new InputStreamReader(sftp.get(file.getFilename())));
                        String line = null;
                        while ((line = bis.readLine()) != null) {
                            bos.write(line);
                            bos.newLine();
                        }
                        bis.close();
                        bos.close();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_hh_mm");
                        String dateStr = sdf.format(new Date());
                        sftp.rename(props.filename, dateStr + props.filename);
                    }
                }
            }
            channel.disconnect();
            session.disconnect();

        } catch (Exception ex) {
            Logger.getLogger(MS_CSVProcessor.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    public static boolean getPLMfromSFTP(ConfigProperties props) {
        /*String hostname = "203.193.50.133";
         String login = "MSCMSDATA";
         String password = "7oA_xxxE";
         String directory = "PLM_MSCMSDATA_Production";
         String filename = "MSCMSDATA.TXT";*/

        if (props.getFileFromFTP.equalsIgnoreCase("true")) {
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            JSch ssh = new JSch();
            Session session;
            try {
                session = ssh.getSession(props.getUser(), props.getHost(), 22);
                session.setConfig(config);
                session.setPassword(props.password);
                session.connect();
                Channel channel = session.openChannel("sftp");
                channel.connect();
                ChannelSftp sftp = (ChannelSftp) channel;
                sftp.cd(props.ftpFolder);
                Vector<ChannelSftp.LsEntry> files = sftp.ls("*");
                for (ChannelSftp.LsEntry file : files) {
                    if (!file.getAttrs().isDir()) {
                        String sftpfilename = props.getFilename().toUpperCase();
                        String fileExt = sftpfilename.toUpperCase().substring(sftpfilename.lastIndexOf("."), sftpfilename.length());
                        if ((file.getFilename().startsWith("PLM")) && (file.getFilename().toUpperCase().contains(".TXT"))) {
                            BufferedWriter bos = new BufferedWriter(new FileWriter(props.getTempfolder() + sftpfilename));
                            BufferedReader bis = new BufferedReader(new InputStreamReader(sftp.get(file.getFilename())));
                            String line = null;
                            while ((line = bis.readLine()) != null) {
                                bos.write(line);
                                bos.newLine();
                            }
                            bis.close();
                            bos.close();
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_hh_mm");
                            String dateStr = sdf.format(new Date());
                            sftp.rename(file.getFilename(), dateStr + file.getFilename());
                        }
                    }
                }
                channel.disconnect();
                session.disconnect();
            } catch (Exception ex) {
                Logger.getLogger(MS_CSVProcessor.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
            return true;
        } else {
            return false;
        }
    }

    public static ConfigProperties readCMSProperties() {
        ConfigProperties cmsProps = new ConfigProperties("cms.properties");
        return cmsProps;
    }

    public static ConfigProperties readPLMProperties() {
        ConfigProperties plmProps = new ConfigProperties("plm.properties");
        return plmProps;
    }

}
