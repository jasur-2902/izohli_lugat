package uz.shukurov.izohlilugat;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Jasur Shukurov
 */
public class Dictionary{

    private static Dictionary instance = null;
    private ArrayList<Word> dictionaryDB;

    private Dictionary()
    {
        dictionaryDB = DictionaryActivity.passWord();
    }

    public static Dictionary getInstance()
    {
        if(instance==null)
        {
            instance = new Dictionary();
        }
        return instance;
    }

    public ArrayList<String> findFromDatabase(String typedWord)
    {
        ArrayList<String> matchingWords = new ArrayList<>();

        for(Word element : dictionaryDB)
        {
            if(element.getWord().contains(typedWord))
                matchingWords.add(element.getWord());
        }

        return matchingWords;
    }

    public String getDefinitionFromDatabase(String word)
    {
        String defi = "";
        for(Word element : dictionaryDB)
        {
            if(element.getWord().equals(word))
            {
                defi = element.getDefinition();
            }
        }
        return defi;
    }

    public boolean ifWordExist(String word)
    {
        for(Word element : dictionaryDB)
        {
            if(element.getWord().equals(word))
            {
                return true;
            }
        }
        return false;
    }

    public Word getWordFromDatabase()
    {
        Random rand = new Random();

        return dictionaryDB.get(rand.nextInt(dictionaryDB.size() - 1));
    }

    public boolean addNewWord(Word word)
    {
        boolean flag = ifWordExist(word.getWord());
        if(!flag)
        {
            dictionaryDB.add(word);
            DictionaryActivity.Addword(word);
            return true;
        }
        return false;
    }

    public void Delete(String word)
    {
        for(Word element : dictionaryDB)
        {
            if(element.getWord().equals(word))
            {
                dictionaryDB.remove(element);
                break;
            }
        }
        DictionaryActivity.deleteWord(word);
    }

    public void Update(Word word)
    {
        for(Word element : dictionaryDB)
        {
            if(element.getWord().equals(word.getWord()))
            {
                dictionaryDB.get(dictionaryDB.indexOf(element)).setDefinition(word.getDefinition());
                break;
            }
        }
        DictionaryActivity.updateWord(word);
    }
}