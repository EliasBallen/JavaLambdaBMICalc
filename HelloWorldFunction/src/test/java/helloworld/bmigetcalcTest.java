package helloworld;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class bmigetcalcTest {
    @Test
    public void successCalc(){
        Map<String, String> inputMap = new HashMap<String, String>();
        inputMap.put("weight","100.00");
        inputMap.put("weightType","kg");
        inputMap.put("height","2.00");
        inputMap.put("heightType","m");
        App app = new App();
        double bmicalc = app.getBMI(inputMap);
        assertEquals(25.00,bmicalc,0.00001);
    }
}