import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class SourceInSlot
{
	private transient Timeslot relatedTimeslot;
	private transient SourceViewInSlot sourceView;
	
	private int elementNumber;
	private File sourceFile;
	private String sourceName;
	private String sourceType;
	private ImageIcon sourceIcon;
	private String dataExchangeChannel = "revizeServer";
	private String identifier;

	private boolean usesCustomConnector = false;
	
	public SourceInSlot(Timeslot timeslot, SourceView dragObject)
	{
		relatedTimeslot = timeslot;
		sourceFile = dragObject.getRelatedSource().getSourceFile();
		sourceName = dragObject.getRelatedSource().getSourceName();
		sourceType = dragObject.getRelatedSource().getSourceType();
		sourceIcon = dragObject.getImageIcon();

		identifier = this.toString();
		if (sourceType.equals("tool"))
		{
			registerForRevizeserver();
		}
		
		// Create Modell
		timeslot.getElementList().add(this);
		elementNumber = timeslot.getElementList().size();
		
		// Create View
		sourceView = new SourceViewInSlot(this);
	}
	
	// Setter & Getter
	public Timeslot getRelatedTimeslot()
	{
		return relatedTimeslot;
	}
	
	public void setRelatedTimeslot(Timeslot relatedTimeslot)
	{
		this.relatedTimeslot = relatedTimeslot;
	}
	
	public SourceViewInSlot getSourceView()
	{
		return sourceView;
	}
	
	public void setSourceView(SourceViewInSlot sourceView)
	{
		this.sourceView = sourceView;
	}
	
	public int getElementNumber()
	{
		return elementNumber;
	}
	
	public File getSourceFile()
	{
		return sourceFile;
	}
	
	public String getSourceName()
	{
		return sourceName;
	}
	
	public String getSourceType()
	{
		return sourceType;
	}

	public ImageIcon getSourceIcon() {
		return sourceIcon;
	}

	public String getDataExchangeChannel() {
		return dataExchangeChannel;
	}

	public void setDataExchangeChannel(String channelName)
	{
		dataExchangeChannel = channelName;
	}

	public void registerForRevizeserver() {
		try {
			ReVizeWebConnector.sendGET("/add/" + identifier);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setUsesCustomConnector(boolean usesCustomConnector) {
		this.usesCustomConnector = usesCustomConnector;
	}

	public boolean hasCustomConnector() {
		return usesCustomConnector;
	}
}
