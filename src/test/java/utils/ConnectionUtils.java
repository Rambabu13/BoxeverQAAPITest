package utils;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Base64;

public class ConnectionUtils {

    private static int lastCode = 0;
    private static String lastErrorMessage = "";

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

    public static HttpsURLConnection delete(final String authToken, final String operation)
            throws IOException {
        final URL target = new URL("https://api.github.com/gists" + operation);
        final HttpsURLConnection connection = (HttpsURLConnection) target.openConnection();

        connection.setRequestMethod("DELETE");
        if (authToken != null)
            connection.setRequestProperty("Authorization", setBasicAuth(authToken));

        return connection;
    }

    static String setBasicAuth(String token) {
        String completeToken = token + ":x-oauth-basic";
        return "Basic " + Base64.getEncoder().encodeToString(completeToken.getBytes());
    }

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
