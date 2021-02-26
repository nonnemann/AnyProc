import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class ColumnTransformationView extends JPanel
{
	private final Map<JComboBox<String>, JComboBox<String>> _transformationRules;
	private final TableEditorDialog _parent;
	private JPanel _mainPanel;
	private JComboBox<String> _columnBox;
	private JTextField _newColumnName;
	private JButton _addRuleButton;
	private JPanel _rulePanel;

	public ColumnTransformationView(List<String> columnNames, TableEditorDialog parent)
	{
		_parent = parent;
		initializeComboBox(columnNames);
		_newColumnName.setText("");
		_transformationRules = new HashMap<>();
		_rulePanel.setLayout(new BoxLayout(_rulePanel, BoxLayout.Y_AXIS));
		_addRuleButton.addActionListener(new AddRuleListener());
		
		add(_mainPanel);
		_mainPanel.setLayout(new BoxLayout(_mainPanel, BoxLayout.Y_AXIS));
		_mainPanel.setVisible(true);
	}
	
	private void initializeComboBox(List<String> columnNames)
	{
		for(String columnName : columnNames)
			_columnBox.addItem(columnName);
	}
	
	private class AddRuleListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			JComboBox<String> ruleBox = new JComboBox<>();
			JComboBox<String> transformationBox = new JComboBox<>();
			ruleBox.addItem("Convert color code");
			ruleBox.addItem("Convert time format");
			
			ruleBox.addItemListener(e1 ->
			{
				transformationBox.removeAllItems();
				
				if(e1.getItem().toString().contains("color"))
				{
					transformationBox.addItem("HEX to RBG");
					transformationBox.addItem("RGB to HEX");
					return;
				}
				transformationBox.addItem("To hh");
				transformationBox.addItem("To hh:mm");
				transformationBox.addItem("To hh:mm:ss");
			});
			
			ruleBox.setSelectedIndex(1);
			_rulePanel.add(ruleBox);
			_rulePanel.add(transformationBox);
			_transformationRules.put(ruleBox, transformationBox);
			_parent.pack();
		}
	}
	
	public String getOldColumnName()
	{
		return Objects.requireNonNull(_columnBox.getSelectedItem()).toString();
	}
	
	public String getNewColumnName()
	{
		return _newColumnName.getText();
	}

	public Map<String, String> getTransformations()
	{
		Map<String, String> transformMap = new HashMap<>();
		for(JComboBox<String> box : _transformationRules.keySet())
		{
			String rule = Objects.requireNonNull(box.getSelectedItem()).toString();
			String selection = Objects.requireNonNull(_transformationRules.get(box).getSelectedItem()).toString();
			transformMap.put(rule, selection);
		}
		return transformMap;
	}
}
