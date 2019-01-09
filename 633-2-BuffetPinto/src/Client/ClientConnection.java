
package Client;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.lang.reflect.Array;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import java.awt.Font;
import java.awt.Color;
import java.awt.CardLayout;

public class ClientConnection
{
    // Client variables
    private Client client = null;
    //private String Clientlogin = "";
    private String ClientIP = "";
    private boolean exist;
    private Socket clientSocket = null;
    private int clientPort = 45001;

    // Server variables
    private String ServerIP = "";
    private ArrayList<String> allFilesList = new ArrayList<String>();
    protected ArrayList<Client> clientList = new ArrayList<>();
    private int serverPort = 45005;
    private ObjectInputStream oisServer;

    // Sockets for upload and download
    private Socket downloadSocket = null;
    private ServerSocket listeningSocket;

    // "Files" variables
    private ObjectOutputStream oos = null;
    private ObjectInputStream ois = null;
    private BufferedReader buffin = null;
    private File directory = new File("C:\\ClientFiles");

    // GUI variables
    private ClientFrame clientFrame;
    private JFileChooser fc = new JFileChooser();

    protected Client getClient() {
        return client;
    }
    protected ArrayList<Client> getClientList() {
        return clientList;
    }
    protected String getServerIP() {return ServerIP;}
    public int getClientPort() {return clientPort;}

    public ClientConnection() throws IOException
    {
        //connectToServer();
        //connectToClient();
    }

    private void prepareClientSocket(String cName, int cPort) {
        try {
            InetAddress localAddress = InetAddress.getByName(cName);
            listeningSocket = new ServerSocket(cPort, 5, localAddress);
        } catch (UnknownHostException e1) {
            e1.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    protected void connectToServer(String serverIP, String clientName) throws IOException
    {
        exist = true;
        //serverIP = "localhost";
        clientSocket = new Socket(serverIP, serverPort);

        oos = new ObjectOutputStream(clientSocket.getOutputStream());
        oisServer = new ObjectInputStream(clientSocket.getInputStream());
        buffin = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        //
        ClientIP = clientSocket.getLocalAddress().getHostAddress(); //nous donne l'adresse ip du client.
        allFilesList = getListOfFiles();

        System.out.println("Voici les infos que l'on ma donner pour me connecter : \n " + " login : " + clientName +
                " monIP : " + ClientIP +" ipServer : "+ ServerIP + " j'existe : "+ exist );
        //va contr�ler si l'objet existe d�ja lors de la s�rialisation.
        client = new Client(clientName, ClientIP, allFilesList, exist);
        System.out.println("coucou");
        oos.writeObject(client);

    }


    // listening code
    protected void connectToClient() throws IOException
    {
        exist = true;
        prepareClientSocket(ClientIP, clientPort);


        Thread listenThread = new Thread(new Runnable() {

            @Override
            public void run() {

                while (exist) {
                    try
                    {
                        Socket tempSocket;
                        tempSocket = listeningSocket.accept();

                        new SendFileThread(tempSocket).start();



                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                }
            }

        });
        listenThread.start();

    }

    protected void getClients()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                while(exist)
                {
                    try
                    {
                        Object o = oisServer.readObject();
                        ArrayList<Client> cList = (ArrayList<Client>)o;
                        if(cList.size() > 0 && cList.get(0) instanceof Client)
                        {
                            setClientList(cList);
                        }
                    }
                    catch(IOException ioe)
                    {
                        ioe.printStackTrace();
                    }
                    catch(ClassNotFoundException cnfe)
                    {
                        cnfe.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void setClientList(ArrayList<Client> cList) {
        clientList = cList;
    }

    protected void download(Client fileOwner, FileAsk fAsk)
    {
        new Thread(new Runnable() {
            InputStream is;
            ObjectOutputStream oos;

            @Override
            public void run() {
                try
                {
                    downloadSocket = new Socket(fAsk.getReceiver().getIp(), clientPort);
                    System.out.println(fAsk.getReceiver().getIp());
                    is = downloadSocket.getInputStream();
                    oos = new ObjectOutputStream(downloadSocket.getOutputStream());
                    oos.writeObject(fAsk);
                    System.out.println(Paths.get(directory + "\\" + fAsk.getFileName()).toString());
                    Files.copy(is, Paths.get(directory + "\\" + fAsk.getFileName()));
                }
                catch(IOException ioe)
                {
                    JOptionPane.showMessageDialog(clientFrame, "Impossible download");
                    ioe.printStackTrace();
                }

            }
        }).start();
    }

    private ArrayList<String> getListOfFiles()
    {


        if(!directory.exists())
            directory.mkdir();

        ArrayList<String> filesList = new ArrayList<String>();
        File [] fTab= directory.listFiles();

        for (int i = 0; i < fTab.length; i++)
        {
            filesList.add(fTab[i].getName());
        }

        return filesList;
    }

    protected int disconnect(String serverName, int disconnectServerPort) {
        /*
        InetAddress serverAddress;
        try {
            serverAddress = InetAddress.getByName(serverName);
            Socket disconnectSocket = new Socket();
            disconnectSocket.connect(new InetSocketAddress(serverAddress, disconnectServerPort), 5);
            disconnectSocket.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;*/
        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        exist=false;
        return 0;
    }


    protected static class FileAsk implements Serializable
    {
        private final String fileName;
        private final Client sender;
        private final Client receiver;

        public FileAsk(String nameFile, Client sender, Client receiver) {
            this.fileName = nameFile;
            this.sender = sender;
            this.receiver = receiver;
        }

        public String getFileName() {return fileName;}

        public Client getSender() {return sender;}

        public Client getReceiver() {return receiver; }
    }

    private class SendFileThread extends Thread {
        Socket tempSocket;
        Object tempClient = null;
        private OutputStream os = null;
        private ObjectInputStream ois = null;

        public SendFileThread(Socket tempSocket) {
            this.tempSocket = tempSocket;
        }

        @Override
        public void run() {
            try {
                ois = new ObjectInputStream(tempSocket.getInputStream());
                os = tempSocket.getOutputStream();
                tempClient = ois.readObject();
                FileAsk cFileAsk = (FileAsk) tempClient;
                File receptionDir = directory;

                if(!directory.exists())
                {
                    JOptionPane.showMessageDialog(clientFrame, "Directory " + directory.getName() + " not found.");
                    return;
                }

                if(!cFileAsk.getReceiver().getIp().equals(client.getIp()))
                {
                    JOptionPane.showConfirmDialog(clientFrame, "Incorrect receiver IP");
                    return;
                }

                File askedFile = null;

                for (File file: directory.listFiles())
                {
                    if(file.getName().equals(cFileAsk.getFileName()))
                    {
                        askedFile = (File) file;
                        break;
                    }
                }

                String absPath = askedFile.getAbsolutePath();
                Files.copy(Paths.get(absPath), os);
                tempSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException cnfe) {
                cnfe.printStackTrace();
            }
        }
    }
}


