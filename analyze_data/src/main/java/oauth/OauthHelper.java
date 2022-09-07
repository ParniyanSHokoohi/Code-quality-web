package oauth;

import java.nio.charset.StandardCharsets;
import java.util.Base64;


public abstract class OauthHelper{
	
	
    /**
     * Sonar Authentication for the login in sonarqube
	 */
	public static  String GetSonarAuthentication(){
        String auth = "admin" + ":" + "SonarQube120#";
        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8));
        return "Basic " + new String(encodedAuth);
    }
	
	/**
	 * GitHub Authorization with owner and Token
	 */
	public static String GetGithubAuthorization(){
        String auth = "raoulnzefa" + ":" + "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";
        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8));
        return "Basic " + new String(encodedAuth);
    } 
}
