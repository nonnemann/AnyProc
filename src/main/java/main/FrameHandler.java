package main;

import main.App;

import javax.swing.*;
import java.awt.*;

public class FrameHandler
{
	private JFrame frameReference;
	
	public FrameHandler(JFrame frame)
	{
		frameReference = frame;
		App.globalSettings.getFrameList().add(frameReference);
	}
	
	public void setPositionAtCenter()
	{
		Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
		int pointX = screenDimension.width/2 - frameReference.getSize().width/2;
		int pointY = screenDimension.height/2 - frameReference.getSize().height/2;
		frameReference.setLocation(pointX, pointY);
	}
	
	public void setPositionAtBottomRight()
	{
		Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
		int pointX = screenDimension.width - frameReference.getWidth();
		int pointY = screenDimension.height - frameReference.getHeight();
		frameReference.setLocation(pointX, pointY);
	}
	
	public void setImageIcon()
	{
		String LOGO_PATH = "./src/main/resources/images/logo.png";
		ImageIcon logoImage = new ImageIcon(LOGO_PATH);
		frameReference.setIconImage(logoImage.getImage());
	}
}
