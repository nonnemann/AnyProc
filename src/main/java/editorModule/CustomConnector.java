package editorModule;

import customConnectors.KnimeConnector;
import main.App;
import main.DataSource;
import main.ToolSource;
import transferHandling.FileChooser;

import javax.swing.*;
import java.io.File;
import java.util.List;

public class CustomConnector {

    private JPanel parentPanel;
    private JTextArea pathTextArea;
    private JButton applyOwnConnectorButton;
    private JButton applyLibraryConnectorButton;
    private JComboBox connectorLibraryCombobox;
    private JPanel successPanel;

    private File connectorFile;
    private ToolSource connectedToolsource;
    private List<DataSource> dataSourceList;

    public CustomConnector(ToolSource toolSource) {

        if (toolSource == null)
        {
            App.globalLogger.severe("Creation of Custom Connector failed. Nullpointer Exception in Toolsource.");
        }
        else
        {
            connectedToolsource = toolSource;
            initializeCustomConnector();
        }
    }

    private void initializeCustomConnector() {

        // Add Listener
        applyOwnConnectorButton.addActionListener(event -> {
            FileChooser fileChooser = new FileChooser();
            connectorFile = fileChooser.getFileFromChooser();
            pathTextArea.setText(connectorFile.getPath());
        });

        applyLibraryConnectorButton.addActionListener(event -> {
            updateDatasourceList(connectedToolsource.getRelatedTimeslot());
            KnimeConnector connector = new KnimeConnector(dataSourceList);
        });
    }

    private void updateDatasourceList(Timeslot timeslot)
    {
        dataSourceList = timeslot.getDataSourceList();
    }

    public JPanel getPanel() {
        return parentPanel;
    }
}
