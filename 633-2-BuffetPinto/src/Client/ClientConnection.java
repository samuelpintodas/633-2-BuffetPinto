
package Client;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import java.awt.Font;
import java.awt.Color;
import java.awt.CardLayout;
import javax.swing.JComboBox;

public class ClientConnection 
{
	private Client client = null;
	private String Clientlogin = "";
	private String Clientpassword = "";
	private String ClientIP = "";
	private String ServerIP = "";
	private String [] ClientListFiles = null ;
	
	private boolean exist;
	private Socket clientSocket = null;
	private ObjectOutputStream out = null;
	private ObjectInputStream ois = null;
	private BufferedReader buffin = null;
	private JFileChooser fc = new JFileChooser();
	private ArrayList<Client> listOfClients = new ArrayList<>();
	
	public ClientConnection() throws IOException
	{
		connect();
	}
	  

	private void connect() throws IOException
	{	
		exist = true;
		ServerIP = "127.0.0.1";
		clientSocket = new Socket(ServerIP, 45005);

		out = new ObjectOutputStream(clientSocket.getOutputStream());
		buffin = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

		Clientlogin = "Admin";
		Clientpassword = "1234";
		ClientIP = clientSocket.getLocalAddress().getHostAddress(); //nous donne l'adresse ip du client.
		ClientListFiles = getListOfFiles();

		System.out.println("Voici les infos que l'on ma donner pour me connecter : \n " + " login : " + Clientlogin + " mdp :  " +  Clientpassword +  " monIP : " + ClientIP +" ipServer : "+ ServerIP + " j'existe : "+ exist );
		//va contrôler si l'objet existe déja lors de la sérialisation.
		client = new Client(Clientlogin, Clientpassword, ClientIP, ClientListFiles, exist);
		System.out.println("coucou");
		out.writeObject(client);

		String controle = buffin.readLine();

	}


	private String [] getListOfFiles()
	{
		File directory = new File("C:\\ClientFiles");

		if(!directory.exists())
			directory.mkdir();

		String [] files = new String [directory.list().length];
		File [] lst = directory.listFiles();

		for (int i = 0; i < files.length; i++) 
		{
			files[i] = lst[i].getName();
		}

		return files;
	}

}
