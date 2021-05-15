package Model;


//Classe do Usuario - todo usuario possui um grupo, um salt, numeors de acessos, numero de consultas, nome, email e uma verificacao se está bloqueado ou nao.
public class User {

    private boolean isBlocked;
    private String name;
    private String email;
    private Group group;
    private String saltNumber;
    private String value;
    private int access;
    private int consultation;

    
    public void getIsBlocked(boolean isUserBlocked) {
        this.isBlocked = isUserBlocked;
    }
    
    public boolean setIsBlocked() {
        return isBlocked;
    }
    
    public void setName(String userName) {
        this.name = userName;
    }

    public String getName() {
        return name;
    }
 
    public void setEmail(String email) {
        this.email = email;
    }

    
    public String getEmail() {
        return email;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
    
    public Group getGroup() {
        return group;
    }

    public void setSaltNumber(String salt) {
        this.saltNumber = salt;
    }
    
    public String getSaltNumber() {
        return saltNumber;
    }

    
    public void setValue(String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
    
    public void setAccess(int access) {
        this.access = access;
    }

    public int getAccess() {
        return access;
    }
    
    public void setConsultation(int consultation) {
        this.consultation = consultation;
    }

    public int getConsultation() {
        return consultation;
    }
}
