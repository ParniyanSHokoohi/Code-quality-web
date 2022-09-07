package IO;

import com.opencsv.CSVReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;


public abstract class IOHelper {

	
	private static String readFile(String path, Charset encoding) throws IOException
    {
        List<String> lines = Files.readAllLines(Paths.get(path), encoding);
        return String.join(System.lineSeparator(), lines);
    }
	
	 
    public static String GetFileContent(String filePath)
    {       
        String content= null;
        
        try {
            content = readFile(filePath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
 
        return content;
    }
    
    private List<String[]> readAll(Reader reader, boolean clearFirstRow) throws Exception {
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
