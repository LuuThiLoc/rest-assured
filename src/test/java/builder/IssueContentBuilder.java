package builder;

import model.IssueFields;

public class IssueContentBuilder {
    private IssueFields issueFields;

    public String build(String projectKey, String taskTypeId, String summary){
        IssueFields.IssueType issueType = new IssueFields.IssueType(taskTypeId);
        IssueFields.Project project = new IssueFields.Project(projectKey);
        IssueFields.Fields fields = new IssueFields.Fields(summary, project, issueType);
        issueFields = new IssueFields(fields); // Update value for issueFields
        return BodyJSONBuilder.getJSONContent(issueFields);
    };

    // Cần get ra từng field của issueFields bên main để print out -> getter
    // Cần tạo ra object của class IssueContentBuilder để class bên main sử dụng -> new IssueContentBuilder()
    public IssueFields getIssueFields() {
        return issueFields;
    }

}
