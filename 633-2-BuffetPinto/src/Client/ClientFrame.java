package Client;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import Client.ClientConnection.FileAsk;

public class ClientFrame extends JFrame {

    private JPanel contentPane;
    private JComboBox clientChoose;

    private JPanel buttonsPanel;
    private Button downloadButton;
    private Button quitButton;

    private JList filesList;
    private DefaultListModel<String> listModel;

    private ClientConnection cc;

    public ClientFrame()
    {
        try
        {
            cc = new ClientConnection();
        }
        catch(IOException ioe)
        {
            ioe.printStackTrace();
        }

        downloadButton = new Button("Download");
        quitButton = new Button("Quit");
        clientChoose = new JComboBox();
        buttonsPanel = new JPanel();
        listModel = new DefaultListModel<>();
        filesList = new JList(listModel);

        setBounds(100, 100, 475, 595);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        downloadButton.addActionListener(new DownloadClick());
        quitButton.addActionListener(new QuitClick());

        buttonsPanel.add(downloadButton, FlowLayout.CENTER);
        buttonsPanel.add(quitButton, FlowLayout.RIGHT);

        contentPane.add(clientChoose, BorderLayout.NORTH);
        contentPane.add(buttonsPanel, BorderLayout.SOUTH);
        contentPane.add(filesList, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private class DownloadClick implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            Client owner = cc.getClientList().get(clientChoose.getSelectedIndex());
            String file = listModel.get(filesList.getSelectedIndex());

            FileAsk fAsk = new FileAsk(file, cc.getClient(), owner);

            cc.download(file, owner, fAsk);

        }
    }

    private class QuitClick implements ActionListener
    {
        int disconnectPort = cc.getClientPort()+1;
        @Override
        public void actionPerformed(ActionEvent e)
        {
            cc.disconnect(cc.getServerIP(), disconnectPort);
        }
    }


    private class switchClient implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) {

            if(!(clientChoose.getItemCount() > 0))
            {
                JOptionPane.showMessageDialog(ClientFrame.this,"There's no client in the list");
            }
            else
            {
                ArrayList<String> clientFileList = new ArrayList<>(cc.getClientList().get(clientChoose.getSelectedIndex()).getListOfFiles());
                if(clientFileList.size() < 1)
                {
                    JOptionPane.showMessageDialog(ClientFrame.this,"No file found for this client");
                }
                for (String file: clientFileList)
                {
                    listModel.addElement(file);
                }
            }
        }
    }
}