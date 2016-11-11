/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ms_csvprocessor;

import com.mapforce.MappingMapToEPSupport;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;




/**
 *
 * @author michaelgoode
 */
public class CSVLookup {

    private static final CSVLookup instance = new CSVLookup();

    public static CSVLookup getInstance() {
        return instance;
    }

    public CSVLookup() {

    }
    
    public void loadDB(String filename, String route) {
        
        File infile = new File( filename );
        
        if (!infile.exists()) {
            return;
        }
        
        System.err.println("Mapping Application");
         
        try { // Mapping
            
            java.util.Hashtable	mapArguments = new java.util.Hashtable();
            mapArguments.put( "input", route );
            TraceTargetConsole ttc = new TraceTargetConsole();

            // create a new connection, multi use it 
            /*
            java.sql.Connection conn = com.altova.db.Dbs.newConnection(
                    "jdbc:sqlserver://192.168.0.65:1489;DatabaseName=lookup",
                    "lookup_admin",
                    "sbit");*/
            
            DBManager db = DBManager.getInstance();
            
            Connection conn = db.getOrderPoolConnection(); // connect to table in OrderPool

            MappingMapToEPSupport MappingMapToEPSupportObject = new MappingMapToEPSupport();

            java.sql.DriverManager.setLogWriter(new java.io.PrintWriter(new java.io.StringWriter()/*java.lang.System.out*/));

            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            MappingMapToEPSupportObject.registerTraceTarget(ttc);

            {
                com.altova.io.Input MSCMSDATAFlex2Source = com.altova.io.StreamInput.createInput(filename);

                MappingMapToEPSupportObject.run(MSCMSDATAFlex2Source, com.altova.CoreTypes.castToString((String) mapArguments.get("input")), conn);
            }

            File file = new File(filename);
            System.err.println("File deleted");
            file.delete();
            System.err.println("Finished");
        } catch (com.altova.UserException ue) {
            System.err.print("USER EXCEPTION:");
            System.err.println(ue.getMessage());
            System.exit(1);
        } catch (com.altova.AltovaException e) {
            System.err.print("ERROR: ");
            System.err.println(e.getMessage());
            if (e.getInnerException() != null) {
                System.err.print("Inner exception: ");
                System.err.println(e.getInnerException().getMessage());
                if (e.getInnerException().getCause() != null) {
                    System.err.print("Cause: ");
                    System.err.println(e.getInnerException().getCause().getMessage());
                }
            }
            System.err.println("\nStack Trace: ");
            e.printStackTrace();
            System.exit(1);
        } catch (Exception e) {
            System.err.print("ERROR: ");
            System.err.println(e.getMessage());
            System.err.println("\nStack Trace: ");
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    
    
    private void clearTable(java.sql.Connection conn, String route) {
        String sql = "delete from MSMS_TXTDATA where file_type = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, route);
            ps.execute();
        } catch (SQLException ex) {
            Logger.getLogger(MS_CSVProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    class TraceTargetConsole implements com.altova.TraceTarget {

        public void writeTrace(String info) {
            System.err.println(info);
        }
    }
    
}
