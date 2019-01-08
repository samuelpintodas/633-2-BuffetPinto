package Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.*;
import java.util.ArrayList;
import java.util.logging.Logger;

import Client.Client;

public class AccepteClient extends Thread
{
	private Serialize serialize;

	private Socket clientSocketOnServer = null;
	private Client client = null;
    private ArrayList<AccepteClient> connectedClientList;
    private ArrayList<String> allFiles;
	private Log log;

	//Info pour le client
	private PrintWriter validate = null;
	private ObjectOutputStream out = null;
	private String validation = "-1";



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
			ObjectInputStream in = new ObjectInputStream(clientSocketOnServer.getInputStream());
			client = (Client) in.readObject();

			/// ajout debut
			ArrayList<Client> listOfClient = (ArrayList<Client>) (serialize.deSerializeObject());
			if (client.isExist())
			{
				//Controle si le client existe deja
				for (Client clientRegistered : listOfClient)
				{
					if (clientRegistered.getName().equalsIgnoreCase(client.getName()))
					{
						log.write(client.getName()+" reconnu","info");
					}
				}
			}
			else {
				//Controle si le client existe deja
				for (Client clientRegistered : listOfClient) {
					if (clientRegistered.getName().equalsIgnoreCase(client.getName())) {
						log.write(client.getName() + " existe déjà, connection refusée","info");
					} else {
						Client newClient = new Client(client.getName(), client.getMdp());
						log.write(client.getName() + " créé, connection validée","info");
						listOfClient.add(newClient);
						serialize.serializeObject(listOfClient);
					}
				}
			}
			// ajout fin
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
			log.write(e.getMessage().toString(), "severe");
		}
	}
}