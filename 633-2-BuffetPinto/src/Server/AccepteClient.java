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
    private ArrayList<AccepteClient> MaListDeClientConnecter;

	//Info pour le client
	private PrintWriter validate = null;
	private ObjectOutputStream out = null;
	private String validation = "-1";

	//Constructor
	public AccepteClient (Socket clientSocketOnServer, ArrayList<AccepteClient> MaListDeClientConnecter, Serialize serialize)
	{
		//récuperation des objet passé en paramètre
		  this.clientSocketOnServer = clientSocketOnServer;
	      this.serialize = serialize;
	      this.MaListDeClientConnecter = MaListDeClientConnecter;
	}

	@SuppressWarnings("unchecked")
	public void run() 
	{
		try 
		{
			ObjectInputStream in = new ObjectInputStream(clientSocketOnServer.getInputStream());
			client = (Client) in.readObject();
			
			System.out.println("je suis un treahd : " + client);
			System.out.println(client.getIp() + client.getMdp() + client.getMdp());
			
			
		}
		catch (IOException | ClassNotFoundException e) 
		{
			e.printStackTrace();
		} 
	}
}