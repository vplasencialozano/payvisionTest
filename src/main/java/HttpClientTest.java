import com.google.common.base.Verify;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.fail;

/**
 * Created by vplasencia on 3/22/2018.
 */
public class HttpClientTest {


    final String uri =
            "https://code-challenge:payvisioner@jovs5zmau3.execute-api.eu-west-1.amazonaws.com/prod/transactions";

    private String username;

    private String password;

    private Map<String, String> params;


    private RestTemplate restTemplate;

    private ResponseEntity<String> result;

    private JSONObject jsonObj;

    private HttpStatus status;

    private String response;

    public HttpClientTest() {
        restTemplate = new RestTemplate();
        params = new HashMap<String, String>();
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public void setFilterMap(ArrayList<String> filerlist, ArrayList<String> valuelist) {


        for (int i = 0; i < filerlist.size(); ++i) {

            params.put(filerlist.get(i), valuelist.get(i));
        }

    }


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
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (HttpServerErrorException e) {

            fail("Call endpoint has an error : " + e.getMessage());

        }

    }

    public void checkResults() {


        Verify.verify(status.is2xxSuccessful(), "Results are valid", "Results are false");

        if (jsonObj != null && jsonObj.length() > 0) {
            Verify.verify(jsonObj.length() > 0, "Json As expected", "Json is not as expected");

        }


    }

}
