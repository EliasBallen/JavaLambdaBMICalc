package helloworld;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class AppTest {
  @Test
  public void successfulResponse() {
    APIGatewayProxyRequestEvent reqevent = new APIGatewayProxyRequestEvent();
    Map<String, String> inputMap = new HashMap<String, String>();
    inputMap.put("weight","100.00");
    inputMap.put("weightType","kg");
    inputMap.put("height","2.00");
    inputMap.put("heightType","m");
    reqevent.setQueryStringParameters(inputMap);
    App app = new App();

    APIGatewayProxyResponseEvent result = app.handleRequest(reqevent, null);
    assertEquals(200, result.getStatusCode().intValue());
    assertEquals("application/json", result.getHeaders().get("Content-Type"));
    String content = result.getBody();
    assertNotNull(content);
    assertTrue(content.contains("\"BMI\""));
    assertTrue(content.contains("25.00"));
    assertTrue(content.contains("\"overweight\""));
  }
}
