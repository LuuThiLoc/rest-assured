package model;

public class IssueFields {
    private Fields fields;

    public IssueFields(Fields fields) {
        this.fields = fields;
    }

    public Fields getFields() {
        return fields;
    }

    public static class Fields {
        private String summary;
        private Project project;
        private IssueType issuetype;

        public Fields(String summary, Project project, IssueType issuetype) {
            this.summary = summary;
            this.project = project;
            this.issuetype = issuetype;
        }

        public String getSummary() {
            return summary;
        }

        public Project getProject() {
            return project;
        }

        public IssueType getIssueType() {
            return issuetype;
        }
    }

    public static class Project {
        private String key;

        public Project(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }

    public static class IssueType {
        private String id;

        public IssueType(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }
    }

//    public static void main(String[] args) {
//        String summary = "This is my summary";
//        IssueType issueType = new IssueType("10001");
//        Project project = new Project("SDETPRO");
//        Fields fields = new Fields(summary, project, issueType);
//        IssueFields issueFields = new IssueFields(fields);
//
//        System.out.println(new Gson().toJson(issueFields));
//    }
}


