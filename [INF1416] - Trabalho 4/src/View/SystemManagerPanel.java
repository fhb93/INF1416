package View;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import Model.Group;
import Model.MySQL;
import Model.User;

public class SystemManagerPanel {

	public List<String> header;

	private static SystemManagerPanel mainPanel;

	public SystemManagerPanel(User user)
	{
		User thisUser = user;
		header = new ArrayList<String>();

		header.add("Login: " + thisUser.getEmail());
		header.add("Grupo: " + thisUser.getGroup().toString());
		header.add("Nome: " + thisUser.getName());
	}

	public void showHeaderPanel()
	{
		for(String str : header)
		{
			System.out.println(str);
		}
	}

	public void showExitMessage(User user)
	{
		User thisUser = user;

		showHeaderPanel();

		System.out.println("Total de acessos do usuário: " + thisUser.getAccess());

		System.out.println("Saída do sistema");

		System.out.println("Até a próxima!");

		System.out.println("1 - Sair\t\t2 - Voltar Menu Principal");
	}

	public void RegisterNewUser()
	{
		showHeaderPanel();
		
		try {

			//MySQL.getInstance();
			
			MySQL.createConnection();

			MySQL.createStatement();
			
			ResultSet set = MySQL.executeQuery(String.format("select count(*) from usuarios;"));
			
			System.out.println(set.toString());
			
			System.out.println("Total de usuários do sistema: " );
		} catch(Exception e)
		{
			System.out.println("Erro: " + e.getMessage());
		}
		
	}

	public static void main(String[] args) {
		User user = new User();
		user.setAccess(1);
		user.setEmail("felipe.h.bezerra@gmail.com");
		user.setGroup(Group.ADMIN);
		user.setName("felipe");

		mainPanel = new SystemManagerPanel(user);

		//headerPanel.showHeaderPanel();

		mainPanel.core(user);

	}




	private void core(User user)
	{		
		User currentUser = user;
		Scanner sc = new Scanner(System.in);

		String command = "";

		do
		{
			mainPanel.showHeaderPanel();

			System.out.print("Comando: ");

			command = sc.next();

			switch(command)
			{
			case "1":
				RegisterNewUser();
				break;

			case "2":
				break;

			case "3":
				break;

			case "4":
				// Sair do sistema
				mainPanel.showExitMessage(currentUser);

				command = sc.next();

				if(command.contentEquals("1"))
				{
					sc.close();
					System.exit(0);
				}
				else {
					mainPanel.core(currentUser);
				}

				break;

			default:

			}

		}
		while(command.length() != 0);
	}

}
