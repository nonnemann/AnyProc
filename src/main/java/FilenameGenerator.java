import java.io.File;

public class FilenameGenerator
{
	public static String addCustomString(File originalFile, String addendum)
	{
		String fileName = originalFile.toPath().toString();
		int indexOfLastDot = fileName.lastIndexOf('.');
		return fileName.substring(0, indexOfLastDot) + addendum + fileName.substring(indexOfLastDot);
	}
}
