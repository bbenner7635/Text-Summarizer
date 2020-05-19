import java.util.HashSet;
import java.util.Set;

/** A class representing a sentence.
 *  @author Brendan Benner
 */
public class Sentence {

    /** Constructor for Sentence class. Takes s as its text. */
    public Sentence(String s) {
        sentence = s;
        s = s.replaceAll("[.!?\\-]", "");
        wordArray = s.split(" ");
        wordSet = new HashSet<String>();
        for (String word : wordArray) {
            wordSet.add(word.toLowerCase());
        }
        // Remove common English words.
        for (String remove : COMMONWORDS) {
            wordSet.remove(remove);
        }
        // Calculate scores by adding normalized sub-scores.
        int frequencyScore = frequencyScore();
        int titleScore = titleScore();
        int properScore = properScore();
        min = Math.min(Math.min(frequencyScore, titleScore), properScore);
        max = Math.max(Math.max(frequencyScore, titleScore), properScore);
        score = norm(frequencyScore) + norm(titleScore) + norm(properScore);
    }

    /** Returns a score based on word frequency of words in the sentence. */
    private int frequencyScore() {
        int score = 0;
        for (String word : wordSet) {
            score += Main.getWords().get(word);
        }
        return score;
    }

    /** Returns a score based on the sentence's similarity with the document
     * title. */
    private int titleScore() {
        for (String word : wordSet) {
            if (word.equals(Main.getTitle().toLowerCase())) {
                return 1;
            }
        }
        return 0;
    }

    /** Returns a score based on the sentence's number of proper nouns. */
    private int properScore() {
        int score = 0;
        for (int i = 1; i < wordArray.length; i++) {
            String word = wordArray[i];
            if (word.length() > 0 && Character.isUpperCase(word.charAt(0))) {
                score++;
            }
        }
        return score;
    }

    /** Returns a normalized SCORE. */
    private double norm(int score) {
        return (score - min) / (max - min);
    }

    /** Returns the score of this sentence. */
    public double getScore() {
        return score;
    }

    /** Returns the text of this sentence. */
    public String getSentence() {
        return sentence;
    }

    /** A String containing the text of this sentence. */
    private String sentence;

    /** An array of the words in this sentence, including capital letters. */
    private String[] wordArray;

    /** A set of the words in this sentence, excluding capital letters. */
    private Set<String> wordSet;

    private final String[] COMMONWORDS = {"the","of","and","a","to","in","is","you","that","it","he","was","for","on","are","as","with","his","they","I","at","be","this","have","from","or","one","had","by","word","but","not","what","all","were","we","when","your","can","said","there","use","an","each","which","she","do","how","their","if","will","up","other","about","out","many","then","them","these","so","some","her","would","make","like","him","into","time","has","look","two","more","write","go","see","number","no","way","could","people","my","than","first","water","been","call","who","oil","its","now","find","long","down","day","did","get","come","made","may","part"};

    /** The minimum sub-scores of this sentence. */
    private int min;

    /** The maximum sub-scores of this sentence. */
    private int max;

    /** The score of this sentence. */
    private double score;
}
