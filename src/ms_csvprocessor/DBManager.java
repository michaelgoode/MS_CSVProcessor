/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ms_csvprocessor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author michaelgoode
 */
public class DBManager {
    private static final Logger LOG = Logger.getLogger(DBManager.class.getName());

    private static final DBManager instance = new DBManager();

    public static DBManager getInstance() {
        return instance;
    }

    public DBManager() {

    }

    public Connection getConnection(String connType) {
        Connection conn = null;
        if (connType.equals("EDI")) {
            conn = getEDIConnection();
        } else if (connType.equals("LOOKUP")) {
            conn = getLookupConnection();
        }
        return conn;
    }
    
    public Connection getLookupConnection() {

        Connection con = null;

        String RL = "jdbc:sqlserver://192.168.0.65:1489;DatabaseName=lookup";

        String user = "lookup_admin";

        String password = "sbit";

        try {

            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            con = DriverManager.getConnection(RL, user, password);

        } catch (Exception e) {
            LOG.log(Level.SEVERE,null,e);
        }
        return con;

    }
    
    public Connection getEDIConnection() {

        Connection con = null;

        String RL = "jdbc:sqlserver://192.168.0.52:1488;DatabaseName=edi_eplatform";

        String user = "sbitapp";

        String password = "sbit";

        try {

            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            con = DriverManager.getConnection(RL, user, password);

        } catch (Exception e) {
            LOG.log(Level.SEVERE,null,e);
        }
        return con;

    }
    
    public Connection getOrderPoolConnection() {

        Connection con = null;

        String RL = "jdbc:sqlserver://192.168.2.8:1433;DatabaseName=OrderPool";

        String user = "sa";

        String password = "";
        
        System.out.println("Connect to " + RL);

        try {

            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            con = DriverManager.getConnection(RL, user, password);

        } catch (Exception e) {
            LOG.log(Level.SEVERE,null,e);
        }
        return con;

    }
}
