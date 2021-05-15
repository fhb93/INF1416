package Controller;

import Model.MySQL;
import Model.Registry;

import java.sql.SQLException;


// Classe responsalvel por registrar as mensagens dos registros

public class Messages {
    
	private Messages() {}

    public static void storeMessage (String messageText, Registry registry) throws SQLException, IllegalAccessException, InstantiationException, ClassNotFoundException {

        MySQL.createConnection();
        MySQL.createStatement();

        int code = registry.getCode();

        String query = "INSERT INTO mensagens (codigo, texto) VALUES" + String.format("('%d','%s');", code, messageText);
        MySQL.executeUpdate(query);
        MySQL.closeConnection();
    }
}