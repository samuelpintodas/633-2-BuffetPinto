
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
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
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
	private ArrayList<String> ClientListFiles = null ;

	private ServerSocket listeningSocket;
	private int clientPort = 45005;
	private boolean exist;
	private Socket clientSocket = null;
	private ObjectOutputStream oos = null;
	private ObjectInputStream ois = null;
	private BufferedReader buffin = null;


	private JFileChooser fc = new JFileChooser();
	private ArrayList<Client> listOfClients = new ArrayList<>();
	private ArrayList<String> serverFileList = new ArrayList<String>();

	public ClientConnection() throws IOException
	{
		connectToServer();
	}

	public void prepareClientSocket(String cName, int cPort) {
		try {
			InetAddress localAddress = InetAddress.getByName(cName);
			listeningSocket = new ServerSocket(cPort, 5, localAddress);
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
	}

	private void connectToServer() throws IOException
	{
		exist = true;
		ServerIP = "localhost";
		clientSocket = new Socket(ServerIP, 45005);

		oos = new ObjectOutputStream(clientSocket.getOutputStream());
		buffin = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

		Clientlogin = "Admin";
		Clientpassword = "1234";
		ClientIP = clientSocket.getLocalAddress().getHostAddress(); //nous donne l'adresse ip du client.
		ClientListFiles = getListOfFiles();

		System.out.println("Voici les infos que l'on ma donner pour me connecter : \n " + " login : " + Clientlogin + " mdp :  " +  Clientpassword +  " monIP : " + ClientIP +" ipServer : "+ ServerIP + " j'existe : "+ exist );
		//va contr�ler si l'objet existe d�ja lors de la s�rialisation.
		client = new Client(Clientlogin, Clientpassword, ClientIP, ClientListFiles, exist);
		System.out.println("coucou");
		oos.writeObject(client);
	}



	private void connectToClient() throws IOException
	{
		exist = true;

		Thread waitThread = new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					try {
						prepareClientSocket(ClientIP, clientPort);
						Socket tempSocket;
						tempSocket = listeningSocket.accept();


						oos = new ObjectOutputStream(clientSocket.getOutputStream());
						buffin = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

						Clientlogin = "Admin";
						Clientpassword = "1234";
						ClientIP = clientSocket.getLocalAddress().getHostAddress(); //nous donne l'adresse ip du client.
						ClientListFiles = getListOfFiles();

						System.out.println("Voici les infos que l'on ma donner pour me connecter : \n " + " login : " + Clientlogin + " mdp :  " +  Clientpassword +  " monIP : " + ClientIP +" ipServer : "+ ServerIP + " j'existe : "+ exist );
						//va contr�ler si l'objet existe d�ja lors de la s�rialisation.
						client = new Client(Clientlogin, Clientpassword, ClientIP, ClientListFiles, exist);
						System.out.println("coucou");
						oos.writeObject(client);
					} catch (Exception e)
					{

					}
				}
			}

		};

	}




	private ArrayList<String> getListOfFiles()
	{
		File directory = new File("C:\\ClientFiles");

		if(!directory.exists())
			directory.mkdir();

		ArrayList<String> filesList = new ArrayList<String>();
		//String [] files = new String [directory.list().length];
		File [] fTab= directory.listFiles();

		for (int i = 0; i < fTab.length; i++)
		{
			filesList.add(fTab[i].getName());
		}

		return filesList;
	}

}
