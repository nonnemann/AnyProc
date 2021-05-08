package main;

import editorModule.*;
import transferHandling.ExchangeProcess;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GlobalGraph
{
    private String graphName;
    private List<Timeslot> timeSlotList = new ArrayList<>();
    private List<Edge> edgeList = new ArrayList<>();
    public static List<ConnectionSlot> _connectionSlots = new ArrayList<>();
    private List<ConnectionSlot> connectionNodes = new ArrayList<>();
    private List<ConnectionOptionOutside> connectionOptionsBetweenTimestepOutsides = new ArrayList<>();
    private List<ConnectionOptionWithIn> connectionOptionWithinTimesteps = new ArrayList<>();
    
    public GlobalGraph(String name)
    {
        graphName = name;
    }
    
    public void resetGraph()
    {
        for(Timeslot timeslot : timeSlotList)
        {
            timeslot.resetSlot();
            for(ToolSource toolSource : timeslot.getToolSourceList())
            {
                toolSource.removeAll();
            }
            timeslot.getToolSourceList().clear();
            for(DataSource dataSource : timeslot.getDataSourceList())
            {
                dataSource.removeAll();
            }
            timeslot.getDataSourceList().clear();
        }
        timeSlotList.clear();
        edgeList.clear();
        
        App.editor.getEditorView().getGraphPoolPanel().removeAll();
        App.editor.getEditorView().refreshView();
    }
    
    public void addObjectToNewTimeslot(Object dragObject)
    {
        Timeslot timeslot = createNewTimeslot();
        timeslot.addObjectToTimeslot(dragObject);
        App.editor.getEditorView().refreshView();
    }
    
    private Timeslot createNewTimeslot()
    {
        Timeslot newTimeslot = new Timeslot();
        newTimeslot.addTimeslotToGraph();
        
        if(_connectionSlots.size() >= 1)
        {
            _connectionSlots.get(_connectionSlots.size() - 1).setSuccessor(newTimeslot);
        }
        
        if(App.debugPrintout)
        {
            App.globalLogger.info("Timeslot " + timeSlotList.size() + " added");
        }

        System.out.println(timeSlotList.size());

        if (timeSlotList.size() >= 2)
        {
            createConnectionSlot(newTimeslot);
        }
        
        return newTimeslot;
    }

    private ConnectionOptionOutside createConnectionSlot(Timeslot destinationTimeslot) {
        int previousTimeslotPosition = timeSlotList.size() - 2;
        Timeslot startTimeslot = timeSlotList.get(previousTimeslotPosition);
        ConnectionOptionOutside connectionOptionOutside = new ConnectionOptionOutside(startTimeslot, destinationTimeslot);
        switchComponentsInGraphPool(destinationTimeslot, connectionOptionOutside);
        App.editor.getEditorView().refreshView();
        return connectionOptionOutside;
    }
    
    private ConnectionOptionOutside recreateConnectionSlot(ConnectionOptionOutside connectionOption) {
        int startIndex = connectionOption.getStartTimeslot().getSlotNumber();
        int destinationIndex = connectionOption.getDestinationTimeslot().getSlotNumber();
        if(Math.abs(startIndex-destinationIndex) == 1)
        {
            App.editor.getEditorView().getGraphPoolPanel().add(connectionOption);
            connectionOption.refreshView();
            if(connectionOption.isActivated())
            {
                connectionOption.open();
            }
        }
        /*Timeslot startTimeslot = timeSlotList.get(timeSlotList.size() - 1);
        ConnectionOptionOutside connectionOptionOutside = new ConnectionOptionOutside(startTimeslot, destinationTimeslot);
        switchComponentsInGraphPool(destinationTimeslot, connectionOptionOutside);
        for(ConnectionOptionOutside connectionOption : connectionOptionsBetweenTimestepOutsides)
        {
            if(connectionOption == startTimeslot.getOutsideConnection() && connectionOption != connectionOptionOutside)
            {
                connectionOptionOutside.open();
            }
        }
        App.editor.getEditorView().refreshView();*/
        return connectionOption;
    }

    private void switchComponentsInGraphPool(Component currentComponent, Component replacementComponent) {
        App.editor.getEditorView().getGraphPoolPanel().remove(currentComponent);
        App.editor.getEditorView().getGraphPoolPanel().add(replacementComponent);
        App.editor.getEditorView().getGraphPoolPanel().add(currentComponent);
    }

    private void createNewConnectionslot(Timeslot timeslot)
    {
        ConnectionSlot connectionslot =  new ConnectionSlot(timeslot);
        _connectionSlots.add(connectionslot);
    
        addConnectionslotToGraph(connectionslot);
    }
    
    private void addConnectionslotToGraph(ConnectionSlot connectionslot)
    {
        App.editor.getEditorView().getGraphPoolPanel().add(connectionslot.getConnectionslotView());
        App.editor.getEditorView().refreshView();
    }
    
    public void addObjectToExistingTimeslot(Object object, Component component)
    {
        Timeslot timeslot = getTimeslotFromList(component);
        timeslot.addObjectToTimeslot((Source) object);
        App.editor.getEditorView().refreshView();
    }
    
    private Timeslot getTimeslotFromList(Component component)
    {
        for(Timeslot timeslot : timeSlotList)
        {
            if(timeslot == component)
            {
                return timeslot;
            }
        }
        App.globalLogger.severe("Component is not a Timeslot");
        return null;
    }
    
    public void removeTimeslot(Timeslot timeslot)
    {
        // Get related timeslot
        if(timeSlotList.contains(timeslot))
        {
            // Remove all element
            timeslot.getToolSourceList().clear();
            timeslot.getDataSourceList().clear();
            timeslot.removeAll();
    
            // Remove timeslot
            App.editor.getEditorView().getGraphPoolPanel().remove(timeslot);
            timeSlotList.remove(timeslot);
            
            // Reset slot Numbers
            resetSlotNumbers();
    
            // Refresh View
            App.editor.getEditorView().refreshView();
        }
    }
    
    public void removeSource(Source source)
    {
        // Clear View
        source.removeAll();
        
        // TODO Delete Source from Timeslot or Timeslot (if needed)
        /*Timeslot relatedTimeslot = source.getRelatedTimeslot();
        relatedTimeslot.getElementList().remove(source);
        relatedTimeslot.remove(source.getSource());
        if(relatedTimeslot.getElementList().isEmpty())
        {
            removeTimeslot(relatedTimeslot);
        }*/
        
        // Refresh View
        App.editor.getEditorView().refreshView();
    }
    
    private void resetSlotNumbers()
    {
        int slotNumber = 0;
        for(Timeslot timeslot : timeSlotList)
        {
            slotNumber++;
            timeslot.setSlotNumber(slotNumber);
            timeslot.setName("Timeslot " + slotNumber);
            timeslot.refreshTopLabel();
        }
    }
    
    public void changePosition(Timeslot timeslot, int position)
    {
        // Change Position in Timeslotlist
        timeSlotList.remove(timeslot);
        timeSlotList.add(position-1, timeslot);

        // Reset slotNumbers in Timeslotlist
        resetSlotNumbers();

        // Remove everything in GraphPool
        App.editor.getEditorView().getGraphPoolPanel().removeAll();

        // Recreate GraphPool from Lists
        recreateGraph();
        
        // Refresh View
        App.editor.getEditorView().refreshView();
    }

    private void recreateGraph() {
        // Recreate Timeslots
        for(Timeslot timeslot : timeSlotList)
        {
            timeslot.addTimeslotToGraph();
            // Add Toolsources
            for(ToolSource toolSource : timeslot.getToolSourceList())
            {
                timeslot.getToolContainer().add(toolSource);
            }
            // Add Datasources
            for(DataSource dataSource : timeslot.getDataSourceList())
            {
                timeslot.getDataContainer().add(dataSource);
            }

            if (timeslot.hasConnectionOption())
            {
                if (timeslot.getSlotNumber() < timeSlotList.size())
                {
                    for (ConnectionOptionOutside connectionOption : timeslot.getOutsideConnectionList())
                    {
                        App.editor.getEditorView().getGraphPoolPanel().add(connectionOption);
                    }
                }
            }
            else
            {
                App.editor.getEditorView().getGraphPoolPanel().add(new ConnectionOptionOutside(timeslot, null));
            }
        }
    }

    /*private void recreateGraphViewFromGraphModel()
    {
        for(Timeslot timeslot : timeSlotList)
        {
            timeslot.setTimeslotView(new Timeslot(timeslot));
            for(Source source : timeslot.getElementList())
            {
                source.setSource(new Source(source));
            }
        }

        // Refresh View
        main.App.editor.getEditorView().refreshView();
    }*/

    /*public void loadViewFromModel()
    {
        for(Timeslot timeslot : timeSlotList)
        {
            timeslot.setTimeslotView(new Timeslot(timeslot));
            timeslot.getTimeslotView().addViewToGraph();
            for(Source Source : timeslot.getElementList())
            {
                Source.setRelatedTimeslot(timeslot);
                Source.setSource(new Source(Source));
                Source.getSource().addViewToSlot();
            }
        }
        // Refresh View
        main.App.editor.getEditorView().refreshView();
    }*/

    public void createConnectionSlot(Edge edge)
    {
        Source startSource = ((Source) edge.getStartComponent());
        Source endSource = ((Source) edge.getEndComponent());

        Timeslot startTimeSlot = startSource.getRelatedTimeslot();
        ConnectionSlot connectionslot = findConnectionslotByPredecessor(startTimeSlot);

        ConnectionSettings connectionSettings = new ConnectionSettings(startSource, endSource);
        connectionslot.addConnectionNode(connectionSettings);

        App.editor.getEditorView().refreshView();
    }

    public Source findDataOfTool(ConnectionSettings node)
    {
        /*Timeslot timeslot = findConnectionslotByNode(node).getPredecessor();
        int indexOfSourceTool = timeslot.getIndexOfSource(node.getOrigin());
        Source dataSource  = timeslot.getSourceAt(indexOfSourceTool + 1);

        if(dataSource.getSourceType().equals("data")) return dataSource;*/
        return null;
    }

    public Source findDataOfTool(Source tool)
    {
        /*Timeslot timeslot = tool.getRelatedTimeslot();

        int indexOfSourceTool = timeslot.getIndexOfSource(tool);
        Source dataSource = timeslot.getSourceAt(indexOfSourceTool + 1);

        if(dataSource.getSourceType().equals("data")) return dataSource;*/
        return null;
    }

    private ConnectionSlot findConnectionslotByNode(ConnectionSettings node)
    {
        return _connectionSlots.stream().filter(connectionSlot -> connectionSlot.containsNode(node)).findFirst().orElse(null);
    }

    public ConnectionSlot findConnectionslotByPredecessor(Timeslot predecessor)
    {
        return _connectionSlots.stream().filter(connectionSlot -> connectionSlot.getPredecessor().equals(predecessor)).findAny().orElse(null);
    }

    private ConnectionSettings findNodeByExchangeProcess(ExchangeProcess exchangeProcess)
    {
        for(ConnectionSlot slot : _connectionSlots)
        {
            if(slot.containsExchangeProcess(exchangeProcess)) return slot.getNodeByExchangeProcess(exchangeProcess);
        }
        return null;
    }

    // Setter & Getter

    public String getGraphName()
    {
        return graphName;
    }

    public void setGraphName(String graphName)
    {
        this.graphName = graphName;
    }
    
    public List<Timeslot> getTimeSlotList()
    {
        return timeSlotList;
    }

    public Timeslot getTimeSlot(int number)
    {
        for (Timeslot timeslotInList : timeSlotList)
        {
            if (number == timeslotInList.getSlotNumber())
            {
                return timeslotInList;
            }
        }
        return null;
    }
    
    public List<Edge> getEdgeList()
    {
        return edgeList;
    }

    public List<ConnectionOptionOutside> getConnectionOptionsBetweenTimeslots() {
        return connectionOptionsBetweenTimestepOutsides;
    }

    public List<ConnectionOptionWithIn> getConnectionOptionWithinTimeslots() {
        return connectionOptionWithinTimesteps;
    }
    
    public void removeToolsource(ToolSource toolSource)
    {
        // Remove all Elements in Toolsource
        toolSource.removeAll();
        
        // Remove Toolsource from Timeslot
        Timeslot relatedTimeslot = toolSource.getRelatedTimeslot();
        relatedTimeslot.getToolSourceList().remove(toolSource);
        relatedTimeslot.getToolContainer().remove(toolSource);
        relatedTimeslot.checkForElements();
        
        // Refresh View
        App.editor.getEditorView().refreshView();
    }
    
    public void removeDatasource(DataSource dataSource)
    {
        // Remove all Elements in Toolsource
        dataSource.removeAll();
    
        // Remove Toolsource from Timeslot
        Timeslot relatedTimeslot = dataSource.getRelatedTimeslot();
        relatedTimeslot.getDataSourceList().remove(dataSource);
        relatedTimeslot.getDataContainer().remove(dataSource);
        relatedTimeslot.checkForElements();
    
        // Refresh View
        App.editor.getEditorView().refreshView();
    }
}
