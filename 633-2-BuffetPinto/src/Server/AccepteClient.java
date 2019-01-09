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
	private Log log;
	private ObjectOutputStream outStream = null;

	//Info pour le client
	private PrintWriter validate = null;
	private ObjectOutputStream out = null;



	//Constructor
	public AccepteClient (Socket clientSocketOnServer, ArrayList<AccepteClient> connectedClientList, Serialize serialize,Log log)
	{
		//r�cuperation des objet pass� en param�tre
		  this.clientSocketOnServer = clientSocketOnServer;
	      this.serialize = serialize;
	      this.connectedClientList = connectedClientList;
	      this.log = log;
	}

	@SuppressWarnings("unchecked")
	public void run()
	{
		try
		{

			outStream = new ObjectOutputStream(clientSocketOnServer.getOutputStream());
			ObjectInputStream in = new ObjectInputStream(clientSocketOnServer.getInputStream());
			client = (Client) in.readObject();


			ArrayList<Client> listOfClient = (ArrayList<Client>) (serialize.deSerializeObject());

			System.out.println("je suis un thread : " + client);
			System.out.println(client.getIp() + " " +client.getName());
            out = new ObjectOutputStream(clientSocketOnServer.getOutputStream());
            validate = new PrintWriter(out);
			this.connectedClientList.add(this);
			updateFileClient();

		}
		catch (IOException | ClassNotFoundException e)
		{
			e.printStackTrace();
			log.write("failed to connect with the client", "severe");
		}
	}

	private void updateFileClient() throws IOException
	{
		ArrayList<Client> allClients = new ArrayList<Client>();
		for (AccepteClient accepteClient : connectedClientList)
		{
			if(accepteClient.client != client)
				allClients.add(accepteClient.client);
			else
				allClients.add(client);
		}
		for (AccepteClient accepteClient : connectedClientList)
		{
			accepteClient.outStream.writeObject(allClients);
			accepteClient.outStream.flush();
		}
	}
}