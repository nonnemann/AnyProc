import javax.swing.*;
import java.awt.*;

public class SourceViewInSlot extends DragPane
{
	private SourceInSlot relatedSource;
	private JLabel titleLabel;
	private String sourceName;
	private String sourceType;

	private JButton customConnectorButton;
	
	public SourceViewInSlot(SourceInSlot sourceInSlot)
	{
		// Get Reference
		importReferences(sourceInSlot);
		setName("Element" + relatedSource.getElementNumber() + "inSlot" + relatedSource.getRelatedTimeslot().getSlotNumber());
		
		// Set Styling Rules
		createView();
		
		// Enable Rightclick Options
		addMouseListener(new RightClickPopUpListener());
		addMouseListener(new EdgeDrawListener());
	}
	
	private void importReferences(SourceInSlot sourceInSlot)
	{
		relatedSource = sourceInSlot;
		sourceName = sourceInSlot.getSourceName();
		sourceType = sourceInSlot.getSourceType();
	}
	
	private void createView()
	{
		// Stylize Settings
		setPreferredSize(new Dimension(120, 120));

		// Add Title to View
		titleLabel = new JLabel(sourceName, relatedSource.getSourceIcon(), JLabel.CENTER);
		titleLabel.setHorizontalTextPosition(JLabel.CENTER);
		titleLabel.setVerticalTextPosition(JLabel.BOTTOM);
		add(titleLabel);

		// Add Button to View
		customConnectorButton = new JButton("Add Connector");
		customConnectorButton.setSize(100, getSize().height);
		if (relatedSource.getSourceType().equals("tool")) {
			add(customConnectorButton);
		}
		customConnectorButton.addActionListener(actionEvent -> {
			KnimeConnector knimeConnector = new KnimeConnector();
			for (SourceInSlot sourceInSlot : relatedSource.getRelatedTimeslot().getElementList())
			{
				if (sourceInSlot.getSourceType().equals("data"))
				{
					knimeConnector.getDataSources().add(sourceInSlot);
				}
			}
			knimeConnector.generateWorkflowAndDirectories();
			relatedSource.setUsesCustomConnector(true);
		});
	}
	
	// Setter & Getter
	public SourceInSlot getRelatedSource()
	{
		return relatedSource;
	}
	
	public void addViewToSlot()
	{
		relatedSource.getRelatedTimeslot().add(this);
	}
}
