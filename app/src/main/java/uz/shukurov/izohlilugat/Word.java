package uz.shukurov.izohlilugat;


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
