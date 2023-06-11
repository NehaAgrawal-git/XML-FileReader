import java.sql.*;
import java.util.ArrayList;

/**
 * Class for DB utility.
 */
public class DatabaseHelper {
    static Connection conn;
    static final String DB_URL = "jdbc:mysql://localhost/TESTINGDB";
    final String USER = "root";
    final String PASS = "Insert_your_password";

    static String TABLE_NAME = "XmlDataFinal1";

    public void setTableName(String tableName) {
        TABLE_NAME = tableName;
    }

    public Boolean openConnection() {
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            return true;
        } catch (Exception e) {
            System.out.println("Connection not opened" + e);
            return false;
        }
    }

    public void getData() {
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + TABLE_NAME);
            while (rs.next()) {
                // Retrieve by column name
                System.out.print("Project Name : " + rs.getString("projectName"));
                System.out.print("Project Name : " + rs.getString("spId"));
                System.out.println();
            }
        } catch (Exception e) {
            System.out.println("Something bad happened" + e);
        }
    }

    public Boolean checkIfSheetNameExists(String projectName) {
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT projectName FROM " + TABLE_NAME + " WHERE projectName = '" + projectName + "'");
            return rs.next();
        } catch (Exception e) {
            System.out.println("Something bad happened" + e);
            return true;
        }
    }

    public void insertData(String projectName, String spId, ArrayList<Integer> list) {
        try {
            Statement stmt = conn.createStatement();
            if (!checkIfSheetNameExists(projectName)) {
                stmt.executeUpdate("INSERT INTO " + TABLE_NAME + " VALUES (" + "'" + projectName + "', " + "'" + spId + "', " + list.get(0) + "," + list
                        .get(1) + "," + list.get(2) + "," + list.get(3) + "," + list.get(4) + "," + list
                        .get(5) + ");");
                System.out.println("Data inserted successfully");
            } else {
                System.out.println("Sheet Name and count already exist");
            }
        } catch (Exception e) {

            System.out.println("Something Bad Happened" + e);
        }
    }
}
