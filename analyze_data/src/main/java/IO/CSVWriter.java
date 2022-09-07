package IO;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;


public class CSVWriter {
	
	
    /***
     * This method create the metric from the sonarqube-analyzer
     * @param sonarQubeMetrics
     * @param metrics
     * @throws IOException
     */
	public void CreateCSVFile(List<HashMap<String, String>> sonarQubeMetrics, String[] metrics) throws IOException {
			
		String csvNameWithCurrentTime = CSVNameWithCurrentTime();	
        FileWriter out = new FileWriter(CSVNameWithCurrentTime());
       		
        try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader(metrics))) {
            	
        	for(HashMap<String, String> sonarQubeMetric: sonarQubeMetrics) {
        		
                Object[] metricsArray = new Object[metrics.length];
                
                for (int i = 0; i < metrics.length; i++){
                    metricsArray[i] = sonarQubeMetric.get(metrics[i]);
                }
                
                printer.printRecord(metricsArray);
            }
        
        } catch (Exception e){
            e.printStackTrace();
        } 
    }
	
    /***
     * Help method to set csv-datei-name-suffix with the current time.
     * @return
     */
    private String CSVNameWithCurrentTime(){
        StringBuilder sb = new StringBuilder();   
        sb.append("export/sonarqube_metrics_");
        LocalDateTime localDateTime = LocalDateTime.now();      
        String DEFAULT_FILE_PATTERN = "yyyy-MM-dd-HH-mm-ss";
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat(DEFAULT_FILE_PATTERN);
        format.format(date);
        
        // sb.append(localDateTime.toString());
        
        sb.append(format.format(date));

        sb.append(".csv");
        return sb.toString();
    }

}
