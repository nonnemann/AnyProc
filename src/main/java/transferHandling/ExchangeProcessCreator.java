package transferHandling;

import main.App;
import main.Source;

import java.io.File;

public class ExchangeProcessCreator
{
	public static ExchangeProcess createExchangeProcess(ExchangeProcessView view, Source originTool, Source destinationTool)
	{
		Action action = generateAction(originTool, view.getAction());
		
		if(!actionWasTaken(action))
			return createDefaultExchangeProcess(originTool, destinationTool, action);
		else
			return createCustomExchangeProcess(view, originTool, destinationTool, action);
	}
	
	private static ExchangeProcess createDefaultExchangeProcess(Source originTool, Source destinationTool, Action action)
	{
		ExchangeProcess exchangeProcess = new ExchangeProcess();
		
		Scope scope = Scope.Dataset;
		Quantity quantity = new FullQuantity();
		Reuse reuse = new DefaultReuse();
		exchangeProcess.setParameters(action, scope, quantity, reuse);
		exchangeProcess.setTools(originTool, destinationTool);
		
		return exchangeProcess;
	}
	
	private static ExchangeProcess createCustomExchangeProcess(ExchangeProcessView view, Source originTool, Source destinationTool, Action action)
	{
		ExchangeProcess exchangeProcess = new ExchangeProcess();
		
		Scope scope = generateScope(view.getScope());
		Quantity quantity = generateQuantity(originTool.getSourceName(), view.getQuantity());
		Reuse reuse = generateReuse(destinationTool.getSourceName(), view.getReuse());
		exchangeProcess.setParameters(action, scope, quantity, reuse);
		exchangeProcess.setTools(originTool, destinationTool);
		
		return exchangeProcess;
	}
	
	private static boolean actionWasTaken(Action action)
	{
		return action != null && action.wasTaken();
	}
	
	private static Action generateAction(Source originTool, String action)
	{
		String originToolName = originTool.getSourceName();
		
		switch(action)
		{
			case "Selection":
				if(originToolName.equals("Plant@Hand3D"))
				{
					String pathParameter = originTool.getSourceFile().toPath().getParent().toString();
					pathParameter += "/parameters.json";
					return new PlantAtHandSelection(new File(pathParameter));
				}
			case "Sorting":
				if(originToolName.equals("EXCEL"))
				{
					// Sorting within Excel is assumed to have occurred when the modified .csv-file from P@H exists --> tailored to the scenario
					String pathModifiedActivityData = FilenameGenerator.addCustomString(App.globalGraph.findDataOfTool(originTool).getSourceFile(), "_modified");
					return new ExcelSorting(new File(pathModifiedActivityData));
				}
			default:
				return null;
		}
	}
	
	private static Scope generateScope(String scope)
	{
		switch(scope)
		{
			case "Parameter":
				return Scope.Parameter;
			case "Dataset":
				return Scope.Dataset;
			case "Combination":
				return Scope.Combination;
			default:
				return null;
		}
	}
	
	private static Quantity generateQuantity(String originToolName, String quantity)
	{
		switch(quantity)
		{
			case "Full":
				if(originToolName.equals("Plant@Hand3D"))
					return new FullQuantity();
				if(originToolName.equals("EXCEL"))
					return new ExcelFullQuantity();
			case "Delta":
				if(originToolName.equals("Plant@Hand3D"))
					return new PlantAtHandDeltaQuantity();
				else return null;
			case "Empty":
				return new EmptyQuantity();
			default:
				return null;
		}
	}
	
	private static Reuse generateReuse(String destinationToolName, String reuse)
	{
		switch(reuse)
		{
			case "New Dataset":
				return new DatasetCreator();
			case "Modify Dataset":
				if(destinationToolName.equals("Plant@Hand3D"))
					return new PlantAtHandDataModifier();
				else return null;
			case "No Changes":
				return new DefaultReuse();
		}
		return null;
	}
}
