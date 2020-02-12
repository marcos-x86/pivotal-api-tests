package com.pivotal.api.client;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import cucumber.api.java.en.And;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import com.pivotal.api.util.DataInterpreter;
import com.pivotal.api.util.Helper;
import com.pivotal.api.util.SharedVariableList;

/**
 * Created by pivotal-test Team.
 */
public class RequestSteps {

    private static final String NAME = "name";
    private static final int LOW = 10;
    private static final int HIGH = 99;
    private static final String AT = "AUTO";
    private final Helper helper;
    private Response response;

    /**
     * Step definition constructor using dependence injection.
     *
     * @param helper is the helper utility class instance.
     */
    public RequestSteps(final Helper helper) {
        this.helper = helper;
    }

    /**
     * Step definition to store the response data to a shared variable.
     *
     * @param name the name of the shared variable.
     */
    @And("^stored as \\[([^\"]*)]$")
    public void storedAsProject(final String name) {
        SharedVariableList.addVariable(name, response);
    }

    /**
     * Step definition to perform a POST or PUT request.
     *
     * @param method is the request type.
     * @param param  is the specified end point.
     * @param map    is the map body content.
     */
    @When("^a \"(POST|PUT)\" request to \"([^\"]*)\" with$")
    public void aRequestToWith(final RequestType method, final String param, final Map<String, String> map) {
        Map<String, String> myMap = new HashMap<>(map);
        appendRandomNumbers(myMap);
        String endpoint = DataInterpreter.builtEndPoint(param);
        response = RequestType.POST.equals(method)
                ? RequestManager.post(endpoint, myMap)
                : RequestManager.put(endpoint, myMap);
        helper.setRequestStatus(response.getStatusCode());
        helper.setBody(response.body().asString());
    }

    /**
     * Step definition to perform a GET or DELETE request.
     *
     * @param method is the request type.
     * @param param  is the specified end point.
     */
    @When("^a \"(GET|DELETE)\" request to \"([^\"]*)\"$")
    public void aRequestTo(final RequestType method, final String param) {
        String endpoint = DataInterpreter.builtEndPoint(param);
        response = RequestType.GET.equals(method)
                ? RequestManager.get(endpoint)
                : RequestManager.delete(endpoint);
        helper.setRequestStatus(response.getStatusCode());
        helper.setBody(response.body().asString());
    }

    /**
     * This method adds a random number to the first map element received as a parameter.
     *
     * @param myMap this map contains the setting.
     */
    private void appendRandomNumbers(final Map<String, String> myMap) {
        for (Map.Entry<String, String> entry : myMap.entrySet()) {
            if (entry.getValue().length() > 0 && entry.getKey().equalsIgnoreCase(NAME)) {
                myMap.put(entry.getKey(), String.format("%s%s%d", AT, entry.getValue(), new Random()
                        .nextInt(HIGH - LOW) + LOW));
            }
        }
    }
}
