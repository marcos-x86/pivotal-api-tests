package com.pivotal.api.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import io.restassured.response.Response;

/**
 * Class containing Shared VariableList.
 */
public final class SharedVariableList {

    private static List<SharedVariable> shareVariablesList = new ArrayList<>();

    /**
     * Private constructor for the shared variable utility class.
     */
    private SharedVariableList() {
    }

    /**
     * This method add a Shared Variable type element to the list.
     *
     * @param sharedVariable is the shared Variable object.
     */
    private static void addSharedVariable(final SharedVariable sharedVariable) {
        shareVariablesList.add(sharedVariable);
    }

    /**
     * This method add a new variable to the list.
     *
     * @param name     is the name of the variable.
     * @param response is the response body.
     */
    public static void addVariable(final String name, final Response response) {
        addSharedVariable(new SharedVariable(name, response.jsonPath().get("")));
    }

    /**
     * This method find an attribute value for a specific variable name.
     *
     * @param variableName is the variable name.
     * @param attribute    is the specified attribute.
     * @return the string representation of the attribute.
     */
    public static String findAttribute(final String variableName, final String attribute) {
        Optional<SharedVariable> firstName =  shareVariablesList.stream()
                .filter(shareVariableItem -> variableName.equalsIgnoreCase(shareVariableItem.getName()))
                .findFirst();
        if (!firstName.isPresent()) {
            throw new IllegalArgumentException(String.format("Not exist %s attribute for %s variable.",
                    attribute, variableName));
        }
        return firstName.get().getAttributeValue(attribute);
    }

    /**
     * This method clean the list.
     */
    public static void cleanList() {
        shareVariablesList.clear();
    }

    /**
     * This method return the list.
     *
     * @return the share variable list.
     */
    public static List<SharedVariable> getList() {
        return shareVariablesList;
    }
}
