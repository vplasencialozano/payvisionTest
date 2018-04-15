import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by vplasencia on 3/22/2018.
 */
public class ListOfSteps {

    /**
     * The Http client test.
     */
    HttpClientTest httpClientTest = new HttpClientTest();


    /**
     * Sets data.
     *
     * @param name     the name
     * @param password the password
     */
    @Given("an endpoint to retrieve transaction to test for $user and $password")
    public void setdata(final String name, final String password) {

        httpClientTest.setUsername(name);
        httpClientTest.setPassword(password);

    }

    /**
     * Sets filters.
     *
     * @param filter the filter
     * @param values the values
     */
    @Given("filters $filters and values $values")
    public void setfilters(final String filter, final String values) {

        httpClientTest.setFilterMap(new ArrayList<String>(Arrays.asList(filter.split(","))), new ArrayList<String>(Arrays.asList(values.split(","))));
    }

    /**
     * Call endpoint.
     */
    @When("call endpoint to retrieve the transactions")
    public void callEndpoint() {
        httpClientTest.callEndpoint();
    }

    /**
     * Check results.
     */
    @Then("information is as expected")
    public void checkResults() {
        httpClientTest.checkResultsByFilters();
    }


}
