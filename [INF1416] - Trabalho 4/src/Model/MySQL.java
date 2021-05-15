package Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


//Banco de dados relacional usuando MySQL
public class MySQL {
	
    private MySQL() {}

    private static Connection connection;
    private static ResultSet result;
    private static Statement statement;

    
    private static String connectionPath = "jdbc:mysql://localhost:3306/trabalho4?useTimezone=true&serverTimezone=UTC";
    private static MySQL data = null;


    public static MySQL getInstance() throws SQLException, ClassNotFoundException {
        if (data == null) {
            data = new MySQL();
        }
        return data;
    }

    public static void createConnection() throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection(connectionPath, "root", "");
    }


    public static ResultSet executeQuery(String query) throws SQLException {
        result = statement.executeQuery(query);
        return result;
    }
    
    public static void createStatement() throws SQLException {
        statement = connection.createStatement();
    }

    public static void executeUpdate(String sql) throws SQLException {
        statement.executeUpdate(sql);
        statement.close();
    }

    public static void closeConnection() {
        try {
            connection.close();
        } catch (SQLException se) {
        }
        try {
            statement.close();
        } catch (SQLException se) {
        }
        try {
            result.close();
        } catch (SQLException se) {
        }
    }
}
