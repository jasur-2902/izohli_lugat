package uz.shukurov.izohlilugat;

/**
 * Created by Foooooo on 10/06/16.
 */
public class Word {
    DictionaryActivity dictionary;


    private String word;
    private String definition;

    public Word(String word, String definition)
    {
        this.word = word;
        this.definition = definition;
    }

    private void setWord(String newWord)
    {
    }

    public void setDefinition(String newDefinition)
    {
        definition = newDefinition;
    }

    public String getWord()
    {
        return word;
    }

    public String getDefinition()
    {
        return definition;
    }
}
