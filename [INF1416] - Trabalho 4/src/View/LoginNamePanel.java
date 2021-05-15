package View;

import Controller.SignIn;
import Controller.Register;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.TimeUnit;


//Panel do login com email - Primeira Etapa da autenticacao 
public class LoginNamePanel extends JFrame {

	private static final long serialVersionUID = 1L;
	private JButton okBtn;
    private JButton cleanBtn;
    private JTextField emailTxtField;
    private JLabel loginNameLbl;


    public LoginNamePanel() {
    	
        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.fill = GridBagConstraints.HORIZONTAL;

        okBtn = new JButton("OK");
        
        okBtn.addActionListener(new ActionListener() {
        	
            public void actionPerformed(ActionEvent e) {
            	
                if(SignIn.isUserAuthenticated(emailTxtField.getText())){
                	// Se a autentificacao foi valida - ao clicar em ok vai para o panel da senha - segunda etapa de autenticacao
                    dispose();
                    PasswordPanel passwordPanel = new PasswordPanel(emailTxtField.getText());
                    passwordPanel.setTitle("Cofre Digital - Autenticação");
                    passwordPanel.setVisible(true);

                    Register.storeRegistry(2003,emailTxtField.getText(),"");
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                    Register.storeRegistry(2002,"","");
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException interruptedException) {
                    	
                        interruptedException.printStackTrace();
                    }
                    
                    Register.storeRegistry(3001,emailTxtField.getText(),"");

                } else if (SignIn.isBlockedMessage) {
                	
                	// Se nao foi valida - usuario bloqueado por 2 minutos
                    JOptionPane.showMessageDialog(LoginNamePanel.this,
                            "Erro! Usuário bloqueado por 2 minutos!",
                            "Erro",
                            JOptionPane.ERROR_MESSAGE);
                    
                    emailTxtField.setText("");
                    Register.storeRegistry(2004,emailTxtField.getText(),"");
                    
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                    Register.storeRegistry(2002,"","");
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                    Register.storeRegistry(2001,"","");
                    
                } else { 
                	
                    JOptionPane.showMessageDialog(LoginNamePanel.this,
                            "Erro! Usuário inválido!",
                            "Erro",
                            JOptionPane.ERROR_MESSAGE);
                    
                    emailTxtField.setText("");
                    Register.storeRegistry(2005,emailTxtField.getText(),"");
                }
            }
        });
        
        cleanBtn = new JButton("Limpar");
        cleanBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	emailTxtField.setText("");
            }
        });

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.add(okBtn);
        buttonsPanel.add(cleanBtn);
        
        loginNameLbl = new JLabel("Login name: ");
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        fieldsPanel.add(loginNameLbl, constraints);

        emailTxtField = new JTextField(20);
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        fieldsPanel.add(emailTxtField, constraints);

        getContentPane().add(fieldsPanel, BorderLayout.CENTER);
        getContentPane().add(buttonsPanel, BorderLayout.PAGE_END);

        pack();
        setResizable(false);
        setLocationRelativeTo(fieldsPanel);
    }
}
