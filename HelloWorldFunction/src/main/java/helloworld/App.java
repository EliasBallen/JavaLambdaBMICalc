package helloworld;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

/**
 * Handler for requests to Lambda function.
 */
public class App implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Access-Control-Allow-Origin", "*"); // Adjust the origin accordingly for production
        headers.put("Access-Control-Allow-Credentials", "true");

        headers.put("Content-Type", "application/json");
        headers.put("X-Custom-Header", "application/json");

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
                .withHeaders(headers);
        //final String pageContents = this.getPageContents("https://checkip.amazonaws.com");
        String output = "";
        if (input == null){
            output = "{ \"error\": \"not passed any data\" }";
        }else{
            Map<String,String> inputquery =  input.getQueryStringParameters();
            double bmi = getBMI(inputquery);
            String diagnosis = returndiagnosis(bmi);
            output = String.format("{ \"BMI\": \"%f\" , \"diagnosis\": \"%s\" }", bmi,diagnosis);
        }
        return response
                .withStatusCode(200)
                .withBody(output);

    }

    private String getPageContents(String address) throws IOException{
        URL url = new URL(address);
        try(BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()))) {
            return br.lines().collect(Collectors.joining(System.lineSeparator()));
        }
    }
    private final double feetconversion = 3.28084;

    public double getBMI(Map<String,String> inputMap){
        double height = 1.75;  //m default
        double weight = 75.00; // kg
        String heightType = "m";  //m default
        String weightType = "kg"; // kg

        if(inputMap.containsKey("height")){
            try{
                height = Double.parseDouble(inputMap.get("height"));
            }catch(NumberFormatException e){
            }
        }
        if(inputMap.containsKey("weight")){
            try{
                weight = Double.parseDouble(inputMap.get("weight"));
            }catch(NumberFormatException e){
            }
        }
        if(inputMap.containsKey("weightType")){
            weightType = inputMap.get("weightType");
        }
        if(inputMap.containsKey("heightType")){
            heightType = inputMap.get("heightType");
        }

        double bmi = bmiCalc(height,weight,heightType,weightType);
        return bmi;
    }
    public double bmiCalc(double height, double weight, String heightType, String weightType){
        double bmi = 20;
//        System.out.println("weight " + weight + " weightType " + weightType +" height " + height +" heightType " + heightType);

        if(weightType.equals("lbs")) {
//            System.out.println("selectedpound");
            if (!heightType.equals("ft")) {
                height = height*feetconversion*12; //convert to inches
            }
            bmi = weight*703/(height*height);
        }
        if(weightType.equals("kg")){
//            System.out.println("selectedKg");
            if (!heightType.equals("m")) {
//                System.out.println("selectednoMeters");
                height = height/feetconversion;
            }
            bmi = weight/(height*height);
        }
        return bmi;
    }
    public String returndiagnosis(double bmi){
        if(bmi < 18.5) return "underweight";
        if(bmi < 25 && bmi >= 18.5) return "normal";
        if(bmi >= 25 && bmi < 30) return "overweight";
        if(bmi > 30) return "obese";
        return "value out of range";
    }
}
