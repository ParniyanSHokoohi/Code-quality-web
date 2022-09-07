package service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import model.SonarQubeModel;
import oauth.OauthHelper;


public abstract class SonarQubeService {
	
	private final static String  SONARQUBE_URL = "https://ecdf-2a02-908-1514-d9c0-4837-3284-b079-89aa.eu.ngrok.io";
	
	 public static HashMap<String, String> GetMeasures(String projectName, String[] metrics) throws IOException {

	        // Build URL String
	        StringBuilder urlStringBuilder = new StringBuilder();
	        urlStringBuilder.append(SONARQUBE_URL + "/api/measures/component");
	        urlStringBuilder.append("?");
	        urlStringBuilder.append("metricKeys=");
	        
	        for (String metric: metrics){ 
	            urlStringBuilder.append(metric);
	            urlStringBuilder.append(",");
	        } 
	        
	          
	        urlStringBuilder.deleteCharAt(urlStringBuilder.toString().length() - 1);
	        urlStringBuilder.append("&");
	        urlStringBuilder.append("component=");
	        urlStringBuilder.append(projectName);

	        String urlString = urlStringBuilder.toString();
	        URL url = new URL(urlString);
	        
	        HttpURLConnection con = (HttpURLConnection) url.openConnection();
	        con.setRequestMethod("GET");
	        con.setRequestProperty("accept", "application/vnd.github.v3+json");
	        con.setRequestProperty("Authorization", OauthHelper.GetSonarAuthentication());

	        int status = con.getResponseCode();
	        
	        if (status != 200) {
	        
	            System.out.println("Project name: " + projectName + ",status:"+status);
	            return null;
	        }
	        
	        JSONObject response = new JSONObject(ReadResponse(con));
	        JSONArray measures = response.getJSONObject("component").getJSONArray("measures");  
	               
	        return CreateSonarQubeMetricMap(measures);  
	    } 
	 
	 
	 private static HashMap<String, String> CreateSonarQubeMetricMap(JSONArray measures){
	        HashMap<String, String> sonarQubeMetrics = new HashMap<>();
	        for (Object metric: measures){
	            if (metric instanceof JSONObject){
	                try {
	                    JSONObject metricJSON = (JSONObject) metric;
	                    String metricName = String.valueOf(metricJSON.get("metric"));
	                    String metricValue = String.valueOf(metricJSON.get("value"));
	                    sonarQubeMetrics.put(metricName, metricValue);
	                } catch (Exception e){
	                    System.out.println("Error: "+metric);
	                }
	            } else {
	                System.out.println("Could not converted to JSONObject");
	            }
	        }
	        return sonarQubeMetrics;
	    }
	 
	 
	 private static String ReadResponse(HttpURLConnection con) throws IOException {
	        int status = con.getResponseCode();
	        BufferedReader in = null;
	        StringBuffer content = new StringBuffer();

	        try {
	        	 if (status > 299) {
	                 in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
	             } else {
	                 in = new BufferedReader(new InputStreamReader(con.getInputStream()));
	             }
	                         
	             String inputLine;
	             
	             while ((inputLine = in.readLine()) != null) {
	                content.append(inputLine);
	             } 
	             
	             in.close();
	             
	        }catch(Exception e) {
	        	   System.out.println("Error:"+e.getMessage());
	        }
	        
	        return content.toString();
	    }

	 public static List<SonarQubeModel> GetSonarQubeProjects(int amount) throws IOException {
	        String urlString = SONARQUBE_URL+"/api/projects/search?qualifiers=TRK&ps=" + amount;
	        URL url = new URL(urlString);
	        
	        HttpURLConnection con = (HttpURLConnection) url.openConnection();
	        con.setRequestMethod("GET");
	        con.setRequestProperty("accept", "application/vnd.github.v3+json");
	        con.setRequestProperty("Authorization", OauthHelper.GetSonarAuthentication());
	        
	        int status = con.getResponseCode();
	        
	        JSONObject response = new JSONObject(ReadResponse(con));
	        JSONArray components = response.getJSONArray("components");
	        List<SonarQubeModel> sonarQubeProjects = new LinkedList<>();  
	        
	        for (Object component: components) {
	            if (component instanceof JSONObject) {
	                JSONObject componentJSON = (JSONObject) component;
	                
	                sonarQubeProjects.add(new SonarQubeModel(
	                		componentJSON.getString("key"), 
	                		componentJSON.getString("name"), 
	                		componentJSON.getString("qualifier"),
	                		"none"
	                		// componentJSON.getString("project")
	                		)); 
	            }
	        } 
	                
	        return sonarQubeProjects;
	    }

	    public static List<String> GetSonarQubeProjectKeys(int amount) throws IOException{
	        List<SonarQubeModel> sonarQubeProjects = GetSonarQubeProjects(amount);
	        List<String> sonarQubeProjectKeys = new LinkedList<>();
	        sonarQubeProjects.forEach(sonarQubeProject -> sonarQubeProjectKeys.add(sonarQubeProject.getKey()));
	        return sonarQubeProjectKeys;
	    }
	    
	   
	    public static List<SonarQubeModel> GetSonarQubeProjectsBvID(int amount) throws IOException {
	        String urlString = SONARQUBE_URL+"/api/projects/search?qualifiers=TRK&ps=" + amount;
	        URL url = new URL(urlString);
	        
	        HttpURLConnection con = (HttpURLConnection) url.openConnection();
	        con.setRequestMethod("GET");
	        con.setRequestProperty("accept", "application/vnd.github.v3+json");
	        con.setRequestProperty("Authorization", OauthHelper.GetSonarAuthentication());
	        
	        int status = con.getResponseCode();
	        
	        JSONObject response = new JSONObject(ReadResponse(con));
	        JSONArray components = response.getJSONArray("components");
	        List<SonarQubeModel> sonarQubeProjects = new LinkedList<>();  
	        
	        for (Object component: components) {
	            if (component instanceof JSONObject) {
	                JSONObject componentJSON = (JSONObject) component;
	                
	                sonarQubeProjects.add(new SonarQubeModel(
	                		componentJSON.getString("key"), 
	                		componentJSON.getString("name"), 
	                		componentJSON.getString("qualifier"),
	                		"none"
	                		// componentJSON.getString("project")
	                		)); 
	            }
	        } 
	                
	        return sonarQubeProjects;
	    }
	    
	 // Delete Project from Sonar through Key
	    public static int DeleteProjectByKey(String key) throws IOException {
	        String urlString = SONARQUBE_URL+"/api/projects/delete?project=" + key;
	        URL url = new URL(urlString);
	        HttpURLConnection con = (HttpURLConnection) url.openConnection();
	        con.setRequestMethod("POST");
	        con.setRequestProperty("accept", "application/vnd.github.v3+json");
	        con.setRequestProperty("Authorization", OauthHelper.GetSonarAuthentication());

	        int status = con.getResponseCode();
	        System.out.println(status);
	        
	        return status;
	    }
}
