package View;

import Controller.SignIn;
import Controller.Register;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;


//Panel do login com senha - Segunda Etapa da autenticacao 
public class PasswordPanel extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private JButton okBtn;
    private JButton cleanBtn;
    private JButton[] phonemesBtns = new JButton[]{new JButton(), new JButton(), new JButton(), new JButton(), new JButton(), new JButton()};

    
    private int tries = 3;
    private JLabel passwordLbl;
    private JPasswordField passwordTxtField;

    private String password = "";
    private ArrayList<Object> passwordList = new ArrayList<>();

    public PasswordPanel(String email){
    	
        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.fill = GridBagConstraints.HORIZONTAL;

        okBtn = new JButton("OK");
        
        okBtn.addActionListener(new ActionListener() {
        	
            public void actionPerformed(ActionEvent e) {
                try {
                	
                    if (SignIn.isPasswordValid(email, passwordList)) {
                    	//Se a autenticacao foi valida - ao clicar em ok o usuario vai para o panel de verificacao da chave privada - terceira etapa
                        dispose();
                        PrivateKeyVerificationPanel privateKeyPanel = new PrivateKeyVerificationPanel(email);
                        privateKeyPanel.setTitle("Cofre Digital - Autenticação");
                        privateKeyPanel.setVisible(true);

                        Register.storeRegistry(3003,email,"");
                        TimeUnit.SECONDS.sleep(1);
                        Register.storeRegistry(3002,email,"");
                        TimeUnit.SECONDS.sleep(1);
                        Register.storeRegistry(4001,email,"");
                        
                    } else {
                    	//Senao, apos tres tentativas erradas - usuario bloqueado e o sistema retorna para a primeira etapa

                        tries = tries - 1;

                        JOptionPane.showMessageDialog(PasswordPanel.this,
                                "Erro! Senha inválida!, Resta(m)"  + tries + " tentativa(s)",
                                "Erro",
                                JOptionPane.ERROR_MESSAGE);

                        password = "";
                        passwordTxtField.setText(password);
                        passwordList.clear();
                        
                        if(tries == 2){
                            Register.storeRegistry(3004,email,"");
                        } else if(tries == 1){
                            Register.storeRegistry(3005,email,"");
                        } else if(tries == 0){
                        	
                            SignIn.isUserBlocked(email);
                            SignIn.waitFor2Minutes(email);
                            dispose();
                            
                            LoginNamePanel loginPanel = new LoginNamePanel();
                            loginPanel.setTitle("Cofre Digital - Autenticação");
                            loginPanel.setVisible(true);
                            
                            Register.storeRegistry(3006,email,"");
                            TimeUnit.SECONDS.sleep(1);
                            Register.storeRegistry(3007,email,"");
                            TimeUnit.SECONDS.sleep(1);
                            Register.storeRegistry(3002,email,"");
                            TimeUnit.SECONDS.sleep(1);
                            Register.storeRegistry(2001,email,"");
                        }
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                } catch (UnsupportedEncodingException ex) {
                    ex.printStackTrace();
                } catch (NoSuchAlgorithmException ex) {
                    ex.printStackTrace();
                } catch (IllegalAccessException illegalAccessException) {
                    illegalAccessException.printStackTrace();
                } catch (InstantiationException instantiationException) {
                    instantiationException.printStackTrace();
                } catch (ClassNotFoundException classNotFoundException) {
                    classNotFoundException.printStackTrace();
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            }
        });

        cleanBtn = new JButton("Limpar");
        cleanBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                password = "";
                passwordTxtField.setText(password);
                passwordList.clear();
            }
        });


        passwordLbl = new JLabel("Senha pessoal: ");
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        fieldsPanel.add(passwordLbl, constraints);

        passwordTxtField = new JPasswordField(20);
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        fieldsPanel.add(passwordTxtField, constraints);

        JPanel phonemesButtonsPanel = new JPanel();
        JPanel buttonsPanel = new JPanel();
        phonemeButtonsInit(passwordTxtField, phonemesButtonsPanel);
        buttonsPanel.add(okBtn);
        buttonsPanel.add(cleanBtn);
        
        getContentPane().add(fieldsPanel, BorderLayout.PAGE_START);
        getContentPane().add(phonemesButtonsPanel,BorderLayout.CENTER);
        getContentPane().add(buttonsPanel, BorderLayout.PAGE_END);

        pack();
        setResizable(false);
        setLocationRelativeTo(fieldsPanel);
    }
    
    
    //Adiciona os possiveis foenmas da tabela de fonemas no array 

    private void addPhonemes(ArrayList<String> phonemeArray) {
        phonemeArray.add("BA");
        phonemeArray.add("CA");
        phonemeArray.add("DA");
        phonemeArray.add("FA");
        phonemeArray.add("GA");
        phonemeArray.add("HA");
        phonemeArray.add("BE");
        phonemeArray.add("CE");
        phonemeArray.add("DE");
        phonemeArray.add("FE");
        phonemeArray.add("GE");
        phonemeArray.add("HE");
        phonemeArray.add("BO");
        phonemeArray.add("CO");
        phonemeArray.add("DO");
        phonemeArray.add("FO");
        phonemeArray.add("GO");
        phonemeArray.add("HO");
    }

    //Inicia os botoes com os fonemas iniciais
    private void phonemeButtonsInit(JPasswordField passwordField, JPanel panel) {
    
        Random random = new Random();

        ArrayList<String> usedPhonemes = new ArrayList<>();
        ArrayList<String> phonemes = new ArrayList<>();

        addPhonemes(phonemes);

        for (int i = 0; i < 6; i++) {

            int index1 = random.nextInt(phonemes.size());
            String phoneme1 = phonemes.get(index1);
            phonemes.remove(index1);

            int index2 = random.nextInt(phonemes.size());
            String phoneme2 = phonemes.get(index2);
            phonemes.remove(index2);
            
            int index3 = random.nextInt(phonemes.size());
            String phoneme3 = phonemes.get(index3);
            phonemes.remove(index3);

            usedPhonemes.add(phoneme1);
            usedPhonemes.add(phoneme2);
            usedPhonemes.add(phoneme3);

            phonemesBtns[i].setText(phoneme1 + " - " + phoneme2 + " - " + phoneme3);
            phonemesBtns[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    password += phoneme1;
                    passwordList.add(new String[]{((JButton) e.getSource()).getText().split(" - ")[0], ((JButton) e.getSource()).getText().split(" - ")[1], ((JButton) e.getSource()).getText().split(" - ")[2]});
                    passwordField.setText(password);
                    phonemeButtonsUpdate(passwordField);
                }
            });
            
            panel.add(phonemesBtns[i]);
        }
    }

  //Atualiza os botoes apos serem clicados com novos possivei fonemas
    private void phonemeButtonsUpdate(JPasswordField passwordField) {
    
        Random random = new Random();

        ArrayList<String> usedPhonemes = new ArrayList<>();
        ArrayList<String> phonemes = new ArrayList<>();

        addPhonemes(phonemes);

        for (int i = 0; i < 6; i++) {

            int index1 = random.nextInt(phonemes.size());
            String phoneme1 = phonemes.get(index1);
            phonemes.remove(index1);

            int index2 = random.nextInt(phonemes.size());
            String phoneme2 = phonemes.get(index2);
            phonemes.remove(index2);
            
            int index3 = random.nextInt(phonemes.size());
            String phoneme3 = phonemes.get(index3);
            phonemes.remove(index3);

            usedPhonemes.add(phoneme1);
            usedPhonemes.add(phoneme2);
            usedPhonemes.add(phoneme3);

            phonemesBtns[i].setText(phoneme1 + " - " + phoneme2 + " - " + phoneme3);
            phonemesBtns[i].removeActionListener(phonemesBtns[i].getActionListeners()[0]);
            phonemesBtns[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    password += phoneme1;
                    passwordList.add(new String[]{((JButton) e.getSource()).getText().split(" - ")[0], ((JButton) e.getSource()).getText().split(" - ")[1], ((JButton) e.getSource()).getText().split(" - ")[2]});
                    passwordField.setText(password);
                    phonemeButtonsUpdate(passwordField);
                }
            });

        }
    }
}
