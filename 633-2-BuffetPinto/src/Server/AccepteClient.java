package Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.*;
import java.util.ArrayList;
import Client.Client;

public class AccepteClient extends Thread 
{
	private Serialize serialize;
	
	private Socket clientSocketOnServer = null;
	private Client client = null;
    private ArrayList<AccepteClient> connectedClientList;
    private ArrayList<String> allFiles;

	//Info pour le client
	private PrintWriter validate = null;
	private ObjectOutputStream out = null;
	private String validation = "-1";



	//Constructor
	public AccepteClient (Socket clientSocketOnServer, ArrayList<AccepteClient> connectedClientList, Serialize serialize)
	{
		//r�cuperation des objet pass� en param�tre
		  this.clientSocketOnServer = clientSocketOnServer;
	      this.serialize = serialize;
	      this.connectedClientList = connectedClientList;
	}

	@SuppressWarnings("unchecked")
	public void run() 
	{
		try 
		{
			ObjectInputStream in = new ObjectInputStream(clientSocketOnServer.getInputStream());
			client = (Client) in.readObject();
			
			System.out.println("je suis un thread : " + client);
			System.out.println(client.getIp() + client.getMdp() + client.getMdp());

			// a supprimer plus tard
            for (String fileName: client.getListOfFiles())
            {
                allFiles.add(fileName);
                System.out.println(fileName);
                
            }
            out = new ObjectOutputStream(clientSocketOnServer.getOutputStream());
            validate = new PrintWriter(out);


			
		}
		catch (IOException | ClassNotFoundException e) 
		{
			e.printStackTrace();
		} 
	}
}