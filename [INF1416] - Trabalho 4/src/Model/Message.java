package Model;

//Messagem dos registros - possuem codigo e texto
public class Message {
	
    private String messageText;
    private int messageCode;

    public Message(String text)  {
        this.messageText = text;
    }
    
    public String getText() {
        return messageText;
    }

    public void setText(String text) {
        this.messageText = text;
    }

    public void setCode(int code) {
        this.messageCode = code;
    }

    public int getCode() {
        return messageCode;
    }
}