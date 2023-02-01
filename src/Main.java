import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Scanner;
import java.io.File;
import java.io.*;

public class Main extends JFrame {

    private HuffmanCodingOnFile huffmanCodingOnFiles = new HuffmanCodingOnFile();
    private JTextField filePathField;
    private JLabel resultLabel;
    private JLabel pathLabel;
    private JTextField pathField;
    private JLabel locationOp;


    public Main() {
        setTitle("TXT File Compressor");
        setSize(700, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new FlowLayout());
        JLabel filePathLabel = new JLabel("Enter file path: ");
        add(filePathLabel);
        filePathLabel.setFont(filePathLabel.getFont().deriveFont(14.0f));

        filePathField = new JTextField(20);
        add(filePathField);
//------------------------------------------------------------------------
//buttons : compress and decompress
        JButton compressButton = new JButton("Compress");
        compressButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(compressButton);

        JButton decompressButton = new JButton("Decompress");
        decompressButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(decompressButton);

        compressButton.setBackground(new Color(173,216,230));
        decompressButton.setBackground(new Color(254, 216, 177));


//-----------------------------------------------------------------------
//locationop : display op file path location

        resultLabel = new JLabel("");
        add(resultLabel);
        resultLabel.setForeground(Color.RED);
        resultLabel.setFont(resultLabel.getFont().deriveFont(16.0f));
        locationOp = new JLabel("");
        add(locationOp);

//-----------------------------------------------------------------------

        compressButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String filePath = filePathField.getText();
                long start = System.currentTimeMillis();
                try {
                    File file = new File(filePathField.getText());
                    huffmanCodingOnFiles.compressFile(filePath);
                    long end = System.currentTimeMillis();
                    resultLabel.setText("Compressed in " + (end - start) + "ms");
                    int pathLen = filePath.length();
                    String tempFilePath = filePath.substring(0,pathLen-3);
                    locationOp.setText("Compressed file path "+ tempFilePath+"compressed");
                } catch (IOException ex) {
                    System.out.println("file not found");
                    resultLabel.setText("Invalid file path. If the path is correct, make sure to add the '.txt' extension.");
                }
            }
        });

        decompressButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String filePath = filePathField.getText();
                long start = System.currentTimeMillis();
                long end = System.currentTimeMillis();
                resultLabel.setText("Decompressed in " + (end - start) + "ms");
                try {
                    huffmanCodingOnFiles.decompressFile(filePath);
                    int pathLen = filePath.length();
                    String tempFilePath = filePath.substring(0,pathLen-10);
                    locationOp.setText("Compressed file path "+ tempFilePath+"compressed");
                } catch (IOException ex) {
                    resultLabel.setText("Invalid file path. If the path is correct, make sure to add the '.compressed' extension.");
                }

            }
        });
    }

    public static void main(String[] args) {
        Main gui = new Main();
        gui.setVisible(true);
    }
}
