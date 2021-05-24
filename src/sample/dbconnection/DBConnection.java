package sample.dbconnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    public static Connection establishConnection() {
        try {
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/energy", "root", "root");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
}
