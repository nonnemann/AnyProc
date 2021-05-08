package transferHandling;

import main.App;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static java.lang.String.valueOf;

public class ReVizeWebConnector {

    private static final String USER_AGENT = "Mozilla/5.0";
    private static String url = "http://localhost:5000";

    public ReVizeWebConnector() throws IOException {
        testConnection();
    }

    private void testConnection() throws IOException {
        sendGET("");
    }

    public static URL sendGET(String option) throws IOException {
        URL obj = new URL(url  + option);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);
        int responseCode = con.getResponseCode();
        App.globalLogger.info("GET Response Code :: " + responseCode);
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // print result
            App.globalLogger.info(response.toString());
            App.globalLogger.info("GET DONE");
        } else {
            App.globalLogger.warning("Failed to sent GET request");
        }
        return obj;
    }


    public static String sendGetStatus(String identifier) throws IOException {
        String message = "";
        URL obj = new URL(url + "/status/" + identifier);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);
        int responseCode = con.getResponseCode();
        App.globalLogger.info("GET Response Code :: " + valueOf(responseCode));
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            message = response.toString();

            // print result
            App.globalLogger.info(response.toString());
            App.globalLogger.info("GET DONE");
        } else {
            message = null;
            App.globalLogger.warning("Failed to sent GET request");
        }
        return message;
    }
}
