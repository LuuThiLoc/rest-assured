package test;

import api_flow.IssueFlow;
import org.testng.annotations.Test;

public class JiraIssueCRUD extends BaseTest {

    @Test
    public void testE2EFlow() {

        // CRUD
        IssueFlow issueFlow = new IssueFlow(baseUri, projectKey, request, "task");

        System.out.println("------>CREATE");
        issueFlow.createIssue();

        System.out.println("------>READ");
        issueFlow.verifyIssueDetails();

        System.out.println("------>UPDATE");
        issueFlow.updateIssue("In Progress");
        issueFlow.verifyIssueDetails();

        System.out.println("------>DELETE");
        issueFlow.deleteIssue();
    }

    @Test
    public void createIssue() {
        IssueFlow issueFlow = new IssueFlow(baseUri, projectKey, request, "task");
        System.out.println("------>CREATE");
        issueFlow.createIssue();
        System.out.println("------>READ");
        issueFlow.verifyIssueDetails();
    }

    @Test
    public void updateIssue() {
        IssueFlow issueFlow = new IssueFlow(baseUri, projectKey, request, "task");
        System.out.println("------>CREATE");
        issueFlow.createIssue();
        System.out.println("------>UPDATE");
        issueFlow.updateIssue("In Progress");
        issueFlow.verifyIssueDetails();
    }

    @Test
    public void deleteIssue() {
        IssueFlow issueFlow = new IssueFlow(baseUri, projectKey, request, "task");
        System.out.println("------>CREATE");
        issueFlow.createIssue();
        System.out.println("------>DELETE");
        issueFlow.deleteIssue();
    }
}
