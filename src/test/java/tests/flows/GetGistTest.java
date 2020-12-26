package tests.flows;

import objects.FinalData;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.ConnectionUtils;

import java.io.IOException;

import static utils.JsonUtils.readJson;
import static utils.JsonUtils.stringToJson;

public class GetGistTest {

    @Test
    public static void getGistTest(String id, JSONObject expected) throws IOException, ParseException {
        String response = ConnectionUtils.get(FinalData.token, id, "");
        Assert.assertNotNull(response);
        JSONObject actual = stringToJson(response);
        if (expected == null) {
            expected = readJson("src/test/resources/getGistTest.json");
        }
        Assert.assertEquals(actual, expected);
    }
}
