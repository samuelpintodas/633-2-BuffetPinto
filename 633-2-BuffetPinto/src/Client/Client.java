package Client;

import java.io.Serializable;
import java.util.ArrayList;

public class Client implements Serializable 
{
	/*
	 * Nous parton du principe que nous allon travailler avec des objet
	 * donc nous serialisons notre client. et nous le stoqueron dans un fichier
	 */
	private String ClientName;
	private String ClientPassword;
	private String ClientIp;
	private ArrayList<String> ClientlstFiles;
	private boolean exist;
	
	public Client(String ClientName, String ClientPassword, String ClientIp, ArrayList<String> ClientlstFiles, boolean exist)
	{
		this.ClientName = ClientName;
		this.ClientPassword = ClientPassword;
		this.ClientIp = ClientIp;
		this.ClientlstFiles = ClientlstFiles;
		this.exist = exist;
	}
	
	public Client(String name, String mdp) 
	{
		this.ClientName = ClientName;
		this.ClientPassword = ClientPassword;
	}

	public boolean isExist() 
	{
		return exist;
	}

	public String getName() 
	{
		return ClientName;
	}
	
	public String getMdp() 
	{
		return ClientPassword;
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
