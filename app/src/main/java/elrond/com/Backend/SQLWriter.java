package elrond.com.Backend;

import android.util.Log;
import java.sql.DriverManager;
import java.sql.Connection;
import android.os.StrictMode;

/**
 * This class is for creating a connection with the SQL Server
 * Created by User on 2017-08-04.
 */

public class SQLWriter {

    // values for connection
    private String ip = "";
    private String username = "";
    private String password = "";
    private String driverClass = "net.sourceforge.jtds.jdbc.Driver";
    private String dbName = "jokesonus";
    private String connectionString = "jdbc:jtds:sqlserver://" + this.ip + ";"
            + "databaseName=" + this.dbName + ";user=" + this.username + ";password="
            + this.password + ";";

    public Connection conn;

    public SQLWriter(){
        connectToDatabase();
    }

    private void connectToDatabase(){
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            Class.forName(driverClass);
            this.conn = DriverManager.getConnection(connectionString);
            Log.w("Connection", "open");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void closeConnection(){
        try {
            this.conn.close();
        }  catch (Exception e) {
            Log.w("Error connection", "" + e.getMessage());
        }
    }

}