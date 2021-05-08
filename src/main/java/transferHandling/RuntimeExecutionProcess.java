package transferHandling;

import main.App;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class RuntimeExecutionProcess {

    Process mProcess;

    public void runCommand(String command){
        Process process;
        try{
            process = Runtime.getRuntime().exec(command);
            mProcess = process;
        }catch(Exception e) {
            App.globalLogger.severe("Exception Raised" + e.toString());
        }
        InputStream stdout = mProcess.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stdout, StandardCharsets.UTF_8));
        String line;
        try{
            while((line = reader.readLine()) != null){
                App.globalLogger.info("stdout: "+ line);
            }
        }catch(IOException e){
            App.globalLogger.severe("Exception in reading output"+ e.toString());
        }
    }
}
