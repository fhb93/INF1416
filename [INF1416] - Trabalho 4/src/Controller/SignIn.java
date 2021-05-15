package Controller;

import Model.MySQL;
import Model.Group;
import Model.User;
import View.PrivateKeyVerificationPanel;

import javax.crypto.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.TimeUnit;



//Classe SignIn - gerencia toda a etapa de autenticacao

public class SignIn {

	
    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
    public static boolean isBlockedMessage;
    public static int size;
    
    
    //Seta um usuário com nome, email e grupo
    public static User setUpUser(String email) {
    	
        User currentUser = new User();
        
        try {
        	
            MySQL.createConnection();
            MySQL.createStatement();
            
            ResultSet result = MySQL.executeQuery(String.format("select * from usuarios where email='%s'", email));
            
            if (result != null && result.next()) {
            	
                currentUser.setEmail(email);
                currentUser.setName(result.getString(2));
                currentUser.setGroup(Group.getGroup(result.getInt(3)));
                MySQL.closeConnection();
                isAccessUpdated(currentUser);
                isConsultationsUpdated(currentUser);
                return currentUser;
                
            }
            
            return null;
            
        } catch (Exception e) {
            return null;
        }
    }
    
    //Verifica se os acessos do usuários estao atualizados
    public static boolean isAccessUpdated(User user){
        try {
        	
            MySQL.createConnection();
            MySQL.createStatement();
            
            ResultSet result = MySQL.executeQuery(String.format("select acesso from usuarios where email='%s'", user.getEmail()));
            
            if (result != null && result.next()) {
                user.setAccess(result.getInt(1)+1);
                MySQL.executeUpdate(String.format("UPDATE usuarios SET acesso = '%d' WHERE email = '%s'", user.getAccess(),user.getEmail()));
                MySQL.closeConnection();
                return true;
            }
            
            return false;
            
        } catch (Exception e) {
            return false;
        }
    }

    //Verifica se as consultas do usuário estao atualizadas
    public static boolean isConsultationsUpdated(User user){
        try {
        	
            MySQL.createConnection();
            MySQL.createStatement();
            
            ResultSet result = MySQL.executeQuery(String.format("select consulta from usuarios where email='%s'", user.getEmail()));
            
            if (result != null && result.next()) {
                user.setConsultation(result.getInt(1)+1);
                MySQL.executeUpdate(String.format("UPDATE usuarios SET consulta = '%d' WHERE email = '%s'", user.getConsultation(),user.getEmail()));
                MySQL.closeConnection();
                return true;
            }
            
            return false;
            
        } catch (Exception e) {
            return false;
        }
    }

    //Verifica se o usuário está autenticado
    public static boolean isUserAuthenticated(String email) {
        
    	try {
    		
            MySQL.createConnection();
            MySQL.createStatement();
            
            ResultSet result = MySQL.executeQuery(String.format("select email,block from usuarios where email = '%s'", email));
            
            if (result != null && result.next()) {
            	
                String signIn = result.getString(1);
                boolean isBlocked = result.getBoolean(2);
                
                if (email.equalsIgnoreCase(signIn) && !isBlocked) {
                    MySQL.closeConnection();
                    return true;
                    
                } else if (isBlocked == true) {
                    isBlockedMessage = true;
                    return false;
                }
                
                return false;
            }
            
            return false;
            
        } catch (Exception e) {
            return false;
        }
    }

    //Verifica se o usuário está bloqueado
    public static boolean isUserBlocked(String email){
        try {
        	
            MySQL.createConnection();
            MySQL.createStatement();
            
                ResultSet result = MySQL.executeQuery(String.format("select block from usuarios where email='%s'", email));
                
                if (result != null && result.next()) {
                    MySQL.executeUpdate(String.format("UPDATE usuarios SET block = '%d' WHERE email = '%s'", 1,email));
                    MySQL.closeConnection();
                    return true;
                }
                
                return false;
                
        } catch (Exception e) {
            return false;
        }
    }

