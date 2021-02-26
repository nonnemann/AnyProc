public class FullQuantity implements Quantity
{
	@Override
	public TransportData getData(ExchangeProcess ep)
	{
		// returns the DataSource below the tool in the graph
		return new TransportData(null, App.globalGraph.findDataOfTool(ep.getOrigin()).getSourceFile());
	}
}
