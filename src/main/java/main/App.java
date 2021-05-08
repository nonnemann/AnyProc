package main;

import editorModule.ConnectionOptionOutside;
import editorModule.ConnectionOptionWithIn;
import editorModule.ConnectorLibrary;
import editorModule.Editor;
import executorModule.Executor;
import settings.LogFormatter;
import settings.Settings;
import transferHandling.ReVizeWebConnector;

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
    public static ConnectorLibrary connectorLibrary;
    
    public static Editor editor;
    public static Executor executor;
    public static boolean debugPrintout = true;

    public static void main(String[] arg) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException, IOException {
        initLogger();
        initServer();

        // Create global settings.Settings
        globalSettings = new Settings();
        globalSettings.SetUIManagerStyling();

        // Create new Main Menu
        new MainMenuUI("AnyProc");
    }

    static void initServer() throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder("python", "./src/revize_server/server.py");
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        revizeWebConnector = new ReVizeWebConnector();
        if(App.debugPrintout)
        {
            App.globalLogger.info("Python Server alive? " + process.isAlive());
        }
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
        globalGraph = new GlobalGraph("Default Graph");
        editor = new Editor();
    }
    
    public static void createNewExecutor()
    {
        executor = new Executor();
    }
    
    public static void changeEdgeDrawMode()
    {
        edgeDrawMode = !edgeDrawMode;
    }

    public static void changeToConnectionMode() {
        connectionMode = !connectionMode;

        for (ConnectionOptionOutside connectionOptionOutside : App.globalGraph.getConnectionOptionsBetweenTimeslots()) {
            connectionOptionOutside.setVisible(connectionMode);
        }

        for (ConnectionOptionWithIn connectionOptionWithin : App.globalGraph.getConnectionOptionWithinTimeslots())
        {
            connectionOptionWithin.setVisible(connectionMode);
        }
    }
}
