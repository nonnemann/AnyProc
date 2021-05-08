package executorModule;

import editorModule.ConnectionSlot;
import editorModule.Timeslot;
import main.App;
import main.DataSource;
import main.ToolSource;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import transferHandling.ReVizeWebConnector;
import transferHandling.WindowsLinkParser;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class Executor
{
	private ExecutorView executorView;

	int executionStepNumber = 0;

	public Executor()
	{
		executorView = new ExecutorView(this);
	}

	public void moveToNextTimeslot() throws IOException
	{
		raiseTimestepNumber();
		switchBetweenTools();
	}

	public void moveToPreviousTimeslot() throws IOException
	{
		lowerTimestepNumber();
		switchBetweenTools();
	}

	private void switchBetweenTools() throws IOException {
		// Start Loading Animation
		startLoading();

		// Get References
		Timeslot upcomingTimestep = getTimeStep(executionStepNumber);
		if (upcomingTimestep == null)
		{
			JOptionPane.showMessageDialog(new JFrame(), "Execution failed. Stepnumber is out of Range.", "Error", JOptionPane.ERROR_MESSAGE);
		}
		Timeslot previousTimestep = getTimeStep(executionStepNumber-1);

		// Start Tools
		startTools(upcomingTimestep);

		// Check if Data Exchange Channels are compatible
		String dataInputChannel = "";
		String dataOutputChannel = "";
		for (ToolSource upcomingTool : upcomingTimestep.getToolSourceList())
		{
			dataInputChannel = upcomingTool.getDataExchangeInputChannel();

			if (previousTimestep != null) {
				for (ToolSource previousTool : previousTimestep.getToolSourceList()) {
					dataOutputChannel = previousTool.getDataExchangeOutputChannel();

					if (dataInputChannel.equals(dataOutputChannel)) {
						// Get Data Output from Previous Tool
						executeDataExchange(upcomingTimestep, previousTimestep, dataInputChannel);
					} else {
						// TODO Apply Conversions Automatically
						JOptionPane.showMessageDialog(new JFrame(), "Execution failed. Inputchannel of Tool " + upcomingTool + " is not compatible with Outputchannel of " + previousTool + ". Please apply nessasary conversions.", "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
			else
			{
				App.globalLogger.info("No previous Step to compare dataimport with");
				executeDataExchange(upcomingTimestep, previousTimestep, dataInputChannel);
			}
		}

		// Stop Loading Animation
		stopLoading();
	}

	private void executeDataExchange(Timeslot upcomingTimestep, Timeslot previousTimestep, String dataChannel) throws IOException {

		CompletableFuture.runAsync(() -> {
			try {
				for (ToolSource toolSource : upcomingTimestep.getToolSourceList()) {
					// Get Data from Revize Server
					if (dataChannel.equals("revizeServer")) {
						System.out.println("request update from revize server.");
						// Wait for Access
						String incomingMessage = "";
						long expectedtime = System.currentTimeMillis();
						while (!incomingMessage.equals("ready")) {
							while (System.currentTimeMillis() < expectedtime) {
								// Empty Loop
							}
							expectedtime += App.globalSettings.getPreloadtimeForTools();
							incomingMessage = ReVizeWebConnector.sendGetStatus(String.valueOf(toolSource.getIdentifier()));
							if(App.debugPrintout)
							{
								App.globalLogger.info("Incoming Message from ReVizeserver: " + incomingMessage);
							}
						}

						// Sent request
						ReVizeWebConnector.sendGET("/update/" + String.valueOf(toolSource.getIdentifier()));
					}
					// Get Data from the Filesystem
					else if (dataChannel.equals("Filesystem"))
					{
						// TODO Data Exchange through Filesystem
					}
				}
			} catch (IOException e) {
				// epic fail lol
				System.out.println("could execute data exchange ...");
			}
		});
	}

	public void startTools(Timeslot upcomingTimestep) throws IOException {
		ArrayList<DataSource> dataSources = upcomingTimestep.getDataSourceList();
		ArrayList<ToolSource> toolSources = upcomingTimestep.getToolSourceList();

		if (!toolSources.isEmpty())
		{
			for (ToolSource toolSource : toolSources)
			{
				String toolSourcePath = getRealPath(toolSource.getSourceFile());

				// Open all Data Sources with each Tool
				if (!dataSources.isEmpty()) {

					// TODO Generify KNIME Exception
					if (toolSource.isCustomConnected())
					{
						ProcessBuilder processBuilder = new ProcessBuilder(toolSourcePath, "custom_connectors/workflow.knime");
						processBuilder.command("cmd", "/c", "start","/wait", toolSourcePath, "custom_connectors/workflow.knime");
						Process process = processBuilder.start();
					}

					// TODO Add a check to only use data that is connected to the Tool
					// Use all Data Sources at once
					StringBuilder dataSourcePaths = new StringBuilder();
					for (DataSource dataSource : dataSources) {
						dataSourcePaths.append(getRealPath(dataSource.getSourceFile()) + " ");
					}
					String data = dataSourcePaths.toString();

					// Open the Tool
					openToolWithData(toolSource, data);
				}
				// Otherwise open just the Tool
				else {
					openTool(toolSource);
				}
			}
		}
		// Otherwise just use the default application
		else
		{
			App.globalLogger.warning("No Tool found. Using default settings.Settings to open the Data Source.");
			for (DataSource dataSource : dataSources) {
				try {
					Desktop.getDesktop().open(new File(dataSource.getSourceFile().getPath()));
				} catch (IOException exception) {
					exception.printStackTrace();
				}
			}
		}
	}

	public String getRealPath(File file)
	{
		String realFilePath = "";
		try {
			// Check if Path is MS Windows Links
			if (file.getName().toLowerCase().endsWith(".lnk"))
			{
				WindowsLinkParser windowsLinkParser = new WindowsLinkParser(file);
				realFilePath = windowsLinkParser.getRealFilename();
			}
			// Otherwise return the Normal Path
			else
			{
				realFilePath = file.getPath();
			}
		}
		catch (IOException | ParseException exception) {
			exception.printStackTrace();
		}
		return realFilePath;
	}

	private void openToolWithData(ToolSource toolSource, String dataPathString) {

		Path toolSourcePath = Paths.get(toolSource.getSourcePath());
		boolean isOpened = false;
		int openMethodCounter = 1;

		while (!isOpened)
		{
			// If Path is a Websource
			if (toolSourcePath.getFileName().endsWith(".url") || toolSourcePath.getFileName().endsWith(".html")) {
				// Try to Open in Default Browser
				isOpened = openWebsourceInDefaultBrowser(toolSourcePath, isOpened);

				//TODO Add Webttools to Toolpool for better coordination
				/*System.setProperty("webdriver.chrome.driver","selenium_modules/chromedriver.exe");
				WebDriver driver = new ChromeDriver();
				driver.manage().window().maximize();
				String baseUrl = toolSource.getSourceFile().toURI().toURL().toString();
				driver.get(baseUrl);
				driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL +"n");*/
			}
			// Else assume native Application
			else {
				switch (openMethodCounter) {
					// Use the Processbuilder
					case 1:
						try {
							isOpened = startToolWithProcessbuilder(toolSource, dataPathString);
						} catch (IOException exception) {
							exception.printStackTrace();
						}
						break;
					// Use Runtime Execution
					case 2:
						try {
							isOpened = startToolWithRuntimeExecution(toolSourcePath, dataPathString);
						} catch (IOException e) {
							e.printStackTrace();
						}
						break;
					// Use Default System settings.Settings
					default:
						try {
							isOpened = startToolWithSystemsettings(toolSourcePath, dataPathString);
						} catch (IOException e) {
							e.printStackTrace();
						}
						break;
				}
			}
			openMethodCounter++;
		}
	}

	private void openTool(ToolSource toolSource){

		Path path = Paths.get(toolSource.getSourcePath());
		boolean isOpened = false;
		int openMethodCounter = 1;

		while (!isOpened)
		{
			// If Path is a Websource
			if (path.getFileName().endsWith(".url") || path.getFileName().endsWith(".html")) {
				// Try to Open in Default Browser
				isOpened = openWebsourceInDefaultBrowser(path, isOpened);

				//TODO Add Webttools to Toolpool for better coordination
				/*System.setProperty("webdriver.chrome.driver","selenium_modules/chromedriver.exe");
				WebDriver driver = new ChromeDriver();
				driver.manage().window().maximize();
				String baseUrl = toolSource.getSourceFile().toURI().toURL().toString();
				driver.get(baseUrl);
				driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL +"n");*/
			}
			// Else assume native Application
			else {
				switch (openMethodCounter) {
					// Use the Processbuilder
					case 1:
						try {
							isOpened = startToolWithProcessbuilder(toolSource);
						} catch (IOException exception) {
							exception.printStackTrace();
						}
						break;
					// Use Runtime Execution
					case 3:
						try {
							isOpened = startToolWithRuntimeExecution(path);
						} catch (IOException e) {
							e.printStackTrace();
						}
						break;
					// Use Default System settings.Settings
					default:
						try {
							isOpened = startToolWithSystemsettings(path);
						} catch (IOException e) {
							e.printStackTrace();
						}
						break;
				}
			}
			openMethodCounter++;
		}
	}

	private void readProcessActions(Process process) throws IOException {
		InputStream inputStream = process.getInputStream();
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		String line;
		while ((line = bufferedReader.readLine()) != null) {
			System.out.println(line);
		}
	}

	private boolean startToolWithProcessbuilder(ToolSource toolSource) throws IOException {
		return startToolWithProcessbuilder(toolSource, "");
	}

	private boolean startToolWithProcessbuilder(ToolSource toolSource, String args) throws IOException {
		boolean isOpened;

		String [] command = {"rundll32.exe", "url.dll,FileProtocolHandler", toolSource.getSourceFile().getName(), args};
		ProcessBuilder processBuilder = new ProcessBuilder(command);
		Path toolPath = Paths.get(toolSource.getSourcePath());
		String directory = String.valueOf(toolPath.getParent());
		processBuilder.directory(new File(directory));
		try {
			Process process = processBuilder.start();
			readProcessActions(process);
			App.globalLogger.info("Started Tool with Process Builder");

			int exitCode = process.waitFor();
			App.globalLogger.warning("Exited with error code : " + exitCode);

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		isOpened = true;
		return isOpened;
	}

	private boolean startToolWithSystemsettings(Path toolPath) throws IOException {
		return startToolWithSystemsettings(toolPath, "");
	}

	private boolean startToolWithSystemsettings(Path toolPath, String args) throws IOException {
		boolean isOpened;
		Desktop.getDesktop().open(new File(toolPath.toString(), args));
		isOpened = true;
		App.globalLogger.info("Started Tool with Default settings.Settings");
		return isOpened;
	}

	private boolean startToolWithRuntimeExecution(Path toolPath) throws IOException {
		return startToolWithRuntimeExecution(toolPath, "");
	}

	private boolean startToolWithRuntimeExecution(Path toolPath, String args) throws IOException {
		boolean isOpened;
		Runtime runtime = Runtime.getRuntime();
		runtime.exec(new String[]{toolPath.toString(), args});

		isOpened = true;
		App.globalLogger.info("Started Tool with Runtime Execution");
		return isOpened;
	}

	private boolean openWebsourceInDefaultBrowser(Path path, boolean isOpened) {
		try {
			Desktop.getDesktop().browse(path.toUri());
			isOpened = true;
			App.globalLogger.info("Started Tool with Default Browser");
		} catch (IOException exception) {
			exception.printStackTrace();
		}
		return isOpened;
	}

	private Timeslot getTimeStep(int stepNumber)
	{
		Timeslot timeslotInList = null;
		for(Timeslot timeslotIterator : App.globalGraph.getTimeSlotList())
		{
			if(timeslotIterator != null && timeslotIterator.getSlotNumber() == stepNumber)
			{
				timeslotInList = timeslotIterator;
			}
			else
			{
				App.globalLogger.warning("Timestep is empty");
			}
		}
		return timeslotInList;
	}

	private void raiseTimestepNumber() {
		executionStepNumber++;
	}

	private void lowerTimestepNumber() {
		executionStepNumber--;
	}

	/*private void useRevizeChannel(String s) {
		// Wait for Tools to Load
		startLoading();
		new java.util.Timer().schedule(
				new java.util.TimerTask() {
					@Override
					public void run() {
						try {
							// Get Data from Server
							URL url = transferHandling.ReVizeWebConnector.sendGET("/queue");
							ArrayList<Double> identifierList = GetListFromURL(url);

							// Get Identifier
							double lastIdentifier = GetLastIdentifier(identifierList);
							transferHandling.ReVizeWebConnector.sendGET(s + String.valueOf(lastIdentifier));
						} catch (IOException exception) {
							exception.printStackTrace();
						}

						stopLoading();
					}
				},
				main.App.globalSettings.getPreloadtimeForTools()
		);
	}*/

	private void startLoading()
	{
		App.executor.getExecutorView().startLoadingAnimation();
		executorView.refreshView();
	}

	private void stopLoading()
	{
		App.executor.getExecutorView().stopLoadingAnimation();
		executorView.refreshView();
	}

	private static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}

	private ArrayList<Double> GetListFromURL(URL url) throws IOException {
		ArrayList<Double> arrayList = new ArrayList<>();

		BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
		String jsonText = readAll(in);

		try {
			JSONObject json = new JSONObject(jsonText);
			JSONArray queue = json.getJSONArray("queue");
			for (int i = 0; i < queue.length(); i++) {
				Double nextID = queue.getDouble(i);
				arrayList.add(nextID);
			}
			App.globalLogger.info(String.valueOf(queue));
		} catch(JSONException e) {
			e.printStackTrace();
		}

		in.close();
		return arrayList;
	}

	private Double GetLastIdentifier(ArrayList<Double> identifierList) {
		return (double) identifierList.get(identifierList.size()-1);
	}

	private void executeToollistWithExchangeProcesses(boolean forward)
	{
		findCurrentConnectionslot().executeExchangeProcesses(forward);
	}

	private void executeToollistWithExchangeProcesses(boolean forward, Timeslot timeslot)
	{
		findCurrentConnectionslot(timeslot).executeExchangeProcesses(forward);
	}

	private ConnectionSlot findCurrentConnectionslot()
	{
		Timeslot predecessor = getTimeStep(executionStepNumber - 1);
		return App.globalGraph.findConnectionslotByPredecessor(predecessor);
	}

	private ConnectionSlot findCurrentConnectionslot(Timeslot timeslot)
	{
		return App.globalGraph.findConnectionslotByPredecessor(timeslot);
	}

	private boolean hasNoValidExchangeProcesses()
	{
		if(findCurrentConnectionslot() == null) return true;

		return findCurrentConnectionslot().hasNoValidExchangeProcesses();
	}

	private boolean hasNoValidExchangeProcesses(Timeslot timeslot)
	{
		return findCurrentConnectionslot(timeslot).hasNoValidExchangeProcesses();
	}

	private void createExchangeProcesses(boolean forward)
	{
		if(executionStepNumber == 1) return;
		findCurrentConnectionslot().generateExchangeProcesses(forward);
	}


	// TODO Try to get the altered output of a VA tool
	/*private void copyRecentlyUsedFileToOut()
	{
		try
		{
			String recentlyChangedFilesDirectory = System.getenv("APPDATA") + "/Microsoft/Windows/Recent/";
			String copyDirectory = System.getProperty("user.dir") + "./out/temp/";
			FileUtils.copyFile(getLastModifiedFile(recentlyChangedFilesDirectory), new File(copyDirectory + getLastModifiedFile(recentlyChangedFilesDirectory).getName()));
		}
		catch(IOException exception)
		{
			exception.printStackTrace();
		}
	}
	
	public File getLastModifiedFile(String dirPath){
		File dir = new File(dirPath);
		File[] files = dir.listFiles();
		main.App.globalLogger.info(files);
		if (files == null || files.length == 0) {
			return null;
		}
		
		File lastModifiedFile = files[0];
		for (int i = 1; i < files.length; i++) {
			if (lastModifiedFile.lastModified() < files[i].lastModified()) {
				lastModifiedFile = files[i];
			}
		}
		return lastModifiedFile;
	}*/

	public void resetProgress() {
		setExecutionStepNumber(0);
		App.executor.getExecutorView().refreshView();
	}
	
	// Setter & Getter
	public ExecutorView getExecutorView()
	{
		return executorView;
	}
	
	public int getExecutionStepNumber()
	{
		return executionStepNumber;
	}

	public void setExecutionStepNumber(int executionStepNumber) {
		this.executionStepNumber = executionStepNumber;
	}
}
