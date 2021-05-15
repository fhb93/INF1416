package Main;

import Controller.SignIn;
import Controller.Register;
import View.LoginNamePanel;
import View.PrivateKeyVerificationPanel;
import View.PasswordPanel;


import javax.swing.JFrame;
import java.util.concurrent.TimeUnit;


//Classe Main - Começa pelo Login por email
public class Main extends JFrame {
	
	private static final long serialVersionUID = 1L;
	public static boolean start = false;
    
    public static void main(String args[]) throws InterruptedException {

        if(start == false){
            Register.storeRegistry(1001,"","");
            start = true;
        }

        LoginNamePanel userPanel = new LoginNamePanel();
        userPanel.setTitle("Cofre Digital - Autenticação");
        userPanel.setVisible(true);
        
//        PasswordPanel passwordPanel = new PasswordPanel("BOCABEHO");
//        passwordPanel.setTitle("Cofre Digital - Autenticação");
//        passwordPanel.setVisible(true);
        

//        PrivateKeyVerificationPanel verificationPanel = new PrivateKeyVerificationPanel("nathi.mariz.inacio@gmail.com");
//        verificationPanel.setTitle("Cofre Digital - Autenticação");
//        verificationPanel.setVisible(true);

        TimeUnit.SECONDS.sleep(1);
        Register.storeRegistry(2001,"","");
    }
}
