import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;

public class PanelTransferable implements Transferable
{
	private final DataFlavor[] flavors = new DataFlavor[]{PanelDataFlavor.SHARED_INSTANCE};
	private final JPanel panel;
	
	public PanelTransferable(JPanel panel)
	{
		this.panel = panel;
	}
	
	@Override
	public DataFlavor[] getTransferDataFlavors()
	{
		return flavors;
	}
	
	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor)
	{
		boolean supported = false;
		for (DataFlavor mine : getTransferDataFlavors())
		{
			if (mine.equals(flavor))
			{
				supported = true;
				break;
			}
		}
		return supported;
	}
	
	private JPanel getPanel()
	{
		return panel;
	}
	
	@NotNull
	@Override
	public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException
	{
		Object data;
		if (isDataFlavorSupported(flavor))
		{
			data = getPanel();
		} else
		{
			throw new UnsupportedFlavorException(flavor);
		}
		return data;
	}
}
