package main;

import editorModule.ConnectionOptionWithIn;
import editorModule.CustomConnector;
import transferHandling.ReVizeWebConnector;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ToolSource extends Source {

    private List<DataSource> connectedDataSources;
    private boolean connected = true;

    private String dataExchangeInputChannel = "revizeServer";
    private String dataExchangeOutputChannel = "revizeServer";
    private int identifier;

    private CustomConnector connectorOption;
    private boolean customconnected = false;
    private boolean usesCustomConnector = false;

    public ToolSource(File file) {
        super(file);
        identifier = (int) Math.floor(Math.random() * 100000);
    }

    public String getDataExchangeChannel() {
        return dataExchangeInputChannel;
    }

    public void setDataExchangeChannel(String channelName)
    {
        dataExchangeInputChannel = channelName;
    }

    public void registerForRevizeserver() {
        try {
            ReVizeWebConnector.sendGET("/add/" + identifier);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isCustomConnected()
    {
        return customconnected;
    }

    public void changeCustomConnectionStatus(boolean status) {
        this.customconnected = status;
    }

    public String getDataExchangeInputChannel() {
        return dataExchangeInputChannel;
    }

    public String getDataExchangeOutputChannel() {
        return dataExchangeOutputChannel;
    }

    public int getIdentifier() { return this.identifier; }

    public void createConnectionOption() {
        // Create ConnectionOption
        connectorOption = new CustomConnector(this);

        // Add Connection Option to Timeslot UI
        ConnectionOptionWithIn connectionOptionWithIn = new ConnectionOptionWithIn(this.getRelatedTimeslot());
        connectionOptionWithIn.addFunctionality(connectorOption.getPanel());
    }
}
