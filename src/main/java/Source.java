import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Path;
import java.text.ParseException;

import static org.apache.commons.io.FilenameUtils.removeExtension;

public class Source implements Serializable
{
	private SourceModel sourceModel;
	private SourceView sourceView;
	
	private File sourceFile;
	private String sourcePath;
	private String sourceName;
	private String sourceType;
	
	public Source(File file, String type)
	{
		sourceFile = file;
		sourceName = removeExtension(file.getName());
		sourceType = type;

		getSourcePath();
		
		sourceModel = new SourceModel(this);
		sourceView = new SourceView(this);
	}

	private void getSourcePath() {
		try {
			sourcePath = sourceFile.getCanonicalPath();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(sourcePath);
	}

	// Setter & Getter
	public File getSourceFile()
	{
		return sourceFile;
	}
	
	public String getSourceName()
	{
		return sourceName;
	}
	
	public String getSourceType()
	{
		return sourceType;
	}
	
	public SourceModel getSourceModel()
	{
		return sourceModel;
	}
	
	public SourceView getSourceView()
	{
		return sourceView;
	}
}
