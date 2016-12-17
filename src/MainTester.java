
import java.io.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by Priya on 10/2/16.
 */
public class MainTester {

    public static void main(String[] args) throws InterruptedException, ExecutionException{

        BufferedReader textData = null;


        try {

            //Read the text file
            //testfile.txt must be located in the folder TestFileLocation
            //the folder TestFileLocation must be present in the root directory.
            textData = new BufferedReader(new FileReader("./TestFileLocation/testfile.txt"));

            //Split the data into segments
            //Segments are separated by newline
            //So all sentences until an enter key is encountered is considered as a segment.
            String currentLine;
            List<String> linesList = new ArrayList<String>();

            while ((currentLine = textData.readLine()) != null)
            {
                if(currentLine.length()!=0)
                    linesList.add(currentLine);
            }
            if(linesList.size()<2)
                System.out.println("Not enough data for multiple threads. Please input a text with sentences separated by newline to see multiple threads in action.");

            if(linesList.size()>0) {

                //using Callable interface to acheive threading and obtain results back from threads
                ExecutorService executorService = Executors.newFixedThreadPool(linesList.size());

                List<Callable<Map<String, Integer>>> allCounters = new ArrayList<Callable<Map<String, Integer>>>();
                //Assign each segment as tasks to threads
                for (String s : linesList)
                    allCounters.add(new WordCounter(s));

                //Invoke threads and waits till all threads complete execution
                List<Future<Map<String, Integer>>> allMaps = executorService.invokeAll(allCounters);//Program execution resumes beyond this point only when all threads are finished.

                executorService.shutdown();//shut down the threadpool

                //Calculate total frequency of each word by combining the word frequencies from different thread outputs
                Map<String, Integer> combinedWordMap = new HashMap<String, Integer>();
                for (Future<Map<String, Integer>> eachMap : allMaps) {
                    //merge results from different threads to combinedWordMap
                    eachMap.get().forEach((key, value) -> combinedWordMap.merge(key, value, Integer::sum));
                }

                //Print the combined word frequency count
                System.out.println("Word frequency count:");
                for (Map.Entry<String, Integer> entry : combinedWordMap.entrySet())
                    System.out.println(entry.getKey() + " = " + entry.getValue());

            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (textData != null)
                    textData.close();
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }


}
