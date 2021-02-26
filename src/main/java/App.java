import javax.swing.*;
import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.Logger;

public class App {
    
    public static Logger globalLogger;
    public static Settings globalSettings;
    public static GlobalGraph globalGraph;
    public static ReVizeWebConnector revizeWebConnector;
    public static Boolean edgeDrawMode = false;
    public static Boolean connectionMode = false;
    
    public static Editor editor;
    public static Executor executor;
    public static ReVizeWebConnector revizeConnector;
    
    public static boolean debugPrintout = true;

    public static void main(String[] arg) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException, IOException {
        initServer();
        initLogger();

        // Create global Settings
        globalSettings = new Settings();
        globalSettings.SetUIManagerStyling();

        // Create new Main Menu
        new MainMenuUI("AnyProc");
    }

    static void initServer() throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder("python", "./revize_server/server.py");
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        revizeWebConnector = new ReVizeWebConnector();
        System.out.println("Python Server alive? " + process.isAlive());
    }
    
    static void initLogger()
    {
        ConsoleHandler handler = new ConsoleHandler();
        LogFormatter formatter = new LogFormatter();
        formatter.setColor(LogFormatter.ANSI_YELLOW);
        handler.setFormatter(formatter);

        globalLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
        globalLogger.setUseParentHandlers(false);
        globalLogger.addHandler(handler);
    }

    static void createNewEditor() {
        createNewGraphIfNeeded();
        editor = new Editor();
    }

    static void createNewGraphIfNeeded()
    {
        if(App.globalGraph == null)
        {
            globalGraph = new GlobalGraph("Default Graph");
        }
    }
    
    public static void createNewExecutor()
    {
        createNewGraphIfNeeded();
        executor = new Executor();
    }
    
    public static void changeEdgeDrawMode()
    {
        edgeDrawMode = !edgeDrawMode;
    }

    public static void changeToConnectionMode()
    {
        connectionMode = !connectionMode;

        for (ConnectionOption connectionOption : App.globalGraph.getConnectionOptions())
        {
            connectionOption.setVisible(connectionMode);
        }

        /*for (ConnectionSlot connectionSlot : GlobalGraph._connectionSlots)
        {
            if (connectionMode)
            {

                connectionSlot.getConnectionSlotView().setVisible(true);
            }
            else
            {
                connectionSlot.getConnectionSlotView().setVisible(false);
            }
        }*/
    }
}
