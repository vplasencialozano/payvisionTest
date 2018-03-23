import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by vplasencia on 3/22/2018.
 */
public class ListOfSteps {

    HttpClientTest httpClientTest = new HttpClientTest();



    @Given("an endpoint to retrieve transaction to test for $user and $password")
    public void setdatas(final String name, final String password) {

        httpClientTest.setUsername(name);
        httpClientTest.setPassword(password);

    }

    @Given("filters $filters and values $values")
    public void setfilter(final String filter,final String values) {

        httpClientTest.setFilterMap(new ArrayList<String>(Arrays.asList(filter.split(","))),new ArrayList<String>(Arrays.asList(values.split(","))));
    }

    @When("call endpoint to retrieve the transactions")
    public void callEndpoint() {
        httpClientTest.callEndpoint();
    }

    @Then("information is as expected")
    public void checkResults() {
        httpClientTest.checkResults();
    }


}
