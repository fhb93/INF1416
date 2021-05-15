package Model;


import java.util.Date;

//Classe dos registros

public class Registry {
	
    private String userID;
    private String fileID;
    private int code;
    private Date currentDate;


    public Registry(int code, String userID, String fileID, Date date) {
    	
        this.currentDate = date;
        this.code = code;
        this.userID = userID;
        this.fileID = fileID;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
    
    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }

    public String getFileID() {
        return fileID;
    }

    public void setFileID(String fileID) {
        this.fileID = fileID;
    }
    
    public void setDate(Date date) {
        this.currentDate = date;
    }
    
    public Date getDate() {
        return currentDate;
    }
}