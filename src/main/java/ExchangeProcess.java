public class ExchangeProcess
{
	private SourceInSlot _origin, _destination;
	private Action _action;
	private Scope _scope;
	private Quantity _quantity;
	private Reuse _reuse;
	private TransportData _transportData;
	
	public ExchangeProcess()
	{
	}
	
	public void setTools(SourceInSlot origin, SourceInSlot destination)
	{
		_origin = origin;
		_destination = destination;
	}
	
	public void setParameters(Action action, Scope scope, Quantity quantity, Reuse reuse)
	{
		_action = action;
		_scope = scope;
		_quantity = quantity;
		_reuse = reuse;
	}
	
	public void generateTransportData()
	{
		switch(_scope)
		{
			case Parameter:
				_transportData = _action.getParameter();
				break;
			case Dataset:
				_transportData = _quantity.getData(this);
				break;
			case Combination:
				_transportData = new TransportData(_action.getParameter().getParameters(), _quantity.getData(this).getData());
				break;
		}
	}
	
	public Action getAction()
	{
		return _action;
	}
	public Reuse getReuse()
	{
		return _reuse;
	}
	public TransportData getTransportData()
	{
		return _transportData;
	}
	public SourceInSlot getDestination()
	{
		return _destination;
	}
	public SourceInSlot getOrigin()
	{
		return _origin;
	}
}
