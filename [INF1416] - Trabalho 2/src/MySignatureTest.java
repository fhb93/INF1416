import java.security.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * INF1416 - Trabalho 2
 *  Dupla:
 * 		Felipe Holanda Bezerra - Matrícula: 1810238
 * 		Nathalia Mariz de Almeida Salgado Inácio - Matrícula: 1520763
*/

public class MySignatureTest {
	
	public static void main(String[] args) {
		
		if(args.length != 2)
		{
			System.out.println("Uso: java MySignatureTest <\"plain Text\"> <Algorithm>");
			System.exit(1);
		}
		
		System.out.println("Recebendo a string e o padrão de assinatura.");
		
		if(args[1].contains("RSA") == false)
		{
			System.out.println("Apenas oferecemos suporte ao RSA!");
			System.exit(1);
		}
		
		//Para casos com a nomenclatura do BouncyCastle (sem o -)
		if(args[1].contains("SHA") == true && args[1].contains("-") == false)
		{
			args[1] = args[1].replace("SHA", "SHA-");
		}
		
		//Separa o nome com os padrões de assinatura 
		String split = args[1].split("with")[0];
		
		System.out.println("Metodo de assinatura: " + split);
		
		MySignature.getInstance().setChosenAlgorithm(split);
		
		MySignature.getInstance().setPlainText(args[0]);
		
		try {
			MySignature.getInstance().initSign();
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException e) {
			System.out.println("Uma " + e.getClass().getName() + " foi gerada! Abortando.");
			System.exit(1);
		}
		
		try {
			MySignature.getInstance().update();
		} catch (NoSuchAlgorithmException e) {
			System.out.println("Uma " + e.getClass().getName() + " foi gerada! Abortando.");
			System.exit(1);
		}
		
		try {
			
			MySignature.getInstance().sign();

			
		} catch (IllegalBlockSizeException | BadPaddingException e1) {
			System.out.println("Uma " + e1.getClass().getName() + " foi gerada! Abortando.");
			System.exit(1);
		}
		
		
		
		try {
			MySignature.getInstance().initVerify();
			
		}catch(InvalidKeyException e2)
		{
			System.out.println("Uma " + e2.getClass().getName() + " foi gerada! Abortando.");
			System.exit(1);
		}
		
		try {
			MySignature.getInstance().verify();
			
		} catch (IllegalBlockSizeException | BadPaddingException e3) {
			System.out.println("Uma " + e3.getClass().getName() + " foi gerada! Abortando.");
			System.exit(1);
		}
			
	}
	
}
