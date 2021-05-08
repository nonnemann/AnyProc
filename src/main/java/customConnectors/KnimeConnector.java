package customConnectors;

import main.DataSource;
import main.Source;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class KnimeConnector {

    private List<DataSource> dataSources = new ArrayList<>();

    public KnimeConnector(List<DataSource> dataSources)
    {
        this.dataSources = dataSources;

        // Build Directories
        generateDirectories();

        // Build Workflow
        generateWorkflow();
    }

    private void generateWorkflow()
    {
        File knimeWorkflow = new File("./custom_connectors/workflow.knime");
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(knimeWorkflow.getPath()));
            bufferedWriter.write("");
            bufferedWriter.close();

            List<String> lines = Files.readAllLines(Paths.get("./custom_connectors/workflow.xml"), StandardCharsets.UTF_8);
            int number = 0;
            for (Source dataSource : dataSources)
            {
                number++;
                lines.add(17, getNodeSetup(number));
            }
            Files.write(Paths.get(knimeWorkflow.getPath()), lines, StandardCharsets.UTF_8);

        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private String getNodeSetup(int number) {

        StringBuilder modifiedFile = new StringBuilder();
        try {
            List<String> lines = Files.readAllLines(Paths.get("./custom_connectors/node.xml"), StandardCharsets.UTF_8);
            String firstLine = "<config key=\"node_" + number + "\">";
            String secondLine = "<entry key=\"id\" type=\"xint\" value=\"" + number + "\"/>";
            String thirdLine = "<entry key=\"node_settings_file\" type=\"xstring\" value=\"File Reader (#" + number + ")/settings.xml\"/>";
            lines.add(0, thirdLine);
            lines.add(0, secondLine);
            lines.add(0, firstLine);
            for (String line : lines)
            {
                modifiedFile.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return modifiedFile.toString();
    }

    private void generateDirectories()
    {
        int number = 0;
        for (Source dataSource : dataSources)
        {
            number++;
            String pathName = "./custom_connectors/" + "File Reader (#" + number +")/";
            Boolean isDirectoryCreated = new File(pathName).mkdirs();
            generateXMLSettings(pathName, dataSource);
        }
    }

    private void generateXMLSettings(String pathName, Source dataSource) {
        File xmlSetting = new File(pathName + "settings.xml");
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(xmlSetting.getPath()));
            bufferedWriter.write("");
            bufferedWriter.close();

            List<String> lines = Files.readAllLines(Paths.get("./custom_connectors/settings.xml"), StandardCharsets.UTF_8);
            String prefixPath = "<entry key=\"DataURL\" type=\"xstring\" value=\"file:/";
            String mainPath = dataSource.getSourceFile().getPath().replaceAll("\\\\", "/");
            String suffixPath = "\"/>";
            lines.add(9,  prefixPath + mainPath + suffixPath);
            Files.write(Paths.get(xmlSetting.getPath()), lines, StandardCharsets.UTF_8);

        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public List<DataSource> getDataSources() {
        return dataSources;
    }
}
