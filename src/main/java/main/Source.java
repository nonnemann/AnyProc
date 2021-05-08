package main;

import dragAndDropHandling.DragPane;
import editorModule.EdgeDrawListener;
import editorModule.Timeslot;
import main.App;
import main.IconTransformer;
import main.RightClickPopUpListener;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;

import static org.apache.commons.io.FilenameUtils.removeExtension;

public class Source extends DragPane implements Serializable{

    private JPanel mainPanel;
    private JLabel sourceNameLabel;
    private ImageIcon sourceIcon;

    private File sourceFile;
    private String sourcePath;
    private String sourceName;

    private Timeslot relatedTimeSlot;

    public Source(File file) {

        // Get File Info
        getFileInfo(file);

        // Add Listener
        addMouseListener(new RightClickPopUpListener());

        // Create View
        Dimension panelSize = new Dimension(100,100);
        this.setPreferredSize(panelSize);
        this.add(mainPanel);
        updateView();
    }

    private void getFileInfo(File file) {
        sourceFile = file;
        sourcePath = file.getPath();
        sourceName = removeExtension(file.getName());
        sourceIcon = setSourceIcon(file);
    }

    private void updateView() {
        sourceNameLabel.setText(sourceName);
        sourceNameLabel.setIcon(sourceIcon);
        sourceNameLabel.setHorizontalTextPosition(JLabel.CENTER);
        sourceNameLabel.setVerticalTextPosition(JLabel.BOTTOM);
    }

    public ImageIcon setSourceIcon(File file)
    {
        ImageIcon sourceIcon = new ImageIcon();
        try
        {
            sun.awt.shell.ShellFolder shellFolder = sun.awt.shell.ShellFolder.getShellFolder(file);

            if (shellFolder != null)
            {
                sourceIcon = new ImageIcon(shellFolder.getIcon(true));
            }
            else
            {
                App.globalLogger.severe("No Applicationicon available.");
                sourceIcon = null;
            }
        }
        catch (FileNotFoundException exception)
        {
            exception.printStackTrace();
        }
        return setupIcon(sourceIcon);
    }

    private ImageIcon setupIcon(ImageIcon imageIcon)
    {
        IconTransformer iconTrans = new IconTransformer(imageIcon);
        iconTrans.scaleIcon(60, 60);
        iconTrans.setTransparentBackground();
        return iconTrans.transformToIcon();
    }

    // Setter & Getter
    public File getSourceFile()
    {
        return sourceFile;
    }

    public String getSourcePath() {
        return sourcePath;
    }

    public String getSourceName()
    {
        return sourceName;
    }

    public ImageIcon getSourceIcon() {
        return sourceIcon;
    }

    public Timeslot getRelatedTimeslot() {
        return relatedTimeSlot;
    }

    public void setRelatedTimeSlot(Timeslot relatedTimeSlot) {
        this.relatedTimeSlot = relatedTimeSlot;
    }
}
