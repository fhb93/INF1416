import java.security.*;
import java.util.Arrays;

import javax.crypto.*;

/**
 * INF1416 - Trabalho 2
 *  Dupla G4:
 * 		Felipe Holanda Bezerra - Matrícula: 1810238
 * 		Nathalia Mariz de Almeida Salgado Inácio - Matrícula: 1520763
 */


public class MySignature {

	private Cipher cipherPattern; 
	private MessageDigest messageDig;
	private byte[] plainText;
	private byte[] cipherText;
	private String chosenAlgorithm;
	private String[] algorithmsSupported = {"MD5", "SHA-1", "SHA-256", "SHA-512"};


	private static MySignature instance = new MySignature();


	private KeyPairGenerator generatedKey;
	private KeyPair keyPair;
	private PrivateKey privateKey;
	private PublicKey publicKey;


	//Métodos da classe MySignature - getInstance, initSign, update, sign, initVerify e verify

	public static MySignature getInstance() {
		return instance;
	}

	//Método getInstance() -> Instancia a classe MySignature e gera as chaves RSA
	private MySignature() {
		try {
			cipherPattern = Cipher.getInstance("RSA");

		} catch (NoSuchAlgorithmException | NoSuchPaddingException e ){

		} 
	}

	//Método initSign() -> Inicializa a assinatura digital
	public void initSign() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {		
		System.out.println("Iniciando a geração do par de chaves");

		messageDig = MessageDigest.getInstance(chosenAlgorithm);	

		if(generateKeys()) {
			cipherPattern = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipherPattern.init(Cipher.ENCRYPT_MODE, privateKey);
			System.out.println("Geração do par de chaves concluída");
		}

	}

	//Método update() -> Pega o digest de mensagem através do algoritmo escolhido e o imprime em hexadecimal
	public void update() throws NoSuchAlgorithmException {
		System.out.println("Calculando o digest da mensagem\n");
		this.checkChosenAlgorithm();
		this.messageDig.update(plainText);
		System.out.println("Digest da mensagem em hexadecimal: " + hexFormat(this.messageDig.digest()));
	}

	//Método sign() -> Assina com chave privada
	public void sign() throws IllegalBlockSizeException, BadPaddingException {
		System.out.println("Assinando o resumo da mensagem...");
		cipherText = cipherPattern.doFinal(this.messageDig.digest());
		System.out.println("Assinatura do resumo concluída.");
		System.out.println("Assinatura digital em Hexadecimal: \n" + hexFormat(cipherText));
	}

	//Método initVerify() -> Inicializa a verificação da assinatura com a chave pública
	public void initVerify() throws InvalidKeyException {
		System.out.println("Iniciando a verificação do texto cifrado...");

		cipherPattern.init(Cipher.DECRYPT_MODE, publicKey);
	}

	//Método verify() -> Verifica a assinatura digital
	public void verify() throws IllegalBlockSizeException, BadPaddingException {
		System.out.println("Começando verificação da assinatura digital...");
		System.out.println("teste: "  + chosenAlgorithm);
		if (Arrays.equals(this.messageDig.digest(), this.cipherPattern.doFinal(cipherText))) {
			System.out.println("Assinatura digital verificada");
		} else {
			System.out.println("Assinatura digital não verificada");
		}
	}



	//Método auxiliar - Formata para hexadecimal

	public String hexFormat(byte[] bytes) {

		StringBuffer stringBuffer = new StringBuffer();
		for(int i = 0; i < bytes.length; i++) {
			String hex = Integer.toHexString(0x0100 + (bytes[i] & 0x00FF)).substring(1);
			stringBuffer.append((hex.length() < 2 ? "0" : "") + hex);
		}

		return stringBuffer.toString();
	}

	public boolean generateKeys() {

		System.out.println("Gerando as chaves RSA...");

		try {
			generatedKey = KeyPairGenerator.getInstance("RSA");
			generatedKey.initialize(1024);
			keyPair = generatedKey.generateKeyPair();
			publicKey = keyPair.getPublic();
			privateKey = keyPair.getPrivate();
			System.out.println("Par de chaves assimetricas gerado:");
			System.out.println("Chave publica:" + publicKey);
			System.out.println("Chave privada:" + privateKey);

			return true;
		} catch (NoSuchAlgorithmException e) {
			return false;
		}

	}


	//Método auxiliar - Checa se o algoritmo escolhido é um dos algoritmos suportados
	private void checkChosenAlgorithm() throws NoSuchAlgorithmException  {
		
		for(int i =0; i < this.algorithmsSupported.length; i++) {
			if (chosenAlgorithm.equals(this.algorithmsSupported[i])) {
				System.out.println("Algoritmo escolhido é suportado!");
				setAlgorithmToMessageDigest(this.algorithmsSupported[i]);
				return;
			}
			
		}

		System.out.println("Algoritmo escolhido não é suportado!");
	}

	//Método auxiliar - Pega o digest da mensagem por um dos algoritmos suportados
	private void setAlgorithmToMessageDigest(String algorithm) throws NoSuchAlgorithmException {
		this.messageDig = MessageDigest.getInstance(algorithm);
	}



	public String getChosenAlgorithm() {
		return chosenAlgorithm;
	}

	public void setChosenAlgorithm(String chosenAlgorithm) {
		this.chosenAlgorithm = chosenAlgorithm;
	}

	public byte[] getPlainText() {
		return plainText;
	}

	public void setPlainText(String plainText) {
		this.plainText = plainText.getBytes();
	}


}
