package editorModule;

import main.App;

import javax.swing.*;
import java.awt.*;

public class ConnectionOptionWithIn extends JPanel {

    private JButton addConnectionOptionButton;
    private JButton removeConnectionOptionButton;

    private Timeslot connectedTimeslot;

    public ConnectionOptionWithIn(Timeslot surroundingTimeslot) {

        // Add to List in Surrounding Timeslot
        connectedTimeslot = surroundingTimeslot;
        connectedTimeslot.getCustomConnectorContainer().add(this);

        // Setup Panel
        setBorder(BorderFactory.createLineBorder(Color.black));
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setVisible(App.connectionMode);

        // Add Option To GraphList
        App.globalGraph.getConnectionOptionWithinTimeslots().add(this);

        // Setup Add Button
        addConnectionOptionButton = new ConnectionOptionButton();
        add(addConnectionOptionButton);

        // Setup Remove Button
        removeConnectionOptionButton = new RemoveConnectionOptionButton();
        add(removeConnectionOptionButton);
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

    public void addFunctionality(JPanel panel)
    {
        // Add Action Listener
        addConnectionOptionButton.addActionListener(buttonPressedEvent -> {
            switchVisibilityOfComponent(addConnectionOptionButton);
            switchVisibilityOfComponent(removeConnectionOptionButton);
            connectedTimeslot.getToolSourceList().get(0).changeCustomConnectionStatus(true);
            add(panel);
            App.editor.getEditorView().refreshView();
        });

        removeConnectionOptionButton.addActionListener (buttonPressedEvent -> {
            switchVisibilityOfComponent(addConnectionOptionButton);
            switchVisibilityOfComponent(removeConnectionOptionButton);
            remove(panel);
            App.editor.getEditorView().refreshView();
        });
    }
}
