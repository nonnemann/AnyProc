import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.ParseException;


public class WindowsLinkParserTest
{
    public static void main(String[] args) throws IOException, ParseException
    {
        String[] filenames =
                {
                    "C://Users/Nutzer/Downloads/AnyProc Demos/Usecase 3 - ReVize/KNIME Analytics Platform.lnk"
                };
        for (String filename : filenames)
        {
            printLink(filename);
        }
    }

    public static void printLink(String filename) throws IOException, ParseException
    {

        File file = new File(filename);
        WindowsLinkParser link = new WindowsLinkParser(file);

        System.out.printf("-------%s------ \n", filename);
        System.out.printf("getRealFilename: %s \n", link.getRealFilename());
        System.out.printf("getDescription: %s \n", link.getDescription());
        System.out.printf("getRelativePath: %s \n", link.getRelativePath());
        System.out.printf("getWorkingDirectory: %s \n", link.getWorkingDirectory());
        System.out.printf("getCommandLineArguments: %s \n", link.getCommandLineArguments());
        System.out.printf("isLocal: %b \n", link.isLocal());
        System.out.printf("isDirectory: %b \n", link.isDirectory());
    }
}