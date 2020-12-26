package tests.flows;

import Objects.FinalData;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.ConnectionUtils;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;

import static utils.ConnectionUtils.assignLast;
import static utils.ConnectionUtils.getResponse;
import static utils.JsonUtils.readJson;
import static utils.JsonUtils.stringToJson;

public class AddGistTest {

    public static JSONObject result;

    @Test
    public static JSONObject addGistTest () throws IOException, ParseException {
        JSONObject gist = readJson("src/test/resources/addGistTestRQ.json");
        HttpsURLConnection response = ConnectionUtils.post(FinalData.token, "", gist.toString());
        Assert.assertNotNull(response);
        String res = getResponse(response.getInputStream());
        JSONObject actual = stringToJson(res);
        Assert.assertEquals(response.getResponseCode(), 201);
        result = readJson("src/test/resources/addGistTestRS.json");
        checkResponseFields(actual);
        assignLast(response);
        return actual;
    }

    private static void checkResponseFields (JSONObject actual) {
        Assert.assertTrue(actual.containsKey("id"));
        Assert.assertEquals(actual.get("filename"), result.get("filename"));
        Assert.assertEquals(actual.get("content"), result.get("content"));
        Assert.assertEquals(actual.get("description"), result.get("description"));
        Assert.assertEquals(actual.get("public"), result.get("public"));
        Assert.assertEquals(actual.get("owner"), result.get("owner"));
    }
}