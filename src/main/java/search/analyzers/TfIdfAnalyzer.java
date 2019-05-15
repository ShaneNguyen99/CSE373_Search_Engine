package search.analyzers;

import datastructures.concrete.ChainedHashSet;
import datastructures.concrete.KVPair;
import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IList;
import datastructures.interfaces.ISet;
import search.models.Webpage;

import java.net.URI;

/**
 * This class is responsible for computing how "relevant" any given document is
 * to a given search query.
 *
 * See the spec for more details.
 */
public class TfIdfAnalyzer {
    // This field must contain the IDF score for every single word in all
    // the documents.
    private IDictionary<String, Double> idfScores;

    // This field must contain the TF-IDF vector for each webpage you were given
    // in the constructor.
    //
    // We will use each webpage's page URI as a unique key.
    private IDictionary<URI, IDictionary<String, Double>> documentTfIdfVectors;

    // Feel free to add extra fields and helper methods.

    public TfIdfAnalyzer(ISet<Webpage> webpages) {
        // Implementation note: We have commented these method calls out so your
        // search engine doesn't immediately crash when you try running it for the
        // first time.
        //
        // You should uncomment these lines when you're ready to begin working
        // on this class.

        this.idfScores = this.computeIdfScores(webpages);
        this.documentTfIdfVectors = this.computeAllDocumentTfIdfVectors(webpages);
    }

    // Note: this method, strictly speaking, doesn't need to exist. However,
    // we've included it so we can add some unit tests to help verify that your
    // constructor correctly initializes your fields.
    public IDictionary<URI, IDictionary<String, Double>> getDocumentTfIdfVectors() {
        return this.documentTfIdfVectors;
    }

    // Note: these private methods are suggestions or hints on how to structure your
    // code. However, since they're private, you're not obligated to implement exactly
    // these methods: feel free to change or modify these methods however you want. The
    // important thing is that your 'computeRelevance' method ultimately returns the
    // correct answer in an efficient manner.

    /**
     * Return a dictionary mapping every single unique word found
     * in every single document to their IDF score.
     */
    private IDictionary<String, Double> computeIdfScores(ISet<Webpage> pages) {
        IDictionary<String, Double> result = new ChainedHashDictionary<>();
        for(Webpage page : pages) {
            IList<String> list = page.getWords();
            ISet<String> uniqueWords = new ChainedHashSet<>();
            for(String val : list)
                uniqueWords.add(val);
            for(String val : uniqueWords) {
                if(!result.containsKey(val))
                    result.put(val, 0.0);
                result.put(val, result.get(val)+1);
            }
        }
        IDictionary<String, Double> temp = new ChainedHashDictionary<>();
        for(KVPair<String, Double> pair : result) {
            temp.put(pair.getKey(), Math.log(pages.size()/pair.getValue()));
        }
        result = temp;
        return result;
    }

    /**
     * Returns a dictionary mapping every unique word found in the given list
     * to their term frequency (TF) score.
     *
     * The input list represents the words contained within a single document.
     */
    private IDictionary<String, Double> computeTfScores(IList<String> words) {
        IDictionary<String, Double> result = new ChainedHashDictionary<>();
        for(String val : words) {
            if(!result.containsKey(val))
                result.put(val, 0.0);
            result.put(val, result.get(val)+1);
        }
        IDictionary<String, Double> temp = new ChainedHashDictionary<>();
        for(KVPair<String, Double> pair : result) {
            temp.put(pair.getKey(), pair.getValue()/words.size());
        }
        result = temp;
        return result;
    }

    /**
     * See spec for more details on what this method should do.
     */
    private IDictionary<URI, IDictionary<String, Double>> computeAllDocumentTfIdfVectors(ISet<Webpage> pages) {
        // Hint: this method should use the idfScores field and
        // call the computeTfScores(...) method.
        IDictionary<URI, IDictionary<String, Double>> result = new ChainedHashDictionary<>();
        for(Webpage page : pages) {
            IList<String> list = page.getWords();
            IDictionary<String, Double> scores = this.computeTfScores(list);
            IDictionary<String, Double> temp = this.computeTfScores(list);
            for(KVPair<String, Double> pair : scores) {
                String word = pair.getKey();
                Double tfScore = pair.getValue();
                tfScore *= this.idfScores.get(word);
                temp.put(word, tfScore);
            }
            result.put(page.getUri(), temp);
        }
        return result;
    }

    /**
     * Returns the cosine similarity between the TF-IDF vector for the given query and the
     * URI's document.
     *
     * Precondition: the given uri must have been one of the uris within the list of
     *               webpages given to the constructor.
     */
    public Double computeRelevance(IList<String> query, URI pageUri) {
        // Note: The pseudocode we gave you is not very efficient. When implementing,
        // this method, you should:
        //
        // 1. Figure out what information can be precomputed in your constructor.
        //    Add a third field containing that information.
        //
        // 2. See if you can combine or merge one or more loops.
        IDictionary<String, Double> docVector = this.documentTfIdfVectors.get(pageUri);

        IDictionary<String, Double> queryVector = this.computeTfScores(query);
        IDictionary<String, Double> temp = new ChainedHashDictionary<>();
        double numerator = 0.0;
        for(KVPair<String, Double> pair : queryVector) {
            String word = pair.getKey();
            Double tfScore = pair.getValue();
            if(this.idfScores.containsKey(word))
                tfScore *= this.idfScores.get(word);
            else
                tfScore = 0.0;
            temp.put(word, tfScore);
            double docWordScore = ((docVector.containsKey(word)) ? docVector.get(word) : 0.0);
            double queryWordScore = tfScore;
            numerator += docWordScore * queryWordScore;
        }

        double denominator = this.norm(docVector) * this.norm(temp);
        if(denominator != 0.0)
            return numerator/denominator;
        else
            return 0.0;
    }

    private double norm(IDictionary<String, Double> vector) {
        double output = 0.0;
        for(KVPair<String, Double> pair : vector) {
            Double score = pair.getValue();
            output += score * score;
        }
        return Math.sqrt(output);
    }
}
