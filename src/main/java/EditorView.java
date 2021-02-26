import javax.swing.*;
import java.awt.*;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static java.awt.event.InputEvent.CTRL_DOWN_MASK;

public class EditorView extends JFrame{

    private Editor editorReference;

    private JPanel editorFramePanel;
    private JPanel toolPoolPanel;
    private JPanel graphPoolPanel;
    private JPanel dataPoolPanel;

    private JLabel toolPoolLabel;
    private JLabel dataPoolLabel;

    private JMenu fileMenu;
    private JMenuItem newToolchainItem;
    private JMenuItem loadToolchainItem;
    private JMenuItem saveToolchainItem;
    private JMenuItem exitEditorItem;

    private JMenu editMenu;
    private JMenuItem executeToolchainItem;
    
    private JCheckBox edgeModeBox;
    private JCheckBox connectionModeBox;

    public EditorView(Editor editor) {
    
        super("Analytical Toolchain Editor");
        
        // Get Reference
        editorReference = editor;

        // Init Content
        initContentPane();
        sendWarningWhenClosingWindow(false);

        // Add Frame Handler
        addFrameHandler();

        // Setup Toolpool
        setupToolPool();

        // Setup Datapool
        setupDataPool();

        // Setup Graphpool
        setupGraphPool();

        // Setup MenuBar
        setupMenuBar();

        // Add RightClickPopUpListener Listener
        addMouseListener(new RightClickPopUpListener());
        
        // Checkbox Functionality
        edgeModeBox.addItemListener(event -> App.changeEdgeDrawMode());
        connectionModeBox.addItemListener(event -> {
            App.changeToConnectionMode();
        });
    }

    private void initContentPane()
    {
        this.setContentPane(this.editorFramePanel);
        this.pack();
        this.setVisible(true);
    }

    private void sendWarningWhenClosingWindow(boolean enabled)
    {
        if(enabled)
        {
            addWindowListener(new WindowAdapter()
            {
                @Override
                public void windowClosing(WindowEvent we)
                {
                    String[] possibilities = {"Yes", "No"};
                    int PromptResult = JOptionPane.showOptionDialog(null, "Are you sure you want to exit without saving?", "Warning", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, possibilities, possibilities[1]);
                    if(PromptResult == JOptionPane.YES_OPTION)
                    {
                        dispose();
                    }
                }
            });
        }
        else
        {
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        }
    }

    private void addFrameHandler()
    {
        FrameHandler frameHandler = new FrameHandler(this);
        frameHandler.setPositionAtCenter();
        frameHandler.setImageIcon();
    }

    private void setupToolPool()
    {
        // Init
        toolPoolPanel.setName("Tool Pool");

        // Add Drag and Drop support
        DataTransferHandler toolImportHandler = new DataTransferHandler(toolPoolPanel);
        toolPoolPanel.setTransferHandler(toolImportHandler);

        // Enable Rightclick Options
        toolPoolPanel.addMouseListener(new RightClickPopUpListener());

        // Add Title Label
        checkForToolPoolLabel();
    }

    void addToolPoolLabel()
    {
        toolPoolLabel = new JLabel("Drop Tools here ...");
        toolPoolLabel.setForeground(App.globalSettings.getFontColor());
        toolPoolPanel.add(toolPoolLabel);
    }

    void removeToolPoolLabel()
    {
        toolPoolPanel.remove(toolPoolLabel);
    }

    public void checkForToolPoolLabel()
    {
        if (!editorReference.getToolSourceList().isEmpty())
        {
            removeToolPoolLabel();
        }
        else
        {
            addToolPoolLabel();
        }
    }

    private void setupDataPool()
    {
        // Init
        dataPoolPanel.setName("Data Pool");

        // Add Drag and Drop support
        DataTransferHandler dataImportHandler = new DataTransferHandler(dataPoolPanel);
        dataPoolPanel.setTransferHandler(dataImportHandler);

        // Enable Rightclick Options
        dataPoolPanel.addMouseListener(new RightClickPopUpListener());

        // Add Title Label
        checkForDataPoolLabel();
    }

    void addDataPoolLabel()
    {
        dataPoolLabel = new JLabel("Drop Data Sources here ...");
        dataPoolLabel.setForeground(App.globalSettings.getFontColor());
        dataPoolPanel.add(dataPoolLabel);
    }

    void removeDataPoolLabel()
    {
        dataPoolPanel.remove(dataPoolLabel);
    }

    public void checkForDataPoolLabel() {
        if (!editorReference.getDataSourceList().isEmpty())
        {
            removeDataPoolLabel();
        }
        else
        {
            addDataPoolLabel();
        }
    }

    private void setupGraphPool()
    {
        // Init
        graphPoolPanel.setName("Graph Pool");
        DropGestureHandler dropGestureHandler = new DropGestureHandler();
        DropTarget dropTarget = new DropTarget(this, DnDConstants.ACTION_COPY, dropGestureHandler, true);

        // Enable Rightclick Options
        graphPoolPanel.addMouseListener(new RightClickPopUpListener());
    }

    private void setupMenuBar()
    {
        // New Toolchain
        newToolchainItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, CTRL_DOWN_MASK));
        newToolchainItem.addActionListener(e -> App.globalGraph.reset());
        fileMenu.add(newToolchainItem);

        // Load Toolchain
        loadToolchainItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, CTRL_DOWN_MASK));
        loadToolchainItem.addActionListener(e -> {
            App.globalGraph.reset();
            JFrame frame = new JFrame("Load File from ...");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new FileLoader());
        });
        fileMenu.add(loadToolchainItem);

        // Save Toolchain
        saveToolchainItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, CTRL_DOWN_MASK));
        saveToolchainItem.addActionListener(e -> {
            JFrame frame = new JFrame("Save File to ...");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new FileSaver());
        });
        fileMenu.add(saveToolchainItem);

        // Separator
        fileMenu.addSeparator();

        // Exit Application
        exitEditorItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitEditorItem);

        // Open Executor
        executeToolchainItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, CTRL_DOWN_MASK));
        executeToolchainItem.addActionListener(e -> {
            if(App.executor == null)
            {
                App.createNewExecutor();
            }
        });
        editMenu.add(executeToolchainItem);
    }

    public void paint(Graphics graphics) {
        super.paint(graphics);
        Graphics2D graphics2D = (Graphics2D) graphPoolPanel.getGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setStroke(new BasicStroke(5f));
        graphics2D.setPaint(App.globalSettings.getEdgeColor());

        for(Edge edge : App.globalGraph.getEdgeList())
        {
            edge.calculateLine();
            graphics2D.drawLine(edge.getStartComponentCenter().x-10, edge.getStartComponentCenter().y-210, edge.getEndComponentCenter().x-10, edge.getEndComponentCenter().y-210);
        }
    }
    
    public void refreshView()
    {
        revalidate();
        repaint();
    }

    // Setter & Getter

    public JPanel getToolPoolPanel() {
        return toolPoolPanel;
    }

    public void setToolPoolPanel(JPanel toolPoolPanel) {
        this.toolPoolPanel = toolPoolPanel;
    }

    public JPanel getGraphPoolPanel() {
        return graphPoolPanel;
    }

    public void setGraphPoolPanel(JPanel graphPoolPanel) {
        this.graphPoolPanel = graphPoolPanel;
    }

    public JPanel getDataPoolPanel() {
        return dataPoolPanel;
    }

    public void setDataPoolPanel(JPanel dataPoolPanel) {
        this.dataPoolPanel = dataPoolPanel;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
