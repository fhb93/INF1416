import java.security.*;

/**
 * INF1416 - Trabalho 2
 *  Dupla:
 * 		Felipe Holanda Bezerra - Matrícula:
 * 		Nathalia Mariz de Almeida Salgado Inácio - Matrícula:
*/

public class MySignatureTest {
	
	private KeyPairGenerator generatedKey;
	private KeyPair keyPair;
	private PrivateKey privateKey;
	private PublicKey publicKey;

	public static void main(String[] args) {
		
		System.out.println("Recebendo a string e o padrão de assinatura.");
		MySignatureTest test = new MySignatureTest();
		
		// Gerando o par de chaves RSA
		test.generateKeys(args[0], args[1]);
			
		
		//TODO:
		/**
		 * Receber a string e o padrao da assinatura na linha de comando
		 * Gerar o par de chaves RSA
		 * Instanciar e usar os métodos da classe MySignature para assinar o texto plano com chave privada
		 * Verificar a assinatura com a chave publica no padrão solicitado
		 * Imprimir, na saída padrão, todos os passos executados durante a geração e verificação da assinatura digital
		 * Imprimir, na saída padrão, o resumo de mensagem (digest) e a assinatura digital no formato hexadecimal
		*/
	}
	
	private boolean generateKeys(String str, String pattern) {
		System.out.println("Gerando as chaves...");
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

}
