import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class TableEditorDialog extends JDialog
{
	private JPanel _contentPane;
	private JButton _applyButton;
	private JButton _cancelButton;
	private JButton _addColumnButton;
	private JPanel _columnContainer;
	private List<String> _columnNames;
	private ConnectionSettings _correspondingNode;
	private List<ColumnTransformationView> _columnTransformationViews;
	private final boolean _forward;
	
	public TableEditorDialog(ConnectionSettings node, boolean forward)
	{
		_forward = forward;
		setUpColumnNames(node);
		setButtonListeners();
		setUpColumnContainer();
		setUpWindow();
	}
	
	private void setUpColumnNames(ConnectionSettings node)
	{
		_correspondingNode = node;
		_columnNames = ColumnExtractor.extractColumnNames(node);
	}
	
	private void setUpWindow()
	{
		setContentPane(_contentPane);
		setModal(true);
		getRootPane().setDefaultButton(_applyButton);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		pack();
		setVisible(true);
	}
	
	private void setButtonListeners()
	{
		_cancelButton.addActionListener(a1 -> dispose());
		
		_applyButton.addActionListener(a1 ->
		{
			TransformationCreator.createTransformations(_columnTransformationViews, _correspondingNode, _forward);
			dispose();
		});
		
		_addColumnButton.addActionListener(a1 ->
		{
			ColumnTransformationView newView = new ColumnTransformationView(_columnNames, this);
			_columnTransformationViews.add(newView);
			_columnContainer.add(newView);
			pack();
		});
	}
	
	private void setUpColumnContainer()
	{
		_columnTransformationViews = new ArrayList<>();
		_columnContainer.setLayout(new BoxLayout(_columnContainer, BoxLayout.X_AXIS));
	}
}
