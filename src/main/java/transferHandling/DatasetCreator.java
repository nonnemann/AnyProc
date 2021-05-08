package transferHandling;

import java.awt.*;
import java.io.IOException;

public class DatasetCreator implements Reuse
{
	@Override
	public void reuseTransportData(ExchangeProcess ep)
	{
		try
		{
			Desktop.getDesktop().open(ep.getTransportData().getData());
		}
		catch(IOException exception)
		{
			exception.printStackTrace();
		}
	}

}
