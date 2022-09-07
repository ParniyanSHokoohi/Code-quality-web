package repository;


import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import IO.CSVWriter;
import model.GithubModel;
import model.WorkflowStatusModel;
import service.GitHubService;
import service.SonarQubeService;


public class ReposAnalyzer {
	
    ReposIterator reposIterator = new ReposIterator();

	public void Analyze(String filename, String sonarPropertiesPath, String commitYmlPath) throws Exception {  
	System.out.println("START ANALYZE"); 
	
    // load the github-list 
    List<GithubModel> reposGithubList = reposIterator.LoadReposGithubList(filename);
    
    if(reposGithubList == null)
    	return;
    
    for (GithubModel githubModel: reposGithubList) {
    	
        System.out.println("### Start: Project " + githubModel.getRepoName());

        // first step. Fork repository
        int forkStatus = ForkRepository(githubModel);
        
        if(forkStatus != 202){
            continue;
        }
        System.out.println("### forkStatus:" + forkStatus+ ": success!");
        
        // second step. Commit associated files
        int commitStatus = CommitAssociatedFiles(githubModel.getUsername(),sonarPropertiesPath, commitYmlPath, githubModel);
        if(commitStatus != 201){
            continue;
        }
        System.out.println("### commitStatus:" + commitStatus+ ": success!");
                    
        System.out.println("### Wait for Github workflow action to be finished");
        
        // third step. Wait for Github workflow action to be finished
        WorkflowStatusModel workflowStatus = WaitForGithubAction(githubModel, 1800);
        
        System.out.println("### WorkflowStatus:" + workflowStatus);
        
        if (workflowStatus == null){
            System.out.println("WORKFLOW COULD NOT BE INITIALIZED.");
        }
        else if (!workflowStatus.getStatus().equals("completed") || !workflowStatus.getConclusion().equals("success")){
            System.out.println("PROJECT COULD NOT BE ANALYZED: " + githubModel.getRepoName());
            System.out.println("Workflow Status: " + workflowStatus.getStatus() + "|" + workflowStatus.getConclusion());
        }else {
        	  System.out.println("### WorkflowStatus:" + workflowStatus+ ": success!");
        }
        
        System.out.println("### End: Project " + githubModel.getRepoName());
    }      
   
    System.out.println("##### END ANALYZE #####");
	}

	
	/**
	 * Fork is the first step of the analyze-process 
	 * @param csvGithubModel
	 * @return
	 * @throws IOException
	 */
    private int ForkRepository(GithubModel csvGithubModel) throws IOException {
        
    	// 1. Fork repository
        System.out.println("Fork repository started...");
        
        int forkStatus =  GitHubService.ForkRepository(csvGithubModel.getUsername(), csvGithubModel.getRepoName());
        
        if (forkStatus != 202){
            // try again once
            forkStatus = GitHubService.ForkRepository(csvGithubModel.getUsername(), csvGithubModel.getRepoName());
            if (forkStatus != 202){
                System.out.println("PROJECT COULD NOT BE FORKED: " + csvGithubModel.getRepoName());
            }
        }
        return forkStatus;
    }

	
    /**
     * Commit is the second step
     * @param owner
     * @param sonarProperties
     * @param commitYml
     * @param csvGithubModel
     * @return
     * @throws IOException
     */
    private int CommitAssociatedFiles(String owner, String sonarProperties, String commitYml, GithubModel csvGithubModel) throws IOException {
        
    	// Commit associated files
    	System.out.println("Commit started...");

        int commitPropStatus = GitHubService.CommitPropertie(sonarProperties, csvGithubModel.getRepoName(), csvGithubModel.getRepoName());
        
        if (commitPropStatus != 201){
            
        	// try again once
            commitPropStatus = GitHubService.CommitPropertie(sonarProperties, csvGithubModel.getRepoName(), csvGithubModel.getRepoName());
            
            if (commitPropStatus != 201){
                System.out.println("PROJECT COULD NOT COMMIT PROPERTY FILE: " + csvGithubModel.getRepoName());
                System.out.println("commitPropStatus: " + commitPropStatus);
                return commitPropStatus;
            }
        }

        int commitYmlStatus = GitHubService.CommitYml(new File(commitYml), csvGithubModel.getRepoName());
        if (commitYmlStatus != 201){
            // try again once
            commitYmlStatus = GitHubService.CommitYml(new File(commitYml), csvGithubModel.getRepoName());
            if (commitYmlStatus != 201){
                System.out.println("PROJECT COULD NOT COMMIT YML FILE: " + csvGithubModel.getRepoName());
            }
        }
        return commitYmlStatus;
    }

	/**
	 * the third step is the githup-action
	 * @param csvGithubModel
	 * @param limitSeconds
	 * @return
	 * @throws InterruptedException
	 * @throws IOException
	 */
    private WorkflowStatusModel WaitForGithubAction(GithubModel csvGithubModel, int limitSeconds) throws InterruptedException, IOException {
        
    	//Wait for Github workflow action to be finished
        WorkflowStatusModel workflowStatus = null;
        int seconds = 0;
        int workflowStatusEqualsNullCounter = 0;
        while (workflowStatus == null || (workflowStatus.getStatus().equals("in_progress") || workflowStatus.getStatus().equals("queued")) && seconds <= limitSeconds){
            if (workflowStatus == null){
                workflowStatusEqualsNullCounter++;
            }
            if (workflowStatusEqualsNullCounter > 6){
                break;
            }
            int waitSeconds = 5;
            Thread.sleep(waitSeconds * 1000);
            seconds += waitSeconds;
            System.out.println("The Thread waits since " + seconds + " seconds.");
            workflowStatus = GitHubService.getStatusOfWorkflow(csvGithubModel.getRepoName());
        }

        return workflowStatus;
    }
	

    /***
     * fourth step 4, this method create the metrics from sonarqube-analyze with customer metric-columns.
     * @param metrics
     * @param amount
     * @throws IOException
     */
    public void ExportProjectMetrics(String[] metrics, int amount) throws IOException {
        
    	List<String> sonarQubeProjectKeys = SonarQubeService.GetSonarQubeProjectKeys(amount);
    	
        List<HashMap<String, String>> sonarQubeMetrics = new LinkedList<>();
        
        for (String key: sonarQubeProjectKeys){
            sonarQubeMetrics.add(SonarQubeService.GetMeasures(key, metrics));
        }
        
        CSVWriter csvWriter = new CSVWriter();
        csvWriter.CreateCSVFile(sonarQubeMetrics, metrics);
    }
}