    //Verifica se a senha é valida
    public static boolean isPasswordValid(String email, ArrayList<Object> password) throws SQLException, UnsupportedEncodingException, NoSuchAlgorithmException, IllegalAccessException, InstantiationException, ClassNotFoundException {
       
    	MySQL.createConnection();
        MySQL.createStatement();
        
        ResultSet result = MySQL.executeQuery(String.format("select valor_armazenado, salt from usuarios where email='%s'", email));
        
        if (result != null && result.next()) {
        	
            String value = result.getString(1);
            String salt = result.getString(2);
            
            int length = password.size();
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    for (int k = 0; k < 3; k++) {
                        for (int l = 0; l < 3; l++) { // Senha com no minimo 4 fonemas 
                                	
                                    String currentPassword;
                                    String currentValue;
                                    
                                    if (length > 9) {
                                        if (length == 12) {  // Senha possui 6 fonemas
                                        	 for (int m = 0; m < 3; m++) {
                                        		 for (int n = 0; n < 3; n++) {
	                                                currentPassword = ((String[]) password.get(0))[i] + ((String[]) password.get(1))[j] + ((String[]) password.get(2))[k] +
	                                                        ((String[]) password.get(3))[l] + ((String[]) password.get(6))[m] + ((String[]) password.get(7))[n];
	                                                currentValue = getDigestValue(currentPassword, salt);
	                                                if (currentValue.equals(value)) {
	                                                    return true;
	                                                }
                                              }
                                        	}
                                        } else { // Senha possui 5 fonemas
                                            for (int o = 0; o < 3; o++) {
                                                    currentPassword = ((String[]) password.get(0))[i] + ((String[]) password.get(1))[j] + ((String[]) password.get(2))[k] +
                                                            ((String[]) password.get(3))[l] + ((String[]) password.get(6))[o];
                                                    currentValue = getDigestValue(currentPassword, salt);
                                                    if (currentValue.equals(value)) {
                                                        return true;
                                                    }
                                                }
                                        	}
                                    } else { //4 fonemas
                                        currentPassword = ((String[]) password.get(0))[i] + ((String[]) password.get(1))[j] + ((String[]) password.get(2))[k] +
                                                ((String[]) password.get(3))[l];
                                        currentValue = getDigestValue(currentPassword, salt);
                                        if (currentValue.equals(value)) {
                                            return true;
                                        }
                                    }
                                }
                            }
                        }
            		}
            
