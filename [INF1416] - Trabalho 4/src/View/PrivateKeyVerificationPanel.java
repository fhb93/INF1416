package View;

import Controller.SignIn;
import Controller.Register;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;


//Panel da verificacao da chave privada - Terceira Etapa da autenticacao 
public class PrivateKeyVerificationPanel extends JFrame {
	
	private static final long serialVersionUID = 1L;
	public static PrivateKey privateKey;
    public static PublicKey publicKey;
    
    private JButton fileBtn;
    private JButton okBtn;
    private JButton cleanBtn;

    private JLabel privateKeyLbl;
    private JLabel secretWordLbl;
    private JPasswordField secretWordTxtField;
    
    private JFileChooser fileChooser = new JFileChooser();
    private String filePath;
    
    private int tries = 3;

    public PrivateKeyVerificationPanel(String email) {
    	
        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.fill = GridBagConstraints.HORIZONTAL;

        okBtn = new JButton("OK");
        okBtn.addActionListener(new ActionListener() {
            @SuppressWarnings("deprecation")
			@Override
            public void actionPerformed(ActionEvent e) {
            	
                if(filePath == null || !fileChooser.getSelectedFile().getName().contains(".key")){
                    Register.storeRegistry(4004,email,"");
                    //Erro no caminho do arquivo escolhido para a chave privada 

                    tries = tries - 1;
                    
                    JOptionPane.showMessageDialog(PrivateKeyVerificationPanel.this,
                            "Erro! Caminho do arquivo inválido, Resta(m)" + tries + " tentativa(s).",
                            "Erro",
                            JOptionPane.ERROR_MESSAGE);

                    secretWordTxtField.setText("");

                    if(tries == 0){
                    	
                        SignIn.isUserBlocked(email);
                        SignIn.waitFor2Minutes(email);
                        dispose();
                        
                        //Após 3 tentativas - usuario bloqueado e sistema retorna para primeira etapa de autenticacao

                        LoginNamePanel loginPanel = new LoginNamePanel();
                        loginPanel.setTitle("Cofre Digital - Autenticação");
                        loginPanel.setVisible(true);
                        Register.storeRegistry(4007,email,"");
                        
                        try {
                            TimeUnit.SECONDS.sleep(1);
                        } catch (InterruptedException interruptedException) {
                            interruptedException.printStackTrace();
                        }
                        Register.storeRegistry(4002,email,"");
                        try {
                            TimeUnit.SECONDS.sleep(1);
                        } catch (InterruptedException interruptedException) {
                            interruptedException.printStackTrace();
                        }
                        Register.storeRegistry(2001,email,"");
                    }
                    
                    return;
                }
                try {
                    try{
                        privateKey = SignIn.generatePrivateKey(secretWordTxtField.getText(), filePath, email);
                    } catch (Exception ex){
                    	
                        //Erro na geracao da chave privada atraves di aquivo

                        tries = tries - 1;
                        
                        JOptionPane.showMessageDialog(PrivateKeyVerificationPanel.this,
                                "Erro! Falha na autenticação! Resta(m) " + tries + " tentativa(s)",
                                "Erro",
                                JOptionPane.ERROR_MESSAGE);
                        secretWordTxtField.setText("");

                        if(tries == 0){
                            SignIn.isUserBlocked(email);
                            SignIn.waitFor2Minutes(email);
                            dispose();
                            
                            //Após 3 tentativas - usuario bloqueado e sistema retorna para primeira etapa de autenticacao

                            LoginNamePanel loginFrame = new LoginNamePanel();
                            loginFrame.setTitle("Cofre Digital - Autenticação");
                            loginFrame.setVisible(true);
                            Register.storeRegistry(4007,email,"");
                            
                            try {
                                TimeUnit.SECONDS.sleep(1);
                            } catch (InterruptedException interruptedException) {
                                interruptedException.printStackTrace();
                            }
                            Register.storeRegistry(4002,email,"");
                            try {
                                TimeUnit.SECONDS.sleep(1);
                            } catch (InterruptedException interruptedException) {
                                interruptedException.printStackTrace();
                            }
                            Register.storeRegistry(2001,email,"");
                        }
                        
                        return;
                    }

                    if(secretWordTxtField.getText().equals("")){
                    	
                        //Erro - frase secreta vazia
                    	
                        Register.storeRegistry(4005,email,"");
                        tries = tries - 1;
                        JOptionPane.showMessageDialog(PrivateKeyVerificationPanel.this,
                                "Erro! Digite algo na frase secreta! Resta(m)" + tries + " tentativa(s).",
                                "Erro",
                                JOptionPane.ERROR_MESSAGE);
                        secretWordTxtField.setText("");

                        if(tries == 0){
                        	
                            //Após 3 tentativas - usuario bloqueado e sistema retorna para primeira etapa de autenticacao

                            SignIn.isUserBlocked(email);
                            SignIn.waitFor2Minutes(email);
                            dispose();
                            
                            LoginNamePanel loginPanel = new LoginNamePanel();
                            loginPanel.setTitle("Cofre Digital - Autenticação");
                            loginPanel.setVisible(true);
                            Register.storeRegistry(4007,email,"");
                            
                            try {
                                TimeUnit.SECONDS.sleep(1);
                            } catch (InterruptedException interruptedException) {
                                interruptedException.printStackTrace();
                            }
                            Register.storeRegistry(4002,email,"");
                            try {
                                TimeUnit.SECONDS.sleep(1);
                            } catch (InterruptedException interruptedException) {
                                interruptedException.printStackTrace();
                            }
                            Register.storeRegistry(2001,email,"");
                        }
                        
                        return;
                    }

                    publicKey = SignIn.generateCertificate(email);

                    byte[] currentByte = new byte[2048];
                    new SecureRandom().nextBytes(currentByte);

                    Signature signature = Signature.getInstance("SHA1WithRSA");
                    signature.initSign(privateKey);
                    signature.update(currentByte);
                    
                    byte[] signatureByte = signature.sign();
                    Register.storeRegistry(4003,email,"");

                    signature.initVerify(publicKey);
                    signature.update(currentByte);
                    try {
                        if (signature.verify(signatureByte)) {
                        	
                            //Autenticacao valida - Ir para o menu principal

                            dispose();
                            Register.storeRegistry(4002,email,"");                            
                            Register.storeRegistry(5001,email,"");
                            
                            //TODO: Ir para menu

                        } else {
                            tries = tries - 1;
                            JOptionPane.showMessageDialog(PrivateKeyVerificationPanel.this,
                                    "Erro! Falha na autenticação! Resta(m) " + tries + " tentativa(s).",
                                    "Erro",
                                    JOptionPane.ERROR_MESSAGE);
                            secretWordTxtField.setText("");

                            if (tries == 0) {
                            	
                                //Após 3 tentativas - usuario bloqueado e sistema retorna para primeira etapa de autenticacao

                                SignIn.isUserBlocked(email);
                                SignIn.waitFor2Minutes(email);
                                dispose();
                                
                                LoginNamePanel loginPanel = new LoginNamePanel();
                                loginPanel.setTitle("Cofre Digital - Autenticação");
                                loginPanel.setVisible(true);
                                Register.storeRegistry(4007,email,"");
                                
                                try {
                                    TimeUnit.SECONDS.sleep(1);
                                } catch (InterruptedException interruptedException) {
                                    interruptedException.printStackTrace();
                                }
                                
                                Register.storeRegistry(4002,email,"");
                                try {
                                    TimeUnit.SECONDS.sleep(1);
                                } catch (InterruptedException interruptedException) {
                                    interruptedException.printStackTrace();
                                }
                                Register.storeRegistry(2001,email,"");
                            }
                            
                            return;
                        }
                    } catch (SignatureException se) {
                    	
                        //Falha na autenticacao

                        Register.storeRegistry(4006,email,"");
                        
                        tries = tries - 1;
                        JOptionPane.showMessageDialog(PrivateKeyVerificationPanel.this,
                                "Erro! Falha na autenticação! Resta(m) " + tries + " tentativa(s).",
                                "Erro",
                                JOptionPane.ERROR_MESSAGE);

                        secretWordTxtField.setText("");

                        if (tries == 0) {
                        	
                            //Após 3 tentativas - usuario bloqueado e sistema retorna para primeira etapa de autenticacao

                            SignIn.isUserBlocked(email);
                            SignIn.waitFor2Minutes(email);
                            dispose();
                            
                            LoginNamePanel loginFrame = new LoginNamePanel();
                            loginFrame.setTitle("Cofre Digital - Autenticação");
                            loginFrame.setVisible(true);
                        }
                        
                        return;
                    }
                }
                catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                    noSuchAlgorithmException.printStackTrace();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                } catch (InvalidKeyException invalidKeyException) {
                    invalidKeyException.printStackTrace();
                } catch (CertificateException certificateException) {
                    certificateException.printStackTrace();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (SignatureException signatureException) {
                    signatureException.printStackTrace();
                } catch (IllegalAccessException illegalAccessException) {
                    illegalAccessException.printStackTrace();
                } catch (InstantiationException instantiationException) {
                    instantiationException.printStackTrace();
                } catch (ClassNotFoundException classNotFoundException) {
                    classNotFoundException.printStackTrace();
                }
            }
        });
        
        
        fileBtn = new JButton("Arquivo");
        fileBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fileChooser.showOpenDialog(null);
                File f = fileChooser.getSelectedFile();
                filePath = f.getAbsolutePath();
                fileBtn.setText(fileChooser.getSelectedFile().getName());
            }
        });
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        fieldsPanel.add(fileBtn, constraints);
        
        
        cleanBtn = new JButton("Limpar");
        cleanBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                secretWordTxtField.setText("");
            }
        });        
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        fieldsPanel.add(cleanBtn, constraints);
        
        
        privateKeyLbl = new JLabel("Chave privada: ");
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        fieldsPanel.add(privateKeyLbl, constraints);


        secretWordLbl = new JLabel("Frase secreta: ");
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        fieldsPanel.add(secretWordLbl, constraints);

        secretWordTxtField = new JPasswordField(20);
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        fieldsPanel.add(secretWordTxtField, constraints);


        JPanel buttonsPanel = new JPanel();
        buttonsPanel.add(okBtn);
        buttonsPanel.add(cleanBtn);

        getContentPane().add(fieldsPanel, BorderLayout.CENTER);
        getContentPane().add(buttonsPanel, BorderLayout.PAGE_END);

        pack();
        setResizable(false);
        setLocationRelativeTo(fieldsPanel);
    }

}
