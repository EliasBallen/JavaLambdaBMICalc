package helloworld;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class bmicalcTest {
    @Test
    public void successCalc(){
        App app = new App();
        double bmicalc = app.bmiCalc(2.00*3.28084, 100.00, "ft", "kg");
        assertEquals(25.00,bmicalc,0.00001);
    }
}