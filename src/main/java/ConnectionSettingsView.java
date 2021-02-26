import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ConnectionSettingsView extends JPanel
{
    private JLabel _topLabel;
    private JLabel _bottomLabel;
    private JButton _topAddConnectionButton;
    private JButton _bottomAddConnectionButton;
    private JPanel _topConnections;
    private JPanel _bottomConnections;
    private JPanel _parentPanel;
    private JButton _topTransformButton;
    private JButton _bottomTransformButton;
    private final List<ExchangeProcessView> _topProcesses = new ArrayList<>();
    private final List<ExchangeProcessView> _bottomProcesses = new ArrayList<>();
    private final ConnectionSettings _correspondingNode;
    
    public ConnectionSettingsView(String originToolName, String destinationToolName, ConnectionSettings node)
    {
        _correspondingNode = node;
        
        setLabels(originToolName, destinationToolName);
        setButtonListeners();
        initConnectionPanels();
        add(_parentPanel);
        setVisible(true);
    }
    
    private void setLabels(String originToolName, String destinationToolName)
    {
        _topLabel.setText(originToolName + " to " + destinationToolName);
        _bottomLabel.setText(destinationToolName + " to " + originToolName);
    }
    
    private void setButtonListeners()
    {
        _topAddConnectionButton.addActionListener(new AddConnectionListener(true));
        _bottomAddConnectionButton.addActionListener(new AddConnectionListener(false));
    
        _topTransformButton.addActionListener(a1 -> new TransformationDialog(_correspondingNode, true));
        _bottomTransformButton.addActionListener(a1 -> new TransformationDialog(_correspondingNode, false));
    }
    
    private void initConnectionPanels()
    {
        _topConnections.setLayout(new BoxLayout(_topConnections, BoxLayout.Y_AXIS));
        _bottomConnections.setLayout(new BoxLayout(_bottomConnections, BoxLayout.Y_AXIS));
    }
    
    public List<ExchangeProcessView> getExchangeProcessViews(boolean forward)
    {
        if(forward)
            return _topProcesses;
        else
            return _bottomProcesses;
    }
    
    private class AddConnectionListener implements ActionListener
    {
        private final boolean _forward;
        
        public AddConnectionListener(boolean forward)
        {
            _forward = forward;
        }
        
        @Override
        public void actionPerformed(ActionEvent e)
        {
            ExchangeProcessView newExchangeProcessView = new ExchangeProcessView();
            if(_forward)
            {
                _topProcesses.add(newExchangeProcessView);
                _topConnections.add(newExchangeProcessView);
            }
            else
            {
                _bottomProcesses.add(newExchangeProcessView);
                _bottomConnections.add(newExchangeProcessView);
            }
        }
    }
    
}
