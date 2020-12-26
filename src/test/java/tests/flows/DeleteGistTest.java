package tests.flows;

import objects.FinalData;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.ConnectionUtils;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;

import static utils.ConnectionUtils.assignLast;

public class DeleteGistTest {

    @Test
    public static void deleteGistTest(String id) throws IOException {
        HttpsURLConnection response = ConnectionUtils.delete(FinalData.token, "/" + id);
        Assert.assertEquals(response.getResponseCode(), 204);
        assignLast(response);
    }
}
