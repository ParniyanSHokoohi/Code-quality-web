package service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import model.WorkflowStatusModel;
import oauth.OauthHelper;

public abstract class GitHubService {

	/***
	 * this method forks a projet from extern-github to our github 
	 * @param owner
	 * @param projectName
	 * @return
	 * @throws IOException
	 */
	public static int ForkRepository(String owner, String projectName) throws IOException {
        URL url = new URL("https://api.github.com/repos/" + owner + "/" + projectName + "/forks");
        
        System.out.println("fork url:"+projectName);
        System.out.println(url);
        
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("accept", "application/vnd.github.v3+json");
        con.setRequestProperty("Authorization", OauthHelper.GetGithubAuthorization());
        return con.getResponseCode();
    }
	
	/***
	 * One commit occurs into the repository in our github
	 * @param filename
	 * @param projectName
	 * @param projectKey
	 * @return
	 * @throws IOException
	 */
	public static int CommitPropertie(String filename, String projectName, String projectKey) throws IOException {
        URL url = new URL("https://api.github.com/repos/raoulnzefa/" + projectName + "/contents/sonar-project.properties");

        Properties prop = new Properties();
        InputStream in = new FileInputStream(filename);
        prop.load(in);
        prop.setProperty("sonar.projectKey", projectKey);
        prop.setProperty("sonar.sources", ".");
        prop.store(new FileOutputStream(filename), null);

        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("PUT");
        con.setRequestProperty("accept", "application/vnd.github.v3+json");
        con.setRequestProperty("Authorization", OauthHelper.GetGithubAuthorization());

        Map<String, String> requestParameter = new HashMap<>();
        requestParameter.put("message", "Add sonar-project.properties");
        byte[] fileContent = FileUtils.readFileToByteArray(new File(filename));
        requestParameter.put("content", Base64.getEncoder().encodeToString(fileContent));

        JSONObject jsonObject = new JSONObject(requestParameter);

        con.setDoOutput(true);
        DataOutputStream out = new DataOutputStream(con.getOutputStream());
        out.writeBytes(jsonObject.toString());
        out.flush();
        out.close();

        return con.getResponseCode();
    }

	
	public static int CommitYml(File file, String projectName) throws IOException {
	        URL url = new URL("https://api.github.com/repos/raoulnzefa/" + projectName + "/contents/.github/workflows/build.yml");

	        HttpURLConnection con = (HttpURLConnection) url.openConnection();
	        con.setRequestMethod("PUT");
	        con.setRequestProperty("accept", "application/vnd.github.v3+json");
	        con.setRequestProperty("Authorization", OauthHelper.GetGithubAuthorization());

	        Map<String, String> requestParameter = new HashMap<>();
	        requestParameter.put("message", "Add build.yml");
	        byte[] fileContent = FileUtils.readFileToByteArray(file);
	        requestParameter.put("content", Base64.getEncoder().encodeToString(fileContent));

	        JSONObject jsonObject = new JSONObject(requestParameter);

	        con.setDoOutput(true);
	        DataOutputStream out = new DataOutputStream(con.getOutputStream());
	        out.writeBytes(jsonObject.toString());
	        out.flush();
	        out.close();

	        return con.getResponseCode();
	    }
	
	
	   public static WorkflowStatusModel getStatusOfWorkflow(String projectName) throws IOException {
	        URL url = new URL("https://api.github.com/repos/raoulnzefa/"+ projectName + "/actions/runs");
	        HttpURLConnection con = (HttpURLConnection) url.openConnection();
	        con.setRequestMethod("GET");
	        con.setRequestProperty("accept", "application/vnd.github.v3+json");
	        con.setRequestProperty("Authorization", OauthHelper.GetGithubAuthorization());


	        try{
	            JSONObject response = new JSONObject(ReadResponse(con));
	            JSONArray measures = response.getJSONArray("workflow_runs");
	            for (int i = 0; i < measures.length(); i++){
	                if (((JSONObject) measures.get(i)).get("name").equals("Sonarqube")){
	                    String status = ((JSONObject) measures.get(i)).get("status").toString();
	                    String conclusion = ((JSONObject) measures.get(i)).get("conclusion").toString();
	                    return new WorkflowStatusModel(status, conclusion);
	                }
	            }
	        } catch(JSONException exp){
	            System.out.println(projectName);
	        }

	        return null;
	    }
	   
	   
	   public static List<String> GetAllRepos() throws IOException {
	        URL url = new URL("https://api.github.com/users/raoulnzefa/repos");

	        HttpURLConnection con = (HttpURLConnection) url.openConnection();
	        con.setRequestMethod("GET");
	        con.setRequestProperty("accept", "application/vnd.github.v3+json");
	        con.setRequestProperty("Authorization", OauthHelper.GetGithubAuthorization());
	        
	        List<String> repoStrings = new LinkedList<>();
	        
	        JSONArray repos = new JSONArray(ReadResponse(con));
	       
	        for (int i = 0; i < repos.length(); i++){
	            repoStrings.add((String) ((JSONObject) repos.get(i)).get("name"));  // name
	        } 

	        return repoStrings;
	    }
	   
	    
	    /***
	     * Helper method
	     * @param con
	     * @return
	     * @throws IOException
	     */
	    private static String ReadResponse(HttpURLConnection con) throws IOException {
	        int status = con.getResponseCode();

	        BufferedReader in = null;

	        if (status > 299) {
	            in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
	        } else {
	            in = new BufferedReader(new InputStreamReader(con.getInputStream()));
	        }
	        String inputLine;
	        StringBuffer content = new StringBuffer();
	        while ((inputLine = in.readLine()) != null) {
	            content.append(inputLine);
	        }
	        in.close();
	        return content.toString();
	    }
}
