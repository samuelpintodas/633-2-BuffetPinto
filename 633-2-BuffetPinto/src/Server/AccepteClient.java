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

	// Variable initialization
	private Serialize serialize;

	private Socket clientSocketOnServer = null;
	private Client client = null;
    private ArrayList<AccepteClient> connectedClientList;
	private Log log;
	private ObjectOutputStream outStream = null;

	//Client variable
	private PrintWriter validate = null;
	private ObjectOutputStream out = null;




	//Constructor
	public AccepteClient (Socket clientSocketOnServer, ArrayList<AccepteClient> connectedClientList, Serialize serialize,Log log)
	{
		//take parameter to this class
		  this.clientSocketOnServer = clientSocketOnServer;
	      this.serialize = serialize;
	      this.connectedClientList = connectedClientList;
	      this.log = log;
	}


	//run the thread
	@SuppressWarnings({"deprecation","unchecked"})
	public void run()
	{
		try
		{
			//create an Arraylist serializable from all clients
			ArrayList<Client> listOfClient=new ArrayList<>();
			outStream = new ObjectOutputStream(clientSocketOnServer.getOutputStream());
			ObjectInputStream in = new ObjectInputStream(clientSocketOnServer.getInputStream());
			client = (Client) in.readObject();

			if(!(serialize.deSerialize() == null))
			{
				listOfClient = (ArrayList<Client>) (serialize.deSerialize());
			}
			// create a client to serialize only name
			Client newClient = new Client(client.getName());
			log.write(client.getName()+" connection validée", "info");
			listOfClient.add(newClient);
			//serialize the new client
			serialize.serialize(listOfClient);
			//send a file list to the client
			updateFileClient();
		}
		catch (IOException | ClassNotFoundException e)
		{
			e.printStackTrace();
			log.write("failed to connect with the client", "severe");
		}
	}

	// method to send the filelist to the client
	private void updateFileClient() throws IOException
	{
		// create an new Arraylist to send to the client
		ArrayList<Client> allClients = new ArrayList<Client>();
		// foreach
		for (AccepteClient accepteClient : connectedClientList)
		{
				allClients.add(client);
				accepteClient.outStream.writeObject(allClients);
				accepteClient.outStream.flush();
		}
	}
}