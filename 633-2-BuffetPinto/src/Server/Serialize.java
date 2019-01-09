package Server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import Client.Client;


public class Serialize 
{
	private String path = "Client\\client.zer";
	private Client admin = new Client("Admin", "1234");
	private ArrayList<Client> list = new ArrayList<>();
	Log log;

	public Serialize() 
	{
		super();
	}

	public void serializeObject(Object o) 
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

	public Object deSerializeObject() 
	{ 		
		Object cs = null;

		try 
		{
			FileInputStream fichier = new FileInputStream(path);
			ObjectInputStream ois = new ObjectInputStream(fichier);
			cs = ois.readObject();
		}
		catch (Exception e) 
		{
			cs = new Object();
			log.write("deserialisation fail","severe");
		}

		return cs;
	}

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
				serializeObject(list);
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
