package repository;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import com.opencsv.CSVReader;
import model.GithubModel;

public class ReposIterator {
	
	/***
	 * Load all projects from the github.
	 * @param filename
	 * @return
	 * @throws Exception
	 */
	public List<GithubModel> LoadReposGithubList(String filename) throws Exception {
        
		List<GithubModel> csvGithubList = null;
		
		try {
			
			csvGithubList = ReadCsvFile(filename);

		} catch (Exception e1) {
			e1.printStackTrace();
		}
       
        return csvGithubList;
    }
	

    private List<GithubModel> ReadCsvFile(String filename) throws Exception {
        
        Reader reader = new InputStreamReader(new FileInputStream(filename), StandardCharsets.UTF_8);
        List<String[]> githubList = ReadAll(reader, true);
        List<GithubModel> githubListModel = new LinkedList<>();
       
        for(String[] stringArray: githubList){           
        	
        	githubListModel.add(new GithubModel(
       				Integer.parseInt(stringArray[0]), 
       				stringArray[1],
       				stringArray[2],
       				new URL(stringArray[4])
       						)); 
        }
        
        return githubListModel;
	}

	
    private List<String[]> ReadAll(Reader reader, boolean clearFirstRow) throws Exception{
        CSVReader csvReader = new CSVReader(reader);
        List<String[]> list;
        
        list = csvReader.readAll();
       
        reader.close();
        
        csvReader.close();
     
        if(clearFirstRow){
            list.remove(0);
        } 
        
        return list;
    }
}
