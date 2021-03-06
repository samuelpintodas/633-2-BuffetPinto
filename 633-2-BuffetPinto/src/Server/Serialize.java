package Server;

import Client.Client;

import java.io.*;
import java.util.ArrayList;


public class Serialize 
{
	// initialization of variable
	private String path = "Client\\client.zer";
	private Client admin = new Client("Admin");
	private ArrayList<Client> list = new ArrayList<>();
	Log log;

	public Serialize() 
	{
		super();
	}

	// method to Serialize
	public void serialize(ArrayList o)
	{
		try 
		{
			FileOutputStream fichier = new FileOutputStream(path);
			ObjectOutputStream oos = new ObjectOutputStream(fichier);
			oos.writeObject(o);
			oos.flush();
			oos.close();
		}
		catch (java.io.IOException e) 
		{
			e.printStackTrace();
			log.write("serialisation fail","severe");
		}
	}

	// method to deserialized
	public Object deSerialize()
	{ 		
		ArrayList cs = null;

		try 
		{
			FileInputStream fichier = new FileInputStream(path);
			ObjectInputStream ois = new ObjectInputStream(fichier);
			cs =(ArrayList)ois.readObject();
		}
		catch (Exception e) 
		{
			cs = new ArrayList();
		}

		return cs;
	}

	// methode to create a file use when no file are created
	public void createFile()
	{
		File f = new File("Client//client.zer");

		try
		{
			if (!f.getParentFile().exists())
				f.getParentFile().mkdir();

			if(!f.exists())
			{
				f.createNewFile();
				list.add(admin);
				serialize(list);
			}

			
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.write("File creation failed","severe");
		} 
	}
}
