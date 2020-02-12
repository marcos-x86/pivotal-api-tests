package com.pivotal.api.client;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import cucumber.api.java.en.Then;
import com.pivotal.api.util.Helper;

/**
 * Class containing Verification Steps.
 */
public class VerificationSteps {

    private final Helper helper;

    /**
     * Step definition constructor using dependence injection.
     *
     * @param helper is the helper utility class instance.
     */
    public VerificationSteps(final Helper helper) {
        this.helper = helper;
    }

    /**
     * Step definition that perform the assert of the status code.
     *
     * @param status the received status code.
     */
    @Then("^the status code should be (\\d+)$")
    public void theStatusCodeShouldBe(int status) {
        assertEquals(helper.getRequestStatus(), status);
    }

    /**
     * Step definition that perform the assert of the status code message.
     *
     * @param errorMessage about status code.
     */
    @Then("^the status code message \"([^\"]*)\" should be displayed$")
    public void verifyThatValidationErrorMessageIsDiplayed(final String errorMessage) {
        assertTrue(helper.getRequestBody().contains(errorMessage));
    }
}
