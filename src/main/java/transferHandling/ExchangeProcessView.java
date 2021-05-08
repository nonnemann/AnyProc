package transferHandling;

import javax.swing.*;
import java.util.Objects;

public class ExchangeProcessView extends JPanel
{
    private JComboBox<String> _actionBox;
    private JComboBox<String> _scopeBox;
    private JComboBox<String> _quantityBox;
    private JComboBox<String> _reuseBox;
	private JPanel _parentPanel;
	
	public ExchangeProcessView()
    {
        add(_parentPanel);
        setVisible(true);
    }

    public String getAction()
    {
        return Objects.requireNonNull(_actionBox.getSelectedItem()).toString();
    }
    public String getScope()
    {
        return Objects.requireNonNull(_scopeBox.getSelectedItem()).toString();
    }
    public String getQuantity()
    {
        return Objects.requireNonNull(_quantityBox.getSelectedItem()).toString();
    }
    public String getReuse()
    {
        return Objects.requireNonNull(_reuseBox.getSelectedItem()).toString();
    }
}
