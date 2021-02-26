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
    private List<ConnectionOption> connectionOptions = new ArrayList<>();
    
    public GlobalGraph(String name)
    {
        graphName = name;
    }
    
    public void reset()
    {
        for(Timeslot timeslot : timeSlotList)
        {
            timeslot.removeAll();
            for(SourceInSlot sourceInSlot : timeslot.getElementList())
            {
                sourceInSlot.getSourceView().removeAll();
            }
            timeslot.getElementList().clear();
        }
        timeSlotList.clear();
        edgeList.clear();
        
        App.editor.getEditorView().getGraphPoolPanel().removeAll();
        App.editor.getEditorView().refreshView();
    }
    
    public void addObjectToNewTimeslot(Object dragObject)
    {
        Timeslot timeslot = createNewTimeslot();
        timeslot.addObjectToTimeslot((SourceView) dragObject);
        App.editor.getEditorView().refreshView();
    }
    
    private Timeslot createNewTimeslot()
    {
        Timeslot newTimeslot = new Timeslot();
        newTimeslot.addToGraph();
        
        if(_connectionSlots.size() >= 1)
        {
            _connectionSlots.get(_connectionSlots.size() - 1).setSuccessor(newTimeslot);
        }
        
        if(App.debugPrintout)
        {
            App.globalLogger.info("Timeslot " + timeSlotList.size() + " added");
        }

        if (timeSlotList.size() >= 2)
        {
            Timeslot previousTimeslot = timeSlotList.get(timeSlotList.size()-1);
            ConnectionOption connectionOption = new ConnectionOption(previousTimeslot, newTimeslot);
            switchComponentsInGraphPool(newTimeslot, connectionOption);
            App.editor.getEditorView().refreshView();

            // TODO Ersetzen
            //createNewConnectionslot(newTimeslot);
        }
        
        return newTimeslot;
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

        // Get Start
        // Get Destination
        //App.editor.getEditorView().getGraphPoolPanel().add(new ConnectionOption());
        App.editor.getEditorView().refreshView();
    }
    
    public void addObjectToExistingTimeslot(Object object, Component component)
    {
        Timeslot timeslot = getTimeslotFromList(component);
        timeslot.addObjectToTimeslot((SourceView) object);
        
        if(App.debugPrintout)
        {
            App.globalLogger.info("Element " + timeslot.getElementList().size() + " added to Timeslot " + timeslot.getSlotNumber());
        }
        
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
            timeslot.getElementList().clear();
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
    
    public void removeSourceInSlot(SourceInSlot source)
    {
        // Clear View
        source.getSourceView().removeAll();
        
        // Delete Source from Timeslot or Timeslot (if needed)
        Timeslot relatedTimeslot = source.getRelatedTimeslot();
        relatedTimeslot.getElementList().remove(source);
        relatedTimeslot.remove(source.getSourceView());
        if(relatedTimeslot.getElementList().isEmpty())
        {
            removeTimeslot(relatedTimeslot);
        }
        
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
            timeslot.refreshTopLabel();
        }
    }
    
    public void changePosition(Timeslot timeslot, int position)
    {
        // Change Position in Timeslotlist
        timeSlotList.remove(timeslot);
        timeSlotList.add(position-1, timeslot);

        for (Timeslot timeslot1 : timeSlotList)
        {
            System.out.println(timeslot1.getElementList().get(0).getSourceFile().getName());
        }

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
        for(Timeslot timeslot : timeSlotList)
        {
            timeslot.addToGraph();
            for(SourceInSlot source : timeslot.getElementList())
            {
                timeslot.add(source.getSourceView());
            }

            if (timeslot.getSlotNumber() >= 2)
            {
                Timeslot previousTimeslot = timeSlotList.get(timeSlotList.size()-1);
                ConnectionOption connectionOption = new ConnectionOption(previousTimeslot, timeslot);
                switchComponentsInGraphPool(timeslot, connectionOption);
                App.editor.getEditorView().refreshView();
            }
        }
    }
    
    /*private void recreateGraphViewFromGraphModel()
    {
        for(Timeslot timeslot : timeSlotList)
        {
            timeslot.setTimeslotView(new Timeslot(timeslot));
            for(SourceInSlot source : timeslot.getElementList())
            {
                source.setSourceView(new SourceViewInSlot(source));
            }
        }
        
        // Refresh View
        App.editor.getEditorView().refreshView();
    }*/
    
    /*public void loadViewFromModel()
    {
        for(Timeslot timeslot : timeSlotList)
        {
            timeslot.setTimeslotView(new Timeslot(timeslot));
            timeslot.getTimeslotView().addViewToGraph();
            for(SourceInSlot sourceInSlot : timeslot.getElementList())
            {
                sourceInSlot.setRelatedTimeslot(timeslot);
                sourceInSlot.setSourceView(new SourceViewInSlot(sourceInSlot));
                sourceInSlot.getSourceView().addViewToSlot();
            }
        }
        // Refresh View
        App.editor.getEditorView().refreshView();
    }*/
    
    public void createConnectionSlot(Edge edge)
    {
        SourceInSlot startSource = ((SourceViewInSlot) edge.getStartComponent()).getRelatedSource();
        SourceInSlot endSource = ((SourceViewInSlot) edge.getEndComponent()).getRelatedSource();

        if(isDataSource(startSource, endSource)) return;
        
        Timeslot startTimeSlot = startSource.getRelatedTimeslot();
        ConnectionSlot connectionslot = findConnectionslotByPredecessor(startTimeSlot);
        
        ConnectionSettings connectionSettings = new ConnectionSettings(startSource, endSource);
        connectionslot.addConnectionNode(connectionSettings);

        App.editor.getEditorView().refreshView();
    }
    
    private boolean isDataSource(SourceInSlot startSource, SourceInSlot endSource)
    {
        return startSource.getSourceType()
                          .equals("data") || endSource.getSourceType()
                                                      .equals("data");
    }
    
    public SourceInSlot findDataOfTool(ConnectionSettings node)
    {
        Timeslot timeslot = findConnectionslotByNode(node).getPredecessor();
        int indexOfSourceTool = timeslot.getIndexOfSource(node.getOrigin());
        SourceInSlot dataSource  = timeslot.getSourceAt(indexOfSourceTool + 1);
        
        if(dataSource.getSourceType().equals("data")) return dataSource;
        return null;
    }
    
    public SourceInSlot findDataOfTool(SourceInSlot tool)
    {
        Timeslot timeslot = tool.getRelatedTimeslot();
        
        int indexOfSourceTool = timeslot.getIndexOfSource(tool);
        SourceInSlot dataSource = timeslot.getSourceAt(indexOfSourceTool + 1);
        
        if(dataSource.getSourceType().equals("data")) return dataSource;
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
    
    public List<Edge> getEdgeList()
    {
        return edgeList;
    }

    public List<ConnectionOption> getConnectionOptions() {
        return connectionOptions;
    }
}
