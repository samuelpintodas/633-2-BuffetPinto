package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    private Socket clientSocket = null;
    private Serialize serialize = new Serialize();
    private ArrayList<AccepteClient> listClientsConnected = new ArrayList<>();
    private Log log = new Log();


    public Server()
    {

    	//create a file if not exist to serialize
        serialize.createFile();

        ServerSocket mySkServer;
        //Initialize log file
        log.createLogger();
        try {
            //create a Sercer socket
            mySkServer = new ServerSocket(45005, 5);
            System.out.println("connection open on port : " + 45005);
            //wait for a client connection
            while (true)
            {
                clientSocket = mySkServer.accept();
                log.write("Client connection on server", "info");
                System.out.println("connection request received : " +  clientSocket.getPort() + "ClientAddress : " + clientSocket.getInetAddress());

                //Creation Thread :
                //initailaize a thread to recieve client
                Thread t = new AccepteClient(clientSocket, listClientsConnected, serialize,log);

                //starting the thread
                t.start();
            }
        } catch (IOException e) {
            log.write("Server fail to run", "severe");
            e.printStackTrace();
        }
    }
}