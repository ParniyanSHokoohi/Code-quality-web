package admin;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import model.SonarQubeModel;
import oauth.OauthHelper;
import service.GitHubService;
import service.SonarQubeService;


public abstract class AdminModel {
	
   /**
   * Only for developper.
   * Delete Project by key from sonarqube. 
   */
	public static void DeleteSonarQubeByKey() throws IOException { 
		
		List<SonarQubeModel>sonarKeyList = SonarQubeService.GetSonarQubeProjects(500);
	      
	      for(SonarQubeModel sonarKey: sonarKeyList){ 
	      	System.out.println("Key:"+sonarKey.getKey());
	      	SonarQubeService.DeleteProjectByKey(sonarKey.getKey());
	      	System.out.println("Key:"+sonarKey.getKey()+" was deleted!");
	      }     
		}
	
	 public static void DeleteGithub() throws IOException {
	    	
    	 List<String>githubProjet = GitHubService.GetAllRepos();
    	 
    	 int status = 400; // Default: data no found 
    	 
    	 for(String hubProjet: githubProjet){  
 			
	        	status = DeleteGithubByName(hubProjet);
	        	
	        	System.out.println("status:"+status);
	        	
	        	if(status != 204)
	        	  continue;
	        	System.out.println("hubProjet:"+hubProjet +" was deleted success!");
	      } 
    	 
         /*
    	 while (githubProjet.size()>0 ) {
    			     
    		 for(String hubProjet: githubProjet){  
    	        			
    			        	status = DeleteGithubByName(hubProjet);
    			        	
    			        	System.out.println("status:"+status);
    			        	
    			        	if(status != 204)
    			        	  continue;
    			        	System.out.println("hubProjet:"+hubProjet +" was deleted success!");
    			        } 
    		 if(status != 403)
    			 githubProjet = GitHubService.GetAllRepos();
    			        
    	     // System.out.println("#### DELETED PROJECT were success!"); 
    	 }  */
    	 
    	 if(GitHubService.GetAllRepos().size() == 0)
    		 System.out.println("#### DELETED PROJECT were success!"); 
    	 else {
    		 System.out.println("#### number of UNDELETED PROJECT:" +GitHubService.GetAllRepos().size()); 
    	 }
    }

    
   /***
	* Only for development: delete project from github by name
	* @param projectName
	* @throws IOException
	*/
    private static int DeleteGithubByName(String projectName) throws IOException {
        URL url = new URL("https://api.github.com/repos/raoulnzefa/" + projectName);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("DELETE");
        con.setRequestProperty("accept", "application/vnd.github.v3+json");
        con.setRequestProperty("Authorization", OauthHelper.GetGithubAuthorization());
        
        return con.getResponseCode();
    }


}
