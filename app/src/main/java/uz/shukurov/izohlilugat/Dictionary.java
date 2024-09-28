package uz.shukurov.izohlilugat;

import java.util.ArrayList;

/**
 * Created by Jasur Shukurov
 */
public class Dictionary {
    private static Dictionary instance = null;
    private ArrayList<Word> dictionaryDB;

    private Dictionary() {
        dictionaryDB = DictionaryActivity.passWord();
    }

    public static Dictionary getInstance() {
        if (instance == null) {
            instance = new Dictionary();
        }
        return instance;
    }

    public ArrayList<String> findFromDatabase(final String typedWord) {
        final ArrayList<String> matchingWords = new ArrayList<>();

        for (final Word element : dictionaryDB) {
            if (element.getWord().contains(typedWord))
                matchingWords.add(element.getWord());
        }

        return matchingWords;
    }

    public String getDefinitionFromDatabase(final String word) {
        String defi = "";
        for (final Word element : dictionaryDB) {
            if (element.getWord().equals(word)) {
                defi = element.getDefinition();
            }
        }
        return defi;
    }
}