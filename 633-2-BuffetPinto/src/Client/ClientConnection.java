
package Client;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.lang.reflect.Array;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import java.awt.Font;
import java.awt.Color;
import java.awt.CardLayout;

public class ClientConnection
{
	// Client variables
	private Client client = null;
	//private String Clientlogin = "";
	private String ClientIP = "";
	private boolean exist;
	private Socket clientSocket = null;
	private int clientPort = 45001;

	// Server variables
	private String ServerIP = "";
	private ArrayList<String> allFilesList = new ArrayList<String>();
	protected ArrayList<Client> clientList = new ArrayList<>();
	private int serverPort = 45005;

	// Sockets for upload and download
	private Socket downloadSocket = null;
	private ServerSocket listeningSocket;

	// "Files" variables
	private ObjectOutputStream oos = null;
	private ObjectInputStream ois = null;
	private BufferedReader buffin = null;
	private File directory = new File("C:\\ClientFiles");

	// GUI variables
	private ClientFrame clientFrame;
	private JFileChooser fc = new JFileChooser();


	protected Client getClient() {
		return client;
	}
	protected ArrayList<Client> getClientList() {
		return clientList;
	}
	protected String getServerIP() {return ServerIP;}
	public int getClientPort() {return clientPort;}

	public ClientConnection() throws IOException
	{
		//connectToServer();
		//connectToClient();
	}

	private void prepareClientSocket(String cName, int cPort) {
		try {
			InetAddress localAddress = InetAddress.getByName(cName);
			listeningSocket = new ServerSocket(cPort, 5, localAddress);
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
	}

	protected void connectToServer(String serverIP, String clientName) throws IOException
	{
		exist = true;
		//serverIP = "localhost";
		clientSocket = new Socket(ServerIP, serverPort);

		oos = new ObjectOutputStream(clientSocket.getOutputStream());
		buffin = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

		//
		ClientIP = clientSocket.getLocalAddress().getHostAddress(); //nous donne l'adresse ip du client.
		allFilesList = getListOfFiles();


		System.out.println("Voici les infos que l'on ma donner pour me connecter : \n " + " login : " + clientName +
											" monIP : " + ClientIP +" ipServer : "+ ServerIP + " j'existe : "+ exist );
		//va contr�ler si l'objet existe d�ja lors de la s�rialisation.
		client = new Client(clientName, ClientIP, allFilesList, exist);
		System.out.println("coucou");
		oos.writeObject(client);
	}


	// listening code
	protected void connectToClient() throws IOException
	{
		exist = true;

		Thread listenThread= new Thread(new Runnable() {

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
		listenThread.start();

	}

	protected void getClients()
	{
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				while(true)
				{
					try
					{
						ArrayList<Client> cList = (ArrayList<Client>)ois.readObject();

						if(cList.size() > 0 && cList.get(0) instanceof  Client)
						{
							clientList = cList;
						}
					}
					catch(IOException ioe)
					{
						ioe.printStackTrace();
					}
					catch(ClassNotFoundException cnfe)
					{
						cnfe.printStackTrace();
					}
				}
			}
		});
	}

	protected void download(String fileName, Client fileOwner, FileAsk fAsk)
	{
		new Thread(new Runnable() {

			@Override
			public void run() {
			try
			{
				downloadSocket = new Socket(fAsk.getReceiver().getIp(), clientPort);
				System.out.println(fAsk.getReceiver().getIp());
				ois = new ObjectInputStream(downloadSocket.getInputStream());
				oos = new ObjectOutputStream(downloadSocket.getOutputStream());
				oos.writeObject(fAsk);
				Files.copy(ois, Paths.get(directory + fAsk.getFileName()));
			}
			catch(IOException ioe)
			{
				JOptionPane.showMessageDialog(clientFrame, "Impossible download");
				ioe.printStackTrace();
			}

			}
		}).start();
	}

	private ArrayList<String> getListOfFiles()
	{


		if(!directory.exists())
			directory.mkdir();

		ArrayList<String> filesList = new ArrayList<String>();
		File [] fTab= directory.listFiles();

		for (int i = 0; i < fTab.length; i++)
		{
			filesList.add(fTab[i].getName());
		}

		return filesList;
	}

	protected static int disconnect(String serverName, int disconnectServerPort) {
		InetAddress serverAddress;
		try {
			serverAddress = InetAddress.getByName(serverName);
			Socket disconnectSocket = new Socket();
			disconnectSocket.connect(new InetSocketAddress(serverAddress, disconnectServerPort), 5);
			disconnectSocket.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}



	protected static class FileAsk implements Serializable
	{
		private final String fileName;
		private final Client sender;
		private final Client receiver;

		public FileAsk(String nameFile, Client sender, Client receiver) {
			this.fileName = nameFile;
			this.sender = sender;
			this.receiver = receiver;
		}

		public String getFileName() {return fileName;}

		public Client getSender() {return sender;}

		public Client getReceiver() {return receiver; }
	}

}


