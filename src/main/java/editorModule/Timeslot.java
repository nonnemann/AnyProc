package editorModule;

import dragAndDropHandling.DropPane;
import main.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

import static main.App.globalGraph;

public class Timeslot extends DropPane {

    // UI
    private JPanel mainPanel;
    private JPanel toolContainer;
    private JPanel dataContainer;
    private JLabel topLabel;
    private JPanel customConnectorContainer;

    // Model
    private int slotNumber;
    private ArrayList<ToolSource> toolSourceList = new ArrayList<>();
    private ArrayList<DataSource> dataSourceList = new ArrayList<>();
    private ConnectionOptionOutside outsideConnection;
    private List<ConnectionOptionOutside> outsideConnectionList = new ArrayList<>();

    public Timeslot()
    {
        // Add to Graphmodel
        globalGraph.getTimeSlotList().add(this);

        // Get Slotnumber
        slotNumber = globalGraph.getTimeSlotList().size();
        setName("Timeslot " + slotNumber);

        // Init View()
        add(mainPanel);
        refreshTopLabel();

        // Enable Rightclick Options
        addMouseListener(new RightClickPopUpListener());
    }

    public void addTimeslotToGraph()
    {
        App.editor.getEditorView().getGraphPoolPanel().add(this);
    }

    public void addObjectToTimeslot(Object dragObject)
    {
        if (dragObject.getClass().equals(ToolSource.class))
        {
            ToolSource toolSource = (ToolSource) dragObject;
            toolSource.addMouseListener(new EdgeDrawListener());
            toolSource.addMouseListener(new RightClickPopUpListener());
            toolSource.setRelatedTimeSlot(this);
            toolSource.createConnectionOption();
            toolSource.registerForRevizeserver();
            toolContainer.add(toolSource);
            toolSource.registerForRevizeserver();
            toolSourceList.add(toolSource);

            if(App.debugPrintout)
            {
                App.globalLogger.info("Element " + toolContainer.getSize() + " added to Timeslot " + slotNumber);
            }
        }
        else if (dragObject.getClass().equals(DataSource.class))
        {
            DataSource dataSource = (DataSource) dragObject;
            dataSource.addMouseListener(new EdgeDrawListener());
            dataSource.addMouseListener(new RightClickPopUpListener());
            dataSource.setRelatedTimeSlot(this);
            dataContainer.add(dataSource);
            dataSourceList.add(dataSource);

            if(App.debugPrintout)
            {
                App.globalLogger.info("Element " + dataContainer.getSize() + " added to Timeslot " + slotNumber);
            }
        }
        else
        {
            App.globalLogger.severe("Unexpected Source Type");
        }
    }

    public void resetSlot() {
        for (Source source : toolSourceList)
        {
            source.removeAll();
        }
        for (DataSource dataSource : dataSourceList)
        {
            dataSource.removeAll();
        }
        toolSourceList.clear();
        dataSourceList.clear();
        removeAll();
    }

    // Setter & Getter

    public int getSlotNumber()
    {
        return slotNumber;
    }

    public void setSlotNumber(int slotNumber)
    {
        this.slotNumber = slotNumber;
    }

    public ArrayList<ToolSource> getToolSourceList() {
        return toolSourceList;
    }

    public ArrayList<DataSource> getDataSourceList() {
        return dataSourceList;
    }

    public void refreshTopLabel()
    {
        topLabel.setText(String.valueOf(slotNumber));
    }

    public JPanel getPanel() {
        return mainPanel;
    }

    public JPanel getCustomConnectorContainer() {
        return customConnectorContainer;
    }

    public JPanel getToolContainer() {
        return toolContainer;
    }

    public JPanel getDataContainer() {
        return dataContainer;
    }
    
    public void checkForElements()
    {
        if(toolSourceList.isEmpty() && dataSourceList.isEmpty())
        {
            resetSlot();
            globalGraph.removeTimeslot(this);
        }
    }
    
    public ConnectionOptionOutside getOutsideConnection()
    {
        return outsideConnection;
    }
    
    public void setOutsideConnection(ConnectionOptionOutside outsideConnection)
    {
        this.outsideConnection = outsideConnection;
    }

    public boolean hasConnectionOption() {
        return !outsideConnectionList.isEmpty();
    }

    public void addOutsideConnection(ConnectionOptionOutside connectionOptionOutside) {
        outsideConnectionList.add(connectionOptionOutside);
    }

    public List<ConnectionOptionOutside> getOutsideConnectionList() {
        return outsideConnectionList;
    }
}
