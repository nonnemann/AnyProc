package dialogs;

import editorModule.ConnectionSettings;
import transferHandling.TableEditorDialog;

import javax.swing.*;
import java.awt.event.*;

public class TransformationDialog extends JDialog
{
	private JPanel _contentPane;
	private JButton _tabularButton;
	private JButton _nonTabularButton;
	private JButton _closeButton;
	private final ConnectionSettings _correspondingNode;
	private final boolean _forward;
	
	public TransformationDialog(ConnectionSettings node, boolean forward)
	{
		_forward = forward;
		_correspondingNode = node;
		setButtonListeners();
		setUpWindow();
	}
	
	private void setUpWindow()
	{
		setContentPane(_contentPane);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		pack();
		setVisible(true);
	}
	
	private void setButtonListeners()
	{
		_tabularButton.addActionListener(new TabularButtonListener());
		_nonTabularButton.addActionListener(new NonTabularButtonListener());
		_closeButton.addActionListener(new CloseButtonListener());
	}
	
	private class TabularButtonListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			dispose();
			new TableEditorDialog(_correspondingNode, _forward);
		}
	}
	
	private static class NonTabularButtonListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
				// TODO: Implement Conversion of Non Tabular Data
		}
	}
	
	private class CloseButtonListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			dispose();
		}
	}
	
}
