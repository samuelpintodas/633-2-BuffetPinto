
package Client;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import java.awt.Font;
import java.awt.Color;
import java.awt.CardLayout;

public class ClientConnection
{
	private Client client = null;
	private String Clientlogin = "";
	private String Clientpassword = "";
	private String ClientIP = "";
	private String ServerIP = "";
	private ArrayList<String> allFilesList = new ArrayList<String>();
	private ArrayList<Client> clientList = new ArrayList<>();

	private ServerSocket listeningSocket;
	private int clientPort = 45005;
	private boolean exist;

	private Socket clientSocket = null;
	private ObjectOutputStream oos = null;
	private ObjectInputStream ois = null;
	private BufferedReader buffin = null;
	private File directory = new File("C:\\ClientFiles");

	private ClientFrame clientFrame;
	private JFileChooser fc = new JFileChooser();



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
		allFilesList = getListOfFiles();

		System.out.println("Voici les infos que l'on ma donner pour me connecter : \n " + " login : " + Clientlogin + " mdp :  " +  Clientpassword +  " monIP : " + ClientIP +" ipServer : "+ ServerIP + " j'existe : "+ exist );
		//va contr�ler si l'objet existe d�ja lors de la s�rialisation.
		client = new Client(Clientlogin, Clientpassword, ClientIP, allFilesList, exist);
		System.out.println("coucou");
		oos.writeObject(client);
	}


	// listening code
	private void connectToClient() throws IOException
	{
		exist = true;

		Thread waitThread = new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {

					Object tempClient = null;
					try
					{
						prepareClientSocket(ClientIP, clientPort);
						Socket tempSocket;
						tempSocket = listeningSocket.accept();

						ois = new ObjectInputStream(clientSocket.getInputStream());
						oos = new ObjectOutputStream(tempSocket.getOutputStream());

						try
						{
							tempClient = ois.readObject();

						} catch (ClassNotFoundException cnfe) {
							cnfe.printStackTrace();
						} catch (IOException ioe) {
							ioe.printStackTrace();
						}
						FileAsk cFileAsk = (FileAsk) tempClient;
						File receptionDir = directory;

						if(!directory.exists())
						{
							JOptionPane.showMessageDialog(clientFrame, "Directory " + directory.getName() + " not found.");
							return;
						}

						if(!cFileAsk.getReceiver().getIp().equals(client.getIp()))
						{
							JOptionPane.showConfirmDialog(clientFrame, "Incorrect receiver IP");
							return;
						}

						File askedFile = null;

						for (Object file: directory.listFiles())
						{
							if(file.getClass().getName().equals(cFileAsk.getFileName()))
							{
								askedFile = (File) file;
								break;
							}
						}

						String absPath = askedFile.getAbsolutePath();
						Files.copy(Paths.get(absPath), oos);
						tempSocket.close();

					} catch (IOException ioe) {
						ioe.printStackTrace();
					}
				}
			}

		});

	}

	private void download()
	{

	}




	private ArrayList<String> getListOfFiles()
	{


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

	public class FileAsk implements Serializable {
		private final String fileName;
		private final Client sender;
		private final Client receiver;

		public FileAsk(String nameFile, Client sender, Client receiver) {
			this.fileName = nameFile;
			this.sender = sender;
			this.receiver = receiver; //tout l'objet client (donc accès ip adress , name etc)
		}

		public String getFileName() {return fileName;}

		public Client getSender() {return sender;}

		public Client getReceiver() {return receiver; }
	}
}


