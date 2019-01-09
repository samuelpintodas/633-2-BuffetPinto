package Client;

import java.io.Serializable;
import java.util.ArrayList;

public class Client implements Serializable 
{
	private String ClientName;
	private String ClientIp;
	private ArrayList<String> ClientlstFiles;
	private boolean exist;
	
	public Client(String ClientName, String ClientIp, ArrayList<String> ClientlstFiles, boolean exist)
	{
		this.ClientName = ClientName;
		this.ClientIp = ClientIp;
		this.ClientlstFiles = ClientlstFiles;
		this.exist = exist;
	}
	
	public Client(String name)
	{
		this.ClientName = ClientName;
	}


    public boolean isExist()
	{
		return exist;
	}

	public String getName() 
	{
		return ClientName;
	}

	public String getIp() {
		return ClientIp;
	}

	public ArrayList<String> getListOfFiles()
	{
		return ClientlstFiles;
	}

	public void setListOfFiles(ArrayList<String> ClientlstFiles)
	{
		this.ClientlstFiles = ClientlstFiles;
	}
	
	@Override
	public String toString() {
		return ClientName ;
	}
}
