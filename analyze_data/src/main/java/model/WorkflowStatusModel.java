package model;

public class WorkflowStatusModel {

	 private final String status;
	 private final String conclusion;
	 
	 public WorkflowStatusModel (String status, String conclusion) {
	        this.status = status;
	        this.conclusion = conclusion;
		}
		public String getStatus() {
			return status;
		}
		public String getConclusion() {
			return conclusion;
		}
}
