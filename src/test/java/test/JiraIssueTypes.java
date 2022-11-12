package test;

import model.RequestCapability;
import utils.ProjectInfo;

public class JiraIssueTypes implements RequestCapability {

    public static void main(String[] args) {
        String baseUri = "https://sdetpro-tutorial.atlassian.net";
        String projectKey = "SDETPRO1";

        ProjectInfo projectInfo = new ProjectInfo(baseUri, projectKey);
        System.out.println("TaskId: " + projectInfo.getIssueTypeId("task"));
        System.out.println("Sub-task: " + projectInfo.getIssueTypeId("sub-task"));
        System.out.println("Epic: " + projectInfo.getIssueTypeId("epic"));
        System.out.println("Bug: " + projectInfo.getIssueTypeId("bug"));
        System.out.println("Story: " + projectInfo.getIssueTypeId("story"));
    }
}
