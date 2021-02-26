import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.io.*;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;

public class Executor
{
	private ExecutorView executorView;
	
	int executionStepNumber = 0;
	
	public Executor()
	{
		executorView = new ExecutorView(this);
	}
	
	public void moveToNextTimeslot() throws IOException {

		// Raise Timestepnumber
		raiseTimestepNumber();

		// Refresh View
		executorView.refreshView();

		// Get Reference
		Timeslot timeslot = getTimeStep(executionStepNumber);

		// Get Data Output from Previous Tool

		// Apply Transformation
		/*for (ConnectionOption connectionOption : App.globalGraph.getConnectionOptions())
		{
			if (connectionOption)
		}

		Runtime runtime = Runtime.getRuntime();
		try
		{
			runtime.exec(new String[]{sourceInSlot.getSourceFile().getPath(), dataSource.getPath()});
		}
		catch(IOException exception)
		{
			exception.printStackTrace();
		}*/

		// Open all tools of the next node
		// createExchangeProcesses(true);
		if(timeslot != null)
		{
			try {
				timeslot.startTools();
			} catch (ParseException | InterruptedException e) {
				e.printStackTrace();
			}
			//if(hasNoValidExchangeProcesses()) timeslot.startTools();
			//else executeToollistWithExchangeProcesses(true);

			// Get Toolsources
			for (SourceInSlot sourceInSlot : timeslot.getElementList())
			{
				if (sourceInSlot.getSourceType().equals("tool"))
				{
					// Get DataExchangeMethod
					switch (sourceInSlot.getDataExchangeChannel())
					{
						// Open Transport Data with ReVize
						case "revizeServer":
							// Start Loading Animation
							startLoading();

							/*// Wait for Access
							String incomingMessage = "";
							long expectedtime = System.currentTimeMillis();
							while(!incomingMessage.equals("ready"))
							{
								while(System.currentTimeMillis() < expectedtime){
									// Empty Loop
								}
								expectedtime += App.globalSettings.getPreloadtimeForTools();
								incomingMessage = ReVizeWebConnector.sendGetStatus(sourceInSlot.toString());
								System.out.println(incomingMessage);
							}

							// Stop Loading Animation
							stopLoading();*/

							// Sent request
							ReVizeWebConnector.sendGET("/update/" + sourceInSlot.toString());

							break;

						// Open Data with Tools
						case "fileSystem":
							break;
					}
				}
			}
		}
		else
		{
			System.out.println(App.globalGraph.getTimeSlotList().size());
			JOptionPane.showMessageDialog(new JFrame(), "Execution failed. Stepnumber is out of Range.", "Error", JOptionPane.ERROR_MESSAGE);
		}

		// Refresh View
		executorView.refreshView();
	}

	private void raiseTimestepNumber() {
		executionStepNumber++;
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
							URL url = ReVizeWebConnector.sendGET("/queue");
							ArrayList<Double> identifierList = GetListFromURL(url);

							// Get Identifier
							double lastIdentifier = GetLastIdentifier(identifierList);
							ReVizeWebConnector.sendGET(s + String.valueOf(lastIdentifier));
						} catch (IOException exception) {
							exception.printStackTrace();
						}

						stopLoading();
					}
				},
				App.globalSettings.getPreloadtimeForTools()
		);
	}*/

	private void startLoading()
	{
		App.executor.getExecutorView().setStatusToLoading();
		executorView.refreshView();
	}

	private void stopLoading()
	{
		App.executor.getExecutorView().setStatusToReady();
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
	
	public void moveToPreviousTimeslot() throws IOException {

		// Lower Timestepnumber
		executionStepNumber--;

		// Get Reference
		Timeslot timeslot = getTimeStep(executionStepNumber);

		// Get Data Output from Previous Tool


		// Apply Transformation


		// Open all tools of the next node
		// createExchangeProcesses(true);
		if(timeslot != null)
		{
			try {
				timeslot.startTools();
			} catch (ParseException | InterruptedException e) {
				e.printStackTrace();
			}
			//if(hasNoValidExchangeProcesses()) timeslot.startTools();
			//else executeToollistWithExchangeProcesses(true);

			// Get Toolsources
			for (SourceInSlot sourceInSlot : timeslot.getElementList())
			{
				if (sourceInSlot.getSourceType().equals("tool"))
				{
					// Get DataExchangeMethod
					switch (sourceInSlot.getDataExchangeChannel())
					{
						// Open Transport Data with ReVize
						case "revizeServer":
							// Start Loading Animation
							startLoading();

							// Wait for Access
							String incomingMessage = "";
							long expectedtime = System.currentTimeMillis();
							while(!incomingMessage.equals("ready"))
							{
								while(System.currentTimeMillis() < expectedtime){
									// Empty Loop
								}
								expectedtime += App.globalSettings.getPreloadtimeForTools();
								incomingMessage = ReVizeWebConnector.sendGetStatus(sourceInSlot.toString());
								System.out.println(incomingMessage);
							}

							// Stop Loading Animation
							stopLoading();

							// Sent request
							ReVizeWebConnector.sendGET("/update/" + sourceInSlot.toString());

							break;

						// Open Data with Tools
						case "fileSystem":
							break;
					}
				}
			}
		}
		else
		{
			System.out.println(App.globalGraph.getTimeSlotList().size());
			JOptionPane.showMessageDialog(new JFrame(), "Execution failed. Stepnumber is out of Range.", "Error", JOptionPane.ERROR_MESSAGE);
		}

		// Refresh View
		executorView.refreshView();
	}
	
	private Timeslot getTimeStep(int stepNumber)
	{
		for(Timeslot timeslotInList : App.globalGraph.getTimeSlotList())
		{
			if(timeslotInList.getSlotNumber() == stepNumber)
			{
				return timeslotInList;
			}
		}
		return null;
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
		App.globalLogger.info(files);
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
