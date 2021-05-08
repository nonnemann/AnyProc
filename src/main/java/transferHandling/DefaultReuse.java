package transferHandling;

import java.awt.*;
import java.io.IOException;

public class DefaultReuse implements Reuse
{
	@Override
	public void reuseTransportData(ExchangeProcess ep)
	{
		try
		{
			Desktop.getDesktop().open(ep.getDestination().getSourceFile());
		}
		catch(IOException exception)
		{
			exception.printStackTrace();
		}
		
	}
}
