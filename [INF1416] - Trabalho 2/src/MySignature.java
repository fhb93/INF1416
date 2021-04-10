import java.security.*;
import java.util.Arrays;

import javax.crypto.*;

/**
 * INF1416 - Trabalho 2
 *  Dupla:
 * 		Felipe Holanda Bezerra - Matrícula:
 * 		Nathalia Mariz de Almeida Salgado Inácio - Matrícula:
*/


public class MySignature {
	
	private Cipher cipherPattern; 
	private MessageDigest messageDig;
	private static MySignature instance = new MySignature();
	
	
	//Métodos da classe MySignature - getInstance, initSign, update, sign, initVerify e verify
	
	public static MySignature getInstance() {
		return instance;
	}
	
	//Método getInstance() -> Instancia a classe MySignature e gera as chaves RSA
	private MySignature() {
		try {
			this.cipherPattern = Cipher.getInstance("RSA");
			this.messageDig = MessageDigest.getInstance("MD5");	
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e ){
			
		} 
	}
	
	//Método initSign() -> Inicializa a assinatura digital
	public void initSign(PrivateKey key) throws InvalidKeyException {		
		this.cipherPattern.init(Cipher.ENCRYPT_MODE, key);
	}
	  
	//Método update() -> Pega o resumo de mensagem 
	public void update(byte[] data) {
	    this.messageDig.update(data);
	}
	
	//Método sign() -> Assina com chave privada
	public byte[] sign() throws IllegalBlockSizeException, BadPaddingException {
		return this.cipherPattern.doFinal(this.messageDig.digest());
	}
	  
	//Método initVerify() -> Inicializa a verificação da assinatura com a chave pública
	public void initVerify(PublicKey key) throws InvalidKeyException {
		this.cipherPattern.init(Cipher.DECRYPT_MODE, key);
	}
	
	//Método verify() -> Checa se a assinatura foi validada
	public boolean verify(byte[] signature) throws IllegalBlockSizeException, BadPaddingException {
		return Arrays.equals(this.messageDig.digest(), cipherPattern.doFinal(signature));
	}
	  
	
	//Método auxiliar - Formata a assinatura digital para hexadecimal e a imprime
	public static void hexFormat(byte[] bytes) {
		  
		StringBuffer digitalSignature = new StringBuffer();
	    for(int i = 0; i < bytes.length; i++) {
	    	String hex = Integer.toHexString(0x0100 + (bytes[i] & 0x00FF)).substring(1);
	    	digitalSignature.append((hex.length() < 2 ? "0" : "") + hex);
	    }
	
	    System.out.println( digitalSignature.toString() );
	}
}
