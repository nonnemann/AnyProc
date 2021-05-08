package main;

import java.io.File;
import java.util.List;

public class DataSource extends Source {

    private boolean isConnected;
    private List<ToolSource> connectedToolSources;

    public DataSource(File file) {
        super(file);
    }

}
