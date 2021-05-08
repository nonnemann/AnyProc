package editorModule;

import customConnectors.KnimeConnector;

import java.util.ArrayList;
import java.util.List;

public class ConnectorLibrary {

    private List<KnimeConnector> connectorLibrary;

    public ConnectorLibrary() {
        connectorLibrary = new ArrayList<>();
    }

    public void addConnector(KnimeConnector connector)
    {
        connectorLibrary.add(connector);
    }

    public List<KnimeConnector> getConnectorLibrary() {
        return connectorLibrary;
    }
}
