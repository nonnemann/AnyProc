package editorModule;

import main.App;
import main.GlobalGraph;
import main.Source;
import transferHandling.ExchangeProcess;
import transferHandling.ExchangeProcessCreator;
import transferHandling.ExchangeProcessView;
import transferHandling.Transformation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConnectionSlot
{
	private final Timeslot _predecessor;
	private final ConnectionSlotView _connectionSlotView;
	private final Map<ConnectionSettings, List<ExchangeProcess>> _connectionNodes;
	
	private Timeslot _successor; // not final because it does not exist on initialization
	
	public ConnectionSlot(Timeslot timeSlotWithConnectionStart)
	{
		_predecessor = timeSlotWithConnectionStart;

		_connectionSlotView = new ConnectionSlotView(this);
		_connectionSlotView.addViewToGraph();
		_connectionSlotView.setVisible(App.connectionMode);

		GlobalGraph._connectionSlots.add(this);
		_connectionNodes = new HashMap<>();
	}
	
	public void addConnectionNode(ConnectionSettings connectionSettings)
	{
		ConnectionSettingsView connectionNodeView = new ConnectionSettingsView(connectionSettings.getOriginToolName(), connectionSettings.getDestinationToolName(), connectionSettings);
		connectionSettings.setConnectionNodeView(connectionNodeView);
		
		//_connectionSlotView.addConnectionNodeView(connectionNodeView);
		_connectionNodes.put(connectionSettings, new ArrayList<>());
	}
	
	public void generateExchangeProcesses(boolean forward)
	{
		List<ExchangeProcess> exchangeProcesses = new ArrayList<>();
		
		for(ConnectionSettings node : _connectionNodes.keySet())
		{
			_connectionNodes.get(node).clear();
			exchangeProcesses.clear();
			
			List<ExchangeProcessView> views = node.getConnectionNodeView().getExchangeProcessViews(forward);
			for(ExchangeProcessView view : views)
			{
				Source originTool = getTool(node, forward);
				Source destinationTool = getTool(node, !forward);
				exchangeProcesses.add(ExchangeProcessCreator.createExchangeProcess(view, originTool, destinationTool));
			}
			
			_connectionNodes.get(node).addAll(exchangeProcesses);
		}
	}

	private Source getTool(ConnectionSettings node, boolean forward)
	{
		if(forward) return node.getOrigin();
		else return node.getDestination();
	}
	
	public void executeExchangeProcesses(boolean forward)
	{
		for(ConnectionSettings node : _connectionNodes.keySet())
		{
			for(ExchangeProcess exchangeProcess : _connectionNodes.get(node))
			{
				exchangeProcess.generateTransportData();
				
				for(Transformation transformation : node.getTransformations(forward))
					transformation.transformColumn(exchangeProcess.getTransportData().getData());
				
				exchangeProcess.getReuse().reuseTransportData(exchangeProcess);
			}
		}
	}

	public boolean containsNode(ConnectionSettings node)
	{
		return _connectionNodes.containsKey(node);
	}
	
	public boolean hasNoValidExchangeProcesses()
	{
		if(_connectionNodes.size() == 0)
			return true;
		
		for(List<ExchangeProcess> exchangeProcessList : _connectionNodes.values())
			if(exchangeProcessList.size() > 0)
				return false;
		
		return true;
	}
	
	public boolean containsExchangeProcess(ExchangeProcess exchangeProcess)
	{
		for(ConnectionSettings node : _connectionNodes.keySet())
			if(_connectionNodes.get(node).contains(exchangeProcess))
				return true;
		
		return false;
	}
	
	public ConnectionSettings getNodeByExchangeProcess(ExchangeProcess exchangeProcess)
	{
		for(ConnectionSettings node : _connectionNodes.keySet())
			if(_connectionNodes.get(node).contains(exchangeProcess))
				return node;
			
		return null;
	}
	
	
	public Timeslot getPredecessor()
	{
		return _predecessor;
	}
	public ConnectionSlotView getConnectionslotView()
	{
		return _connectionSlotView;
	}
	
	public void setSuccessor(Timeslot successor)
	{
		_successor = successor;
	}
	public Timeslot getSuccessor()
	{
		return _successor;
	}
	public ConnectionSlotView getConnectionSlotView() { return _connectionSlotView;}
}
