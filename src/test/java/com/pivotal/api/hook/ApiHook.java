package com.pivotal.api.hook;

import java.util.List;
import java.util.Map;

import cucumber.api.java.After;
import io.restassured.path.json.JsonPath;
import com.pivotal.api.client.RequestManager;
import com.pivotal.api.util.DataInterpreter;
import com.pivotal.api.util.Helper;
import com.pivotal.api.util.SharedVariableList;

/**
 * Created by pivotal-test Team.
 */
public class ApiHook {

    private static final String PROJECT_ENDPOINT = "/projects/[%s.id]";
    private static final String WORKSPACE_ENDPOINT = "/my/workspaces/[%s.id]";
    private static final String WORKSPACE_TYPE = "workspace";
    private static final String PROJECT_TYPE = "project";
    private static final String AT = "AUTO";
    private static final String NAME = "name";
    private final Helper helper;

    /**
     * Api Hook constructor using Dependency Injection.
     *
     * @param helper object utility instance.
     */
    public ApiHook(final Helper helper) {
        this.helper = helper;
    }

    /**
     * This method clean all the values of the Shared Variables List.
     * This hook will be executed at the last.
     */
    @After(order = 1000)
    public void cleanSharedVariables() {
        SharedVariableList.cleanList();
    }

    /**
     * Hook for delete a certain project specified for the helper content.
     */
    @After("@DeleteProject")
    public void deleteProject() {
        deleteRequests(PROJECT_TYPE, PROJECT_ENDPOINT);
    }

    /**
     * Hook for delete a workspace specified for the helper content.
     */
    @After("@DeleteWorkspace")
    public void deleteWorkspace() {
        deleteRequests(WORKSPACE_TYPE, WORKSPACE_ENDPOINT);
    }

    /**
     * This method perform the delete request of all elements of certain type stored in the
     * Shared variable list.
     *
     * @param type     is the type of element.
     * @param endpoint is the endpoint format.
     */
    private void deleteRequests(final String type, final String endpoint) {
        SharedVariableList.getList().stream()
                .filter(variable -> variable.getType().equals(type))
                .forEach(element -> {
                    String format = String.format(endpoint, element.getName());
                    RequestManager.delete(DataInterpreter.builtEndPoint(format));
                });
    }

    /**
     * Hook for delete a certain project specified for the helper content.
     */
    @After("@DeleteSingleProject")
    public void deleteSingleProject() {
        JsonPath jsonPath = new JsonPath(RequestManager.get("/projects").asString());
        List<Map<String, Object>> projects = jsonPath.get();
        for (final Map<String, Object> map : projects) {
            if (map.get("name").equals(helper.getProjectVariable())) {
                RequestManager.delete(String.format("/projects/%s", map.get("id").toString()));
            }
        }
    }

    /**
     * Hook for delete a certain workspace specified for the helper content.
     */
    @After("@DeleteSingleWorkspace")
    public void deleteSingleWorkSpace() {
        JsonPath jsonPath = new JsonPath(RequestManager.get("/my/workspaces").asString());
        List<Map<String, Object>> workspace = jsonPath.get();
        for (final Map<String, Object> map : workspace) {
            if (map.get(NAME).equals(helper.getWorkspaceVariable())) {
                RequestManager.delete(String.format("/my/workspaces/%s", map.get("id").toString()));
            }
        }
    }

    /**
     * Hook for delete all projects with prefix AUTO.
     */
    @After("@DeleteProjectsByPrefix")
    public void deleteProjectsByPrefix() {
        JsonPath jsonPath = new JsonPath(RequestManager.get("/projects").asString());
        List<Map<String, Object>> project = jsonPath.get();
        for (final Map<String, Object> map : project) {
            if (map.get(NAME).toString().contains(AT)) {
                RequestManager.delete(String.format("/projects/%s", map.get("id").toString()));
            }
        }
    }

    /**
     * Hook for delete all workspace with prefix AUTO.
     */
    @After("@DeleteWorkspaceByPrefix")
    public void deleteWorkspaceByPrefix() {
        JsonPath jsonPath = new JsonPath(RequestManager.get("/my/workspaces").asString());
        List<Map<String, Object>> workspace = jsonPath.get();
        for (final Map<String, Object> map : workspace) {
            if (map.get("name").toString().contains(AT)) {
                RequestManager.delete(String.format("/my/workspaces/%s", map.get("id").toString()));
            }
        }
    }
}
