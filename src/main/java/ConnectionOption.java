import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConnectionOption extends JPanel{

    private JButton addConnectionSpecificationButton;
    private JButton removeConnectionSpecificationButton;

    public ConnectionOption(Timeslot originTimeslot, Timeslot destinationTimeslot) {

        // Setup Panel
        setBorder(BorderFactory.createLineBorder(Color.black));
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setVisible(false);

        App.globalGraph.getConnectionOptions().add(this);

        // Setup Add Button
        addConnectionSpecificationButton = new JButton();
        ImageIcon imageIcon = new ImageIcon(Settings.PLUS_ICON_PATH);
        IconTransformer iconTransformer = new IconTransformer(imageIcon);
        iconTransformer.scaleIcon(50,50);
        imageIcon = iconTransformer.transformToIcon();
        addConnectionSpecificationButton.setIcon(imageIcon);
        addConnectionSpecificationButton.setPreferredSize(new Dimension(50,50));
        add(addConnectionSpecificationButton);

        // Setup Remove Button
        removeConnectionSpecificationButton = new JButton();
        removeConnectionSpecificationButton.setText("Reset Default");
        removeConnectionSpecificationButton.setVerticalAlignment(SwingConstants.TOP);
        removeConnectionSpecificationButton.setHorizontalAlignment(SwingConstants.RIGHT);
        removeConnectionSpecificationButton.setVisible(false);
        add(removeConnectionSpecificationButton);

        // Get Input, Output and create ConnectionNode
        ConnectionNode connectionNode = new ConnectionNode(originTimeslot, destinationTimeslot);

        // Add Listener
        addConnectionSpecificationButton.addActionListener(buttonPressedEvent -> {
            switchVisibilityOfComponent(addConnectionSpecificationButton);
            switchVisibilityOfComponent(removeConnectionSpecificationButton);
            add(connectionNode.getPanel());
            App.editor.getEditorView().refreshView();
            App.globalLogger.info("Add connectionNode between " + originTimeslot.getName() + " and " + destinationTimeslot.getName());
        });

        removeConnectionSpecificationButton.addActionListener (new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                switchVisibilityOfComponent(addConnectionSpecificationButton);
                switchVisibilityOfComponent(removeConnectionSpecificationButton);
                remove(connectionNode.getPanel());
            }
        });
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
}
