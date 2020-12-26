package tests;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.testng.annotations.Test;
import tests.flows.AddGistTest;
import tests.flows.DeleteGistTest;
import tests.flows.GetGistTest;

import java.io.IOException;

public class FullFlowTest {

    @Test
    public void fullFlowTest () throws IOException, ParseException {
        JSONObject addResult = AddGistTest.addGistTest();
        String id = addResult.get("id").toString();
        GetGistTest.getGistTest(id, addResult);
        DeleteGistTest.deleteGistTest(id);
    }
}
