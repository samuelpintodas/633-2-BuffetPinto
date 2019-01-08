package Client;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JComboBox;
import javax.swing.JList;
import java.awt.*;

public class ClientFrame extends JFrame
{

    private JPanel contentPane;
    private JComboBox clientChoose;
    private JPanel buttonsPanel;
    private Button downloadButton;
    private Button quitButton;
    private Button quitServButton;
    private JList filesList;


    public ClientFrame()
    {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 475, 595);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        clientChoose = new JComboBox();
        contentPane.add(clientChoose, BorderLayout.NORTH);

        buttonsPanel = new JPanel();
        contentPane.add(buttonsPanel, BorderLayout.SOUTH);
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        downloadButton = new Button("Download");
        buttonsPanel.add(downloadButton, BorderLayout.EAST);

        quitButton = new Button("Quit");
        buttonsPanel.add(quitButton, BorderLayout.EAST);

        quitServButton = new Button("Quit and close server");
        buttonsPanel.add(quitServButton, BorderLayout.EAST);

        filesList = new JList();
        contentPane.add(filesList, BorderLayout.CENTER);

    }

}