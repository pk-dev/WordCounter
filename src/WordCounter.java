
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by Priya on 10/2/16.
 * This class implements Callable interface which facilitates threading.
 */
public class WordCounter implements Callable {

    private String textLine;

    public WordCounter(String textLine)
    {
        this.textLine=textLine;

    }

    //the processing logic
    private Map<String,Integer> doWork()
    {
        //split text into words delimited by space
        String[] words = textLine.split(" ");
        Map<String, Integer> wordMap = new HashMap<>();
        //counts frequency of each word
        for (String word : words)
        {
            Integer count = wordMap.get(formatWord(word));
            count = (count == null) ? 1 : count+1;//if the word is already present in wordMap, increment its count.
            wordMap.put(formatWord(word), count);
        }

        return wordMap;

    }


    private String formatWord(String word)
    {
        //format each word to avoid punctuations.
        //only letters and numbers are counted as words.
        //all words are converted to lower case to
        //avoid considering same words with capitalization as different.

        String newWord=word.replaceAll("[^a-zA-Z0-9]+", "");
        return newWord.toLowerCase();
    }

    @Override
    public Map<String,Integer> call() throws Exception
    {
        //Calls the processing logic and return the results
        System.out.println("Thread id " + Thread.currentThread().getId()+" evaluates: "+textLine);
        return doWork();

    }
}
