package model;

public class SonarQubeModel {
	
	private final String key;
    private final String name;
    private final String qualifier;
    private final String project;

	public SonarQubeModel(String key, String name, String qualifier, String project) {
		this.key = key;
		this.name = name;
		this.qualifier = qualifier;
		this.project = project;
	}

	public String getKey() {
		return key;
	}

	public String getName() {
		return name;
	}

	public String getQualifier() {
		return qualifier;
	}

	public String getProject() {
		return project;
	}
}
