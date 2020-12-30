package utils;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Base64;

public class ConnectionUtils {

    private static int lastCode = 0;
    private static String lastErrorMessage = "";

    /**
     * Function to get a GIST from GITHUB and cast it to a String
     * @param authToken User token to connect to GITHUB
     * @param id ID of the GIST we want to retrieve
     * @param operation If needed, the operation to perform
     * @return The desired GIST as a String
     * @throws IOException
     */
    public static String get (final String authToken, final String id, final String operation)
            throws IOException {
        final URL target = new URL("https://api.github.com/gists/"+ id + operation);
        final HttpsURLConnection connection = (HttpsURLConnection) target.openConnection();
        if(authToken != null)
            connection.setRequestProperty("Authorization",setBasicAuth(authToken));

        String res;
        try {
            res = getResponse(connection.getInputStream());
        } finally {
            assignLast(connection);
        }

        return res;
    }

    /**
     * Function to connect to GITHUB to obtain a GIST
     * @param stream Connection data string
     * @return A Gist from HITBUG as a String
     * @throws IOException
     */
    public static String getResponse(final InputStream stream)
            throws IOException {
        StringBuilder full = new StringBuilder();
        String line;

        InputStreamReader reader = new InputStreamReader(stream, Charset.forName("UTF-8"));
        try (BufferedReader streamBuf = new BufferedReader(reader)) {

            while ((line = streamBuf.readLine()) != null)
                full.append(line);

        } finally {
            reader.close();
            stream.close();
        }

        return full.toString();
    }

    /**
     * Function to create a GIST
     * @param authToken User token to connect to GITHUB
     * @param operation If needed, the operation to perform
     * @param gist The basic data to create a GIST in GITHUB
     * @return The connection object to be used in the main test
     * @throws IOException
     */
    public static HttpsURLConnection post(final String authToken, final String operation, final String gist)
            throws IOException {
        final URL target = new URL("https://api.github.com/gists" + operation);
        final HttpsURLConnection connection = (HttpsURLConnection) target.openConnection();

        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        if (authToken != null) {
            connection.setRequestProperty("Authorization", setBasicAuth(authToken));
        }
        OutputStream ost = connection.getOutputStream();
        try (final DataOutputStream requestBody = new DataOutputStream(ost)) {
            requestBody.writeBytes(gist);
        } finally {
            ost.close();
        }

        return connection;

    }

    /**
     * Function to delete a GIST from GITHUB
     * @param authToken User token to connect to GITHUB
     * @param operation If needed, the operation to perform
     * @return The connection object to be used in the main test
     * @throws IOException
     */
    public static HttpsURLConnection delete(final String authToken, final String operation)
            throws IOException {
        final URL target = new URL("https://api.github.com/gists" + operation);
        final HttpsURLConnection connection = (HttpsURLConnection) target.openConnection();

        connection.setRequestMethod("DELETE");
        if (authToken != null)
            connection.setRequestProperty("Authorization", setBasicAuth(authToken));

        return connection;
    }

    /**
     * Function to code the user token to a basic authentication to connect to GITHUB
     * @param token User token to connect to GITHUB
     * @return The connection String
     */
    static String setBasicAuth(String token) {
        String completeToken = token + ":x-oauth-basic";
        return "Basic " + Base64.getEncoder().encodeToString(completeToken.getBytes());
    }

    /**
     * Function to keep the last connection data used
     * @param conn The connection objet used in the test
     */
    public static void assignLast(HttpsURLConnection conn) {
        try {
            lastCode = conn.getResponseCode();
            lastErrorMessage = conn.getResponseMessage();
        } catch(IOException ioe) {
            lastCode = -1;
            lastErrorMessage = "Unknown";
        }
    }
}
