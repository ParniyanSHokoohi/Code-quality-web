package model;

import java.net.URL;

public class GithubModel {

	private int rank;              
    private String username;  // owner
    private String repoName;  // name
    private String full_name;
    private URL repoURL;      // clone_url
    private int forks;
    private int watchers;
    private int open_issues;
    private int describtion_length;
    private int size;
    private int branches;
	    
	public GithubModel(int rank,String username,String repoName, URL url) {
		this.rank = rank;
		this.repoURL = url;
		this.repoName = repoName;	
		this.username = username;	
	}

	 public String getRepoName() {
			return repoName;
		}
		public void setRepoName(String repoName) {
			this.repoName = repoName;
		}
		
		public URL getRepoURL() {
			return repoURL;
		}
		public void setRepoURL(URL repoURL) {
			this.repoURL = repoURL;
		}
		
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}


		public int getRank() {
			return rank;
		}


		public void setRank(int rank) {
			this.rank = rank;
		}

		public String getFull_name() {
			return full_name;
		}


		public void setFull_name(String full_name) {
			this.full_name = full_name;
		}


		public int getForks() {
			return forks;
		}


		public void setForks(int forks) {
			this.forks = forks;
		}


		public int getWatchers() {
			return watchers;
		}


		public void setWatchers(int watchers) {
			this.watchers = watchers;
		}


		public int getOpen_issues() {
			return open_issues;
		}


		public void setOpen_issues(int open_issues) {
			this.open_issues = open_issues;
		}


		public int getDescribtion_length() {
			return describtion_length;
		}


		public void setDescribtion_length(int describtion_length) {
			this.describtion_length = describtion_length;
		}


		public int getSize() {
			return size;
		}


		public void setSize(int size) {
			this.size = size;
		}


		public int getBranches() {
			return branches;
		}


		public void setBranches(int branches) {
			this.branches = branches;
		}
}
