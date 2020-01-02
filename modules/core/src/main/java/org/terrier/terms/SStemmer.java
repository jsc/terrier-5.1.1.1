package org.terrier.terms;

/**
 * Stemmer, implementing the "S" Stemming Algorithm. 
 * 
 *
 * @author <a href="mailto:shane.culpepper@gmail.com">J. Shane Culpepper</a>
 * @version 0.1
 * @since 0.1
 * 
 */

public class SStemmer extends StemmerTermPipeline {
    /**
     * constructor
     */
    public SStemmer() {
        super();
    }

    /**
     * constructor
     * 
     * @param next
     */
    public SStemmer(TermPipeline next) {
        super(next);
    }

		private String recodeEnding(String s) {
    	if (s.endsWith("ies")) {
        return s.substring(0, s.length() - 3);
			} else if (s.endsWith("y")) {
        return s.substring(0, s.length()- 1);
			} else if (s.endsWith("es")) {
        return s.substring(0, s.length() - 2);
			} else if (s.endsWith("s")) {
        return s.substring(0, s.length() - 1);
    	} else {
        return s;
    	}
		}

    @Override
    public String stem(String word) {
        if (word.length() > 3) {
          return recodeEnding(word.toLowerCase());
        } 
				else {
          return word.toLowerCase();
        }
    }
}



