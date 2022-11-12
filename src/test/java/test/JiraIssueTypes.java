package test;

import model.RequestCapability;
import utils.ProjectInfo;

public class JiraIssueTypes implements RequestCapability {

    public static void main(String[] args) {
        String baseUri = "https://sdetpro-tutorial.atlassian.net";
        String projectKey = "SDETPRO";

        ProjectInfo projectInfo = new ProjectInfo(baseUri, projectKey);
        System.out.println("Task ID: " + projectInfo.getIssueTypeId("task"));
        System.out.println("Subtask: " + projectInfo.getIssueTypeId("subtask"));
        System.out.println("Epic: " + projectInfo.getIssueTypeId("epic"));
    }
}
