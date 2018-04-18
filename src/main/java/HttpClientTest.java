import com.google.common.base.Verify;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static junit.framework.TestCase.fail;

/**
 * Created by vplasencia on 3/22/2018.
 */
public class HttpClientTest {


    private static final Logger LOGGER = Logger.getLogger(HttpClientTest.class.getName());

    /**
     * The Uri.
     */
    final String uri =
            "https://code-challenge:payvisioner@jovs5zmau3.execute-api.eu-west-1.amazonaws.com/prod/transactions";

    private String username;

    private String password;

    private Map<String, String> params;


    private RestTemplate restTemplate;

    private ResponseEntity<String> result;

    private JSONObject jsonObj;

    private JSONArray jsonArray;

    private HttpStatus status;

    private String response;

    /**
     * Instantiates a new Http client test.
     */
    public HttpClientTest() {
        restTemplate = new RestTemplate();
        params = new HashMap<String, String>();
    }

    /**
     * Sets username.
     *
     * @param username the username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Sets password.
     *
     * @param password the password
     */
    public void setPassword(String password) {
        this.password = password;
    }


    /**
     * Sets filter map.
     *
     * @param filerlist the filerlist
     * @param valuelist the valuelist
     */
    public void setFilterMap(ArrayList<String> filerlist, ArrayList<String> valuelist) {

        params = new HashMap<String, String>();
        for (int i = 0; i < filerlist.size(); ++i) {

            params.put(filerlist.get(i), valuelist.get(i));
        }

    }


    /**
     * Call endpoint.
     */
    public void callEndpoint() {

        try {


            restTemplate.getInterceptors().add(new BasicAuthorizationInterceptor(username, password));

            if (params.isEmpty()) {
                result = restTemplate.getForEntity(uri, String.class);

            } else {

                UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(uri);

                for (Map.Entry<String, String> entry : params.entrySet()) {
                    builder.queryParam(entry.getKey(), entry.getValue());
                }
                //System.out.println( builder.build().encode().toUri());
                result = restTemplate.exchange(
                        builder.build().encode().toUri(),
                        HttpMethod.GET,
                        new HttpEntity(new HttpHeaders()),
                        String.class);


            }
            status = result.getStatusCode();
            response = result.getBody();

            if (!response.equalsIgnoreCase("[]")) {
                jsonObj = new JSONObject(response.replace("[", ""));

                jsonArray = new JSONArray(result.getBody());
            } else {
                jsonObj = new JSONObject();
                jsonArray = null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (HttpServerErrorException e) {

            fail("Error calling endpoint : " + uri + "with params: " + params + "--> " + e.getMessage());

        }

    }



    /**
     * Check results by filters.
     */
    public void checkResultsByFilters() {

        try {

            if (jsonArray != null) {

                if (params.get("action") != null) {

                    checkFilterAction();


                } else if (params.get("currencyCode") != null) {

                    checkFilterCurrencyCode();

                }
                if (params.get("orderBy") != null) {

                    checkFilterOrderBy();
                }
            } else {
                LOGGER.log(Level.INFO, "JSON is empty, no transactions were found.");
            }

        } catch (JSONException e) {
            e.printStackTrace();

        }
    }

    /**
     * checkFilterOrderBy
     * @throws JSONException
     */
    private void checkFilterOrderBy() throws JSONException {

        Calendar previousDate = Calendar.getInstance();

        Calendar currentDate = Calendar.getInstance();

        ArrayList<String> transactionsSaved = new ArrayList<String>();

        for (int i = 0; i < jsonArray.length(); ++i) {
            JSONObject jsonObject = (JSONObject) jsonArray.getJSONObject(i).get("card");
            transactionsSaved.add(jsonObject.toString());

            if (i == 0) {

                previousDate.set(Calendar.YEAR, Integer.valueOf(jsonObject.get("expiryYear").toString()));
                previousDate.set(Calendar.MONTH, Integer.valueOf(jsonObject.get("expiryMonth").toString()));

            } else {
                currentDate.set(Calendar.YEAR, Integer.valueOf(jsonObject.get("expiryYear").toString()));
                currentDate.set(Calendar.MONTH, Integer.valueOf(jsonObject.get("expiryMonth").toString()));

                if (params.get("orderBy").equals("date")) {
                    Verify.verify(currentDate.before(previousDate), "Order by date is wrong: %s", transactionsSaved);

                } else {
                    Verify.verify(currentDate.after(previousDate), "Order by date is wrong: %s", transactionsSaved);

                }
                previousDate.set(Calendar.YEAR, Integer.valueOf(jsonObject.get("expiryYear").toString()));
                previousDate.set(Calendar.MONTH, Integer.valueOf(jsonObject.get("expiryMonth").toString()));

            }
        }
    }

    /**
     * checkFilterCurrencyCode
     * @throws JSONException
     */
    private void checkFilterCurrencyCode() throws JSONException {
        for (int i = 0; i < jsonArray.length(); ++i) {
            Verify.verify(jsonArray.getJSONObject(i).get("currencyCode") != null, "CurrencyCode is empty");
            Verify.verify(jsonArray.getJSONObject(i).get("currencyCode").equals(params.get("currencyCode")), "CurrencyCode is not valid, CurrencyCode expected was %s and it was received %s ", params.get("currencyCode"), jsonObj.get("currencyCode"));
        }
    }

    /**
     * checkFilterAction
     * @throws JSONException
     */
    private void checkFilterAction() throws JSONException {
        for (int i = 0; i < jsonArray.length(); ++i) {
            Verify.verify(jsonArray.getJSONObject(i).get("action") != null, "Action is empty ");
            Verify.verify(jsonArray.getJSONObject(i).get("action").equals(params.get("action")), "Action is not valid, action expected was %s and it was received %s ", params.get("action"), jsonObj.get("action"));
        }
    }

}
