public class EmptyQuantity implements Quantity
{
	@Override
	public TransportData getData(ExchangeProcess ep)
	{
		return new TransportData(null, null);
	}
}
