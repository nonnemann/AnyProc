package transferHandling;

import editorModule.ConnectionSettings;
import settings.ColorConverter;

import java.util.ArrayList;
import java.util.List;

public class TransformationCreator
{
	public static void createTransformations(List<ColumnTransformationView> columnTransformationViews, ConnectionSettings correspondingNode, boolean forward)
	{
		List<Transformation> transformations = new ArrayList<>();
		
		for(ColumnTransformationView view : columnTransformationViews)
		{
			String oldColumnName = view.getOldColumnName();
			String newColumnName = view.getNewColumnName();
			
			if(!newColumnName.equals(""))
				transformations.add(new ColumnNameConverter(oldColumnName, newColumnName));
			
			for(String rule : view.getTransformations().keySet())
			{
				if(rule.contains("color"))
					transformations.add(new ColorConverter(newColumnName));
				else if(rule.contains("time"))
					transformations.add(new TimeConverter(newColumnName));
			}
		}
		
		correspondingNode.setTransformations(forward, transformations);
	}
}
