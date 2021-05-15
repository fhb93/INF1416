package Log;

import Model.MySQL;

import javax.swing.*;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


//Classe que gera a tabela com os registros e mensagens
public class LogView {
	
    public static JFrame frame;
    public static JTable table;
    
    public static List<String> date = new ArrayList<>();
    public static List<String> code = new ArrayList<>();
    public static List<String> userID = new ArrayList<>();
    public static List<String> fileID = new ArrayList<>();
    public static List<String> text = new ArrayList<>();

    public static boolean LogView() {
    	
        frame = new JFrame();
        frame.setTitle("Tabela de Mensagens de Registro");

        try {
        	
        	MySQL.createConnection();
        	MySQL.createStatement();
        	
            ResultSet result = MySQL.executeQuery(String.format("SELECT * FROM registros INNER JOIN mensagens ON registros.codigo = mensagens.codigo ORDER BY datetime"));
            
            if (result != null && result.next()) {
                date.add(result.getString(1));
                code.add(String.valueOf(result.getInt(2)));
                userID.add(result.getString(3));
                fileID.add(result.getString(4));
                text.add(result.getString(6));
                
                while (result.next()){
                    date.add(result.getString(1));
                    code.add(String.valueOf(result.getInt(2)));
                    userID.add(result.getString(3));
                    fileID.add(result.getString(4));
                    text.add(result.getString(6));
                }
                
                MySQL.closeConnection();
                return true;
            }
            
            return false;
            
        } catch (Exception e) {
            return false;
        }

    }
    
    public static void main(String[] args) {
        LogView.LogView();

        String[][] data = new String[date.size()][5];

        for(int i = 0; i < date.size(); i++){
            data[i][0] = date.get(i);
            data[i][1] = code.get(i);
            data[i][2] = userID.get(i);
            data[i][3] = fileID.get(i);
            data[i][4] = text.get(i);
        }

        String[] columnNames = {"Código", "Nome_Login", "Nome_Arquivo", "Mensagem", "Data"};

        table = new JTable(data, columnNames);
        table.setBounds(30, 40, 200, 300);

        JScrollPane scrollPanel = new JScrollPane(table);
        frame.add(scrollPanel);
        frame.setSize(500, 200);
        frame.setVisible(true);

    } 
}

