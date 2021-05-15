package Controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import Model.MySQL;


//Classe responsavel por registrar os registros

public class Register {
	
    private Register(){}

    public static void storeRegistry (int code, String userID, String fileID) {
     
    	SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        
        try {
            MySQL.createConnection();
            MySQL.createStatement();

            String query = "INSERT INTO registros (code, userID, fileID, datetime) VALUES" + String.format("('%s','%d','%s','%s');", code, userID, fileID, dateFormatter.format(date));

            MySQL.executeUpdate(query);
            MySQL.closeConnection();
        } catch (Exception e){
            return;
        }
    }
}