            return false;
        }
        return false;
    }

    //Verifica a senha está sendo criada corretamenta no momento do cadastro
    public static boolean isSignUpPasswordValid(String password) {
    	
        if(password.length() >= 8 && password.length() <= 12){ //entre 4 e 6 fonemas
        	
            String[] array = password.split("");
            String firstDigit = null;
            boolean isEqual = true;

            for (int i = 0; i < array.length; i++) {
                for (int j = i + 1 ; j < array.length; j++) {
                    if (array[i].equals(array[j])) {
                        return false;
                    }
                }
            }

            for (int i = 0 ; i < array.length; i++) {
                if (i == 0){
                    firstDigit = array[i];
                }
                if (!array[i].equals(firstDigit)){
                    isEqual = false;
                }
            }
            
            if (isEqual) { //Se nao repetir fonema, pode criar
                return false;
            } else {
            	return true;
            }
        }
        
        return false;
    }


    //Pega a contagem do usuario no SQL
    public static boolean getUsersCount(){
        
    	try {
        	
            MySQL.createConnection();
            MySQL.createStatement();
            
            ResultSet result = MySQL.executeQuery(String.format("select count(*) from usuarios"));
            
            if (result != null && result.next()) {
                size = result.getInt(1);
                MySQL.closeConnection();
                return true;
            }
            
            return false;
            
        } catch (Exception e) {
            return false;
        }
    }

    //Retorna o digest em hexadecimal
    public static String getDigestValue(String password, String salt) throws NoSuchAlgorithmException, UnsupportedEncodingException {
    	
        password = password + salt;
        
        try {
        	
            MessageDigest sha1 = MessageDigest.getInstance("SHA1");
            String hexDigest = formatToHex(sha1.digest(password.getBytes("UTF-8")));
            return hexDigest;
            
        } catch (NoSuchAlgorithmException ex) {
            throw ex;
        } catch (UnsupportedEncodingException ex) {
            throw ex;
        }
    }

    //Gera o Salt através dos caracteres - pegando 10 aleatoriamente
    public static String generateSaltNumber() {
    	
        StringBuilder salt = new StringBuilder();
        Random random = new Random();
    	
        String saltChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        while (salt.length() < 10) {
            int index = (int) (random.nextFloat() * saltChars.length());
            salt.append(saltChars.charAt(index));
        }
        
        String genaratedSalt = salt.toString();
        return genaratedSalt;
    }

    
    // Gera a chave privada (BASE64)
    public static PrivateKey generatePrivateKey(String secretWord, String filePath, String email) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IOException, BadPaddingException, IllegalBlockSizeException, InvalidKeySpecException {

        // SHA1-PRNG
        SecureRandom prngSecurity = SecureRandom.getInstance("SHA1PRNG");
        byte[] secretWordBytes = secretWord.getBytes();
        prngSecurity.setSeed(secretWordBytes);


        // Chave de decriptação
        KeyGenerator keyGenerator = KeyGenerator.getInstance("DES");
        keyGenerator.init(prngSecurity);
        Key key = keyGenerator.generateKey();

        // Descriptando arquivo
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key);
        
        Path keyPath = Paths.get(filePath);
        byte[] plainText = null;
        
        try{
            plainText = cipher.doFinal(Files.readAllBytes(keyPath));
        } catch (Exception e){
            Register.storeRegistry(4005,email,"");
        }

        //Base 64
        String userKey64 = new String(plainText, "UTF8");

        String[] userKey64Array = Arrays.copyOfRange(userKey64.split("\n"),1,userKey64.split("\n").length -1);
        String privateKey64 = Arrays.toString(userKey64Array);
        
        privateKey64 = privateKey64.substring(1,privateKey64.length() - 1);
        byte[] userKey64Encoded = Base64.getMimeDecoder().decode(privateKey64);

        PKCS8EncodedKeySpec encoded = new PKCS8EncodedKeySpec(userKey64Encoded);
        KeyFactory factory = KeyFactory.getInstance("RSA");
        return factory.generatePrivate(encoded);
    }

    //Gera a chave publica do certificado
    public static PublicKey generateCertificate(String email) throws SQLException, CertificateException, IllegalAccessException, InstantiationException, ClassNotFoundException, FileNotFoundException {

        String certicateText = "";
        boolean certificate64 = false;

        ResultSet result;
        MySQL.createConnection();
        MySQL.createStatement();
        result = MySQL.executeQuery(String.format("select certificado from usuarios where email='%s'", email));
        
        if (result != null && result.next()) {
        	
            String resultCertificate = result.getString(1);
            String[] certificateArray = resultCertificate.split("\n");
            for(String splitedLine : certificateArray) {
            	
                if (splitedLine.equals("-----BEGIN CERTIFICATE-----")) {
                    certificate64 = true;
                    certicateText+=splitedLine+"\n";
                    continue;
                }
                
                if(certificate64 == true){
                    certicateText+=splitedLine+"\n";
                    continue;
                }
            }
            
            MySQL.closeConnection();
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            ByteArrayInputStream bytes = new ByteArrayInputStream(certicateText.getBytes());
            X509Certificate certificate = (X509Certificate)certificateFactory.generateCertificate(bytes);
            return certificate.getPublicKey();
        }
        
        return null;
    }

    //Decripta uma lista de arquivos
    public static Object filesListDecriptation(ArrayList<File> filesList, User user, boolean isIndexed) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException, BadPaddingException, IllegalBlockSizeException, SignatureException {
        
    	File file = null; File encryptedFile = null; File signatureFile = null;
    	
        for(File currentFile : filesList) {
            if (currentFile.getName().contains("enc")){
                encryptedFile = currentFile;
            } else if(currentFile.getName().contains("env")) {
                file = currentFile;
            } else {
                signatureFile = currentFile;
            }
        }
        
        int index = file.getName().indexOf(".");
        
        //Descriptando arquivo .env
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, PrivateKeyVerificationPanel.privateKey);
        Path filePath = Paths.get(file.getPath());
        byte[] bytes;
        
        try {
            bytes = cipher.doFinal(Files.readAllBytes(filePath));
            Register.storeRegistry(8011,user.getEmail(),file.getName().substring(0,index));
        } catch (Exception e){ //Usuário não tem permissão de acesso
            Register.storeRegistry(8012,user.getEmail(),file.getName().substring(0,index));
            return null;
        }
        
        //Gerando semente
        String seed = new String(bytes, "UTF8");

        //SHA1-PRNG
        SecureRandom prngSecurity = SecureRandom.getInstance("SHA1PRNG");

        prngSecurity.setSeed(seed.getBytes());

        //Chave simétrica para decriptar o arquivo .enc
        KeyGenerator keyGen = KeyGenerator.getInstance("DES");
        keyGen.init(prngSecurity);
        Key key = keyGen.generateKey();

        //Decriptando arquivo .enc
        cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key);
        
        Path encryptedFilePath = Paths.get(encryptedFile.getPath());
        byte[] encryptedBytes = cipher.doFinal(Files.readAllBytes(encryptedFilePath));
        
        //Texto plano do .enc
        String plainText = new String(encryptedBytes, "UTF8");
        Register.storeRegistry(8005,user.getEmail(),file.getName().substring(0,index));
        
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        
        //Validando o arquivo .enc com a assinatura digital
        Signature signature = Signature.getInstance("SHA1WithRSA");
        Path signatureFilePath = Paths.get(signatureFile.getPath());
        byte[] signatureBytes = Files.readAllBytes(signatureFilePath);
        
        signature.initVerify(PrivateKeyVerificationPanel.publicKey);
        signature.update(encryptedBytes);
        
        if (signature.verify(signatureBytes)) {
            if(isIndexed == true) {
                Register.storeRegistry(8006,user.getEmail(),encryptedFile.getName().substring(0,index));
                return plainText.split("\n");
            } else {
                Register.storeRegistry(8014,user.getEmail(),encryptedFile.getName().substring(0,index));
                return encryptedBytes;
            }
        } else {
            if(isIndexed == true) {
                Register.storeRegistry(8008,user.getEmail(),encryptedFile.getName().substring(0,index));
                return new byte[1];
            } else {
                Register.storeRegistry(8016,user.getEmail(),encryptedFile.getName().substring(0,index));
                return new byte[1];
            }
        }
    }
    
    //Funcoes auxiliares
    
    //Seta um timer de 2 minutos para desbloqueio do usuario
    public static void waitFor2Minutes(String email) {
    	
        new java.util.Timer().schedule(
        		
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        try {
                        	
                            MySQL.createConnection();
                            MySQL.createStatement();
                            
                            ResultSet result = MySQL.executeQuery(String.format("select block from usuarios where email='%s'", email));
                            
                            if (result != null && result.next()) {
                                MySQL.executeUpdate(String.format("UPDATE usuarios SET block = '%d' WHERE email = '%s'", 0,email));
                                MySQL.closeConnection();
                            }
                            
                        } catch (Exception e) {}
                    }
                }, 120000);
    }
    
    //Formata para hexadecimal
    public static String formatToHex(byte[] bytes) {
    	
        char[] hexChars = new char[bytes.length * 2];
        int index;
        
        for (int j = 0; j < bytes.length; j++) {
            index = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[index >>> 4];
            hexChars[j * 2 + 1] = hexArray[index & 0x0F];
        }
        
        return new String(hexChars);
    }

}
