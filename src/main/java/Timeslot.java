import org.jetbrains.annotations.NotNull;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class Timeslot extends DropPane
{
	private JLabel topLabel;
	private int slotNumber;
	private List<SourceInSlot> elementList = new ArrayList<>();
	
	public Timeslot()
	{
		App.globalGraph.getTimeSlotList().add(this);
		slotNumber = App.globalGraph.getTimeSlotList().size();

		// Set Styling Rules
		setBorder(BorderFactory.createLineBorder(Color.black));
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setName("Timeslot " + slotNumber);
		topLabel = new JLabel(String.valueOf(slotNumber));
		add(topLabel, BorderLayout.NORTH);

		// Enable Rightclick Options
		addMouseListener(new RightClickPopUpListener());
	}
	
	public void addObjectToTimeslot(Object dragObject)
	{
		SourceInSlot element = new SourceInSlot(this, (SourceView) dragObject);
		add(element.getSourceView());
	}

	public void startTools() throws IOException, ParseException, InterruptedException {
		ArrayList<SourceInSlot> dataSources = getDataSourcesFromElementList();
		ArrayList<SourceInSlot> toolSources = GetToolSourcesFromElementList();

		if (!toolSources.isEmpty())
		{
			// Open all Data Sources with connected Tools
			for (SourceInSlot toolSource : toolSources)
			{
				// Get ToolsourcePath
				String toolSourcePath = "";
				if (toolSource.getSourceFile().getName().toLowerCase().endsWith(".lnk"))
				{
					WindowsLinkParser windowsLinkParser = new WindowsLinkParser(toolSource.getSourceFile());
					toolSourcePath = windowsLinkParser.getRealFilename();
				}
				else
				{
					toolSourcePath = toolSource.getSourceFile().getCanonicalPath();
				}

				// Check if there are Data Sources
				if (!dataSources.isEmpty()) {

					// TODO Generify KNIME Exception
					if (toolSource.hasCustomConnector())
					{
						ProcessBuilder processBuilder = new ProcessBuilder(toolSourcePath, "savesfiles/workflow.knime");
						processBuilder.command("cmd", "/c", "start","/wait", toolSourcePath, "savesfiles/workflow.knime");
						Process process = processBuilder.start();
					}

					// Open Data Sources with Tool
					StringBuilder dataSourcePaths = new StringBuilder();
					for (SourceInSlot dataSource : dataSources) {
						// Get DatasourcePath
						String dataSourcePath = "";
						if (dataSource.getSourceFile().getName().toLowerCase().endsWith(".lnk"))
						{
							WindowsLinkParser windowsLinkParser = new WindowsLinkParser(dataSource.getSourceFile());
							dataSourcePath = windowsLinkParser.getRealFilename();
							dataSourcePaths.append(dataSourcePath + " ");
						}
						else
						{
							dataSourcePath = dataSource.getSourceFile().getCanonicalPath();
							dataSourcePaths.append(dataSourcePath + " ");
						}
					}

					Runtime runtime = Runtime.getRuntime();
					try
					{
						runtime.exec(new String[]{toolSourcePath, String.valueOf(dataSourcePaths)});
					}
					catch(IOException exception)
					{
						exception.printStackTrace();
					}

					//Process processBuilder = new ProcessBuilder("cmd", "/c", "start","/wait", toolSourcePath, dataSourcePaths.toString()).start();
					/*processBuilder.command("cmd", "/c", "start","/wait", toolSourcePath, dataSourcePaths.toString());
					Process process = processBuilder.start();*/
				}
				// Otherwise open just the tool
				else {
					try {
						if (toolSource.getSourceFile().getName().toLowerCase().endsWith(".lnk"))
						{
							WindowsLinkParser windowsLinkParser = new WindowsLinkParser(toolSource.getSourceFile());
							Process process = new ProcessBuilder(windowsLinkParser.getRealFilename()).start();
						}
						else if (toolSource.getSourceFile().getName().toLowerCase().endsWith(".url"))
						{
							Desktop.getDesktop().browse(toolSource.getSourceFile().toURI());

							//TODO Add Webttools to Toolpool for better coordination
							/*System.setProperty("webdriver.chrome.driver","selenium_modules/chromedriver.exe");
							WebDriver driver = new ChromeDriver();
							driver.manage().window().maximize();
							String baseUrl = toolSource.getSourceFile().toURI().toURL().toString();
							driver.get(baseUrl);
							driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL +"n");*/
						}
						else if (toolSource.getSourceFile().getName().toLowerCase().endsWith(".html"))
						{
							Desktop.getDesktop().open(new File(toolSource.getSourceFile().getPath()));
						}
						else
						{
							System.out.println("Exeception");
							//Process process= new ProcessBuilder(toolSource.getSourceFile().getPath()).start();

							System.out.println(toolSource.getSourceFile().getPath());

							Process processBuilder = new ProcessBuilder(toolSource.getSourceFile().getPath()).start();
							/*Runtime runtime = Runtime.getRuntime();
							try
							{
								runtime.exec(new String[]{toolSource.getSourceFile().getPath()});
							}
							catch(IOException exception)
							{
								exception.printStackTrace();
							}*/
						}
					} catch (Exception exception) {
						exception.printStackTrace();
					}
				}
			}
		}
		// Otherwise just use the default application
		else
		{
			System.out.println("Exeception here");
			/*for (SourceInSlot dataSource : dataSources) {
				try {
					Desktop.getDesktop().open(new File(dataSource.getSourceFile().getPath()));
				} catch (IOException exception) {
					exception.printStackTrace();
				}
			}*/
		}
	}

	@NotNull
	private ArrayList<SourceInSlot> GetToolSourcesFromElementList() {
		ArrayList<SourceInSlot> toolSources = new ArrayList();
		for(SourceInSlot sourceInSlot : elementList)
		{
			if (sourceInSlot.getSourceType().equals("tool"))
			{
				toolSources.add(sourceInSlot);
			}
		}
		return toolSources;
	}

	@NotNull
	private ArrayList<SourceInSlot> getDataSourcesFromElementList() {
		ArrayList<SourceInSlot> dataSources = new ArrayList();
		for(SourceInSlot sourceInSlot : elementList)
		{
			if (sourceInSlot.getSourceType().equals("data"))
			{
				dataSources.add(sourceInSlot);
			}
		}
		return dataSources;
	}
	
	public int getIndexOfSource(SourceInSlot source)
	{
		return elementList.indexOf(source);
	}
	public SourceInSlot getSourceAt(int index)
	{
		try
		{
			return elementList.get(index);
		}
		catch(IndexOutOfBoundsException exception)
		{
			exception.printStackTrace();
			App.globalLogger.severe("No Data Source defined");
			return null;
		}
	}
	public void addToGraph()
	{
		App.editor.getEditorView().getGraphPoolPanel().add(this);
	}

	public void refreshTopLabel()
	{
		topLabel.setText(String.valueOf(slotNumber));
	}
	
	// Setter & Getter
	
	public int getSlotNumber()
	{
		return slotNumber;
	}
	
	public void setSlotNumber(int slotNumber)
	{
		this.slotNumber = slotNumber;
	}
	
	public List<SourceInSlot> getElementList()
	{
		return elementList;
	}
	
	public boolean containsSourceInSlot(SourceInSlot sourceInSlot)
	{
		return elementList.contains(sourceInSlot);
	}
}
