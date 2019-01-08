package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    private Socket clientSocket = null;
    private Serialize serialize = new Serialize();
    private ArrayList<AccepteClient> listClientsConnected = new ArrayList<>();


    public Server()
    {

    	//cree les fichiers de sauvegarde user en cas de non existence
        serialize.createFile();

        ServerSocket mySkServer;

        try {
            mySkServer = new ServerSocket(45005, 5);
            System.out.println("j'ai ouvert ma connection au port : " + 45005);
            //wait for a client connection
            while (true)
            {
                clientSocket = mySkServer.accept();
                System.out.println("connection request received : " +  clientSocket.getPort() + "Adress du client : " + clientSocket.getInetAddress());

                //Cr�ation du thread :
                //le socket , ma list (toujours a jour) , mon objet s�rialiser
                Thread t = new AccepteClient(clientSocket, listClientsConnected, serialize);

                //starting the thread
                t.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}