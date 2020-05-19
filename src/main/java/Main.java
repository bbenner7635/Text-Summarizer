import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

/** A text summarizer that uses sentence scoring.
 *  @author Brendan Benner
 */
public class Main {

    /** Usage: java src.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND> ....
     *  Writes to unique output file for provided input file. */
    public static void main(String... args) {
        if (args.length == 0) {
            System.out.println("Please provide an input text file.");
            return;
        } else if (args.length == 1) {
            System.out.println("Please provide your desired length of summary" +
                    " as a percentage between 0 and 100 inclusive.");
            return;
        } else if (args.length > 2) {
            System.out.println("Too many arguments passed in.");
            return;
        } else {
            score(args[0]);
            output(Integer.parseInt(args[1]));
        }
    }

    /** Scores the sentences in a text FILENAME by word frequency, sentence
     * position value, similarity with the title, sentence length, and
     * sentence reduction. */
    private static void score(String fileName) {
        // Read in file
        File f;
        if (fileName.indexOf(".txt") == -1) {
            fileName += ".txt";
        }
        f = new File(new File("../../../"), fileName);
        if (!f.exists()) {
            System.out.println("File " + fileName + " does not exist.");
            return;
        }
        title = fileName.substring(0, fileName.length() - 4);
        text = Utils.readContentsAsString(f).replaceAll("\\R+", " ");
        // Initialize words
        words = new HashMap<String, Integer>();
        String noPunctuation = text.replaceAll("[.!?\\-]", "");
        for (String word : noPunctuation.split(" ")) {
            word = word.toLowerCase();
            if (words.containsKey(word)) {
                words.put(word, words.get(word) + 1);
            } else {
                words.put(word, 1);
            }
        }
        // Assign scores
        sentences = new HashMap<Sentence, Double>();
        String[] sentenceArray = text.split("\\. ");
        sentenceArray[sentenceArray.length - 1] =
                sentenceArray[sentenceArray.length - 1].replace(".", "");
        for (int i = 0; i < sentenceArray.length; i++) {
            String s = sentenceArray[i];
            Sentence sentence = new Sentence(s);
            double score = sentence.getScore();
            // Position Value
            if (i == 0 || i == sentenceArray.length - 1) {
                score *= 2;
            } else if (i == 1) {
                score *= 1.5;
            }
            sentences.put(sentence, score);
        }
    }

    /** Writes the top PERCENTAGE % of the document's sentences ranked by
     * score in order of appearance in the input file. */
    private static void output(int percentage) {
        try {
            int numSentences = sentences.size() * percentage / 100;
            // Add the sentences to the list in order of decreasing scores.
            List<Map.Entry<Sentence, Double>> list =
                    new LinkedList<Map.Entry<Sentence, Double>>(sentences.entrySet());
            Collections.sort(list, new Comparator<Map.Entry<Sentence, Double>>() {
                public int compare(Map.Entry<Sentence, Double> o1,
                                   Map.Entry<Sentence, Double> o2) {
                    return (o2.getValue()).compareTo(o1.getValue());
                }
            });
            // Create output file
            File output = new File(new File("../../../output"), title + " " +
                    "Summary" + ".txt");
            String outText = "";
            if (!output.exists()) {
                output.createNewFile();
            }
            // Sublist of first numSentences entries in list.
            list = list.subList(0, numSentences);
            // Sort list by order of appearance in text.
            Collections.sort(list, new Comparator<Map.Entry<Sentence, Double>>() {
                public int compare(Map.Entry<Sentence, Double> o1,
                                   Map.Entry<Sentence, Double> o2) {
                    return text.indexOf(o1.getKey().getSentence())
                            - text.indexOf(o2.getKey().getSentence());
                }
            });
            ListIterator<Map.Entry<Sentence, Double>> iter = list.listIterator();
            // Write to output file.
            while (numSentences > 0 && iter.hasNext()) {
                outText += iter.next().getKey().getSentence() + ". ";
                numSentences -= 1;
            }
            Utils.writeContents(output, outText);
        } catch (IOException ex) {
            System.out.println("IOException");
        }
    }

    /** Returns words. */
    public static HashMap<String, Integer> getWords() {
        return words;
    }

    /** Returns title. */
    public static String getTitle() {
        return title;
    }

    /** A HashMap of all sentences in a document along with their scores. */
    private static HashMap<Sentence, Double> sentences;

    /** A HashMap of all words in a document along with their frequencies. */
    private static HashMap<String, Integer> words;

    /** The document title. */
    private static String title;

    /** A string containing the document's text. */
    private static String text;
}
