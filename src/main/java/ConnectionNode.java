import datatype.ComboItem;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class ConnectionNode {

    private JComboBox<ComboItem> comboBoxOutput;
    private JComboBox<ComboItem> comboBoxInput;

    private String[] comboBoxItems = {"ReVize Server", "Filesystem", "Copy & Paste"};

    private JPanel parentPanel;
    private JButton transformationButton;
    private JLabel outputFormat;
    private JLabel inputFormat;
    private JLabel pathLabel;

    private String inputMode = "";
    private String outputMode = "";
    private File selectedTransformer;

    public ConnectionNode(Timeslot originTimeslot, Timeslot destinationTimeslot) {
        ComboItem comboItemReVize = new ComboItem("ReVize Server", "Value 1");
        ComboItem comboItemFilesystem = new ComboItem("Filesystem", "Value 2");

        comboBoxOutput.addItem(comboItemReVize);
        comboBoxInput.addItem(comboItemReVize);
        comboBoxOutput.addItem(comboItemFilesystem);
        comboBoxInput.addItem(comboItemFilesystem);

        comboBoxOutput.addActionListener (new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Object selectedItem = comboBoxOutput.getSelectedItem();
                if (comboItemReVize.equals(selectedItem))
                {
                    outputMode = "revizeServer";
                    /*for (SourceInSlot sourceInSlot : originTimeslot.getElementList())
                    {
                        sourceInSlot.setDataExchangeChannel("revizeServer");
                    }*/
                }
                else if (comboItemFilesystem.equals(selectedItem))
                {
                    outputMode = "fileSystem";
                    /*for (SourceInSlot sourceInSlot : originTimeslot.getElementList())
                    {
                        sourceInSlot.setDataExchangeChannel("fileSystem");
                    }*/
                }
            }
        });

        comboBoxInput.addActionListener (new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Object selectedItem = comboBoxOutput.getSelectedItem();
                if (comboItemReVize.equals(selectedItem))
                {
                    inputMode = "revizeServer";
                    /*for (SourceInSlot sourceInSlot : originTimeslot.getElementList())
                    {
                        sourceInSlot.setDataExchangeChannel("revizeServer");
                    }*/
                }
                else if (comboItemFilesystem.equals(selectedItem))
                {
                    inputMode = "fileSystem";
                    /*for (SourceInSlot sourceInSlot : originTimeslot.getElementList())
                    {
                        sourceInSlot.setDataExchangeChannel("fileSystem");
                    }*/
                }
            }
        });

        transformationButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                // Import Tool
                FileChooser fileChooser = new FileChooser();
                File importFile = fileChooser.getFileFromChooser();
                if(importFile != null)
                {
                    selectedTransformer = importFile;
                }
                else
                {
                    App.globalLogger.warning("File without Content");
                }
            }
        });
    }

    public JPanel getPanel() {
        return parentPanel;
    }
}
