/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sml.utils;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author michaelgoode
 */
public class EmailUtil {

    private static final EmailUtil instance = new EmailUtil();
    private LinkedHashSet<String> messageLines;
    private LinkedHashSet<String> attachments;

    public static EmailUtil getInstance() {
        return instance;
    }

    public EmailUtil() {

        messageLines = new LinkedHashSet<String>();
        attachments = new LinkedHashSet<String>();

    }

    public void addMessageLine( String msg ) {

        messageLines.add(msg);

    }

    public void addAttachment( String attach ) {

        attachments.add(attach);

    }
    /*
    public void sendMail(String subject, String body) {

        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.host", "imail.sml.com");
        props.setProperty("mail.user", "Burberry");  //smlsysdev
        props.setProperty("mail.password", "ss2006");
        try {
            Session mailSession = Session.getDefaultInstance(props, null);
            Transport transport = mailSession.getTransport();
            messageLines.add(body);
            MimeMessage message = new MimeMessage(mailSession);
            message.setSubject(subject);
            message.setContent(messageLines.toString(), "text/plain");
            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress("michaelgoode@sml.com"));

            transport.connect();
            transport.sendMessage(message,
                    message.getRecipients(Message.RecipientType.TO));
            transport.close();
        } catch (Exception ex) {
        }

    }
    
     */

    public void sendMail(String subject, String body, String recipients) {

        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.host", "imail.sml.com");
        props.setProperty("mail.user", "sysadmin");
        props.setProperty("mail.password", "ss2006");
        try {
            Session mailSession = Session.getDefaultInstance(props, null);
            Transport transport = mailSession.getTransport();
            MimeMessage message = new MimeMessage(mailSession);
            message.setSubject(subject);
          
            Multipart mp = new MimeMultipart();

            message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients));

            MimeBodyPart bodyPart = new MimeBodyPart();
            messageLines.add(body);

            Iterator iter = messageLines.iterator();

            body = "";

            while ( iter.hasNext() ) {
       
                body = body + String.format("%s\r\n", (String) iter.next());

            }
            
            bodyPart.setText(body);
            mp.addBodyPart(bodyPart);

            message.setContent(new MimeMultipart());
            sendAttachments(mp);
            // Add the Multipart to the message
            message.setContent(mp);
            transport.connect();
            transport.sendMessage(message,message.getRecipients(Message.RecipientType.TO));
            transport.close();
            attachments.clear();
            messageLines.clear();
        } catch (Exception ex) {

        }

    }
    
    private void sendAttachments(Multipart mp) {
        try {
        String attachment;
        
        Iterator iter = attachments.iterator();
        while (iter.hasNext()) {
            attachment = (String) iter.next();
            if (attachment != null) {
                MimeBodyPart mbp = new MimeBodyPart();
                FileDataSource fds = new FileDataSource(attachment);
                mbp.setDataHandler(new DataHandler(fds));
                mbp.setFileName(fds.getName());
                mp.addBodyPart(mbp);
            }
        }
        } catch (Exception ex) {

        }
    }

    public void sendErrorEmail(String infileName, String recipients) {

        this.sendMail(String.format("Burberry file %s failed preloader.", infileName), "Please see attached log for details", recipients);

    }

    public void sendSuccessEmail(String infileName, String recipients) {

        this.sendMail(String.format("Burberry file %s successfully passed preloader.", infileName), "Please see attached log for details", recipients);

    }
}
