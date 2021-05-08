package editorModule;

import main.App;

import javax.swing.*;
import java.awt.*;

public class ConnectionOptionOutside extends JPanel{

    private JButton insertConnectionOptionButton;
    private JButton removeConnectionOptionButton;
    private boolean isActivated = false;

    private Timeslot startTimeslot;
    private Timeslot destinationTimeslot;
    
    public ConnectionOptionOutside(Timeslot startTimeslot, Timeslot destinationTimeslot) {
        
        this.startTimeslot = startTimeslot;
        this.destinationTimeslot = destinationTimeslot;
        //startTimeslot.setOutsideConnection(this);
        startTimeslot.addOutsideConnection(this);
        App.globalGraph.getConnectionOptionsBetweenTimeslots().add(this);
        
        refreshView();
    }
    
    public void refreshView()
    {
        // Setup Panel
        setBorder(BorderFactory.createLineBorder(Color.black));
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setVisible(App.connectionMode);
    
        // Setup Add Button
        insertConnectionOptionButton = new ConnectionOptionButton();
        add(insertConnectionOptionButton);
    
        // Setup Remove Button
        removeConnectionOptionButton = new RemoveConnectionOptionButton();
        add(removeConnectionOptionButton);
    
        // Get Input, Output and create ConnectionNode
        ConnectionNode connectionNode = new ConnectionNode(startTimeslot, destinationTimeslot);
    
        // Add Listener
        insertConnectionOptionButton.addActionListener(buttonPressedEvent -> {
            setActivated(true);
            switchVisibilityOfComponent(insertConnectionOptionButton);
            switchVisibilityOfComponent(removeConnectionOptionButton);
            add(connectionNode.getPanel());
            App.editor.getEditorView().refreshView();
            App.globalLogger.info("Add connectionNode between " + startTimeslot.getName() + " and " + destinationTimeslot.getName());
        });
    
        removeConnectionOptionButton.addActionListener (buttonPressedEvent -> {
            setActivated(false);
            switchVisibilityOfComponent(insertConnectionOptionButton);
            switchVisibilityOfComponent(removeConnectionOptionButton);
            remove(connectionNode.getPanel());
        });
    }
    
    void openView(Timeslot originTimeslot, Timeslot destinationTimeslot)
    {
        switchVisibilityOfComponent(insertConnectionOptionButton);
        switchVisibilityOfComponent(removeConnectionOptionButton);
    
        ConnectionNode connectionNode = new ConnectionNode(originTimeslot, destinationTimeslot);
        add(connectionNode.getPanel());
        App.editor.getEditorView().refreshView();
        App.globalLogger.info("Add connectionNode between " + originTimeslot.getName() + " and " + destinationTimeslot.getName());
    }

    private void switchVisibilityOfComponent(JComponent component)
    {
        if (component.isVisible())
        {
            component.setVisible(false);
        }
        else if (!component.isVisible())
        {
            component.setVisible(true);
        }
    }
    
    public boolean isActivated()
    {
        return isActivated;
    }
    
    public void setActivated(boolean activated)
    {
        isActivated = activated;
    }
    
    public JButton getInsertConnectionOptionButton()
    {
        return insertConnectionOptionButton;
    }
    
    public JButton getRemoveConnectionOptionButton()
    {
        return removeConnectionOptionButton;
    }
    
    public void open()
    {
        insertConnectionOptionButton.doClick();
    }
    
    public Timeslot getStartTimeslot()
    {
        return startTimeslot;
    }
    
    public void setStartTimeslot(Timeslot startTimeslot)
    {
        this.startTimeslot = startTimeslot;
    }
    
    public Timeslot getDestinationTimeslot()
    {
        return destinationTimeslot;
    }
    
    public void setDestinationTimeslot(Timeslot destinationTimeslot)
    {
        this.destinationTimeslot = destinationTimeslot;
    }
}
