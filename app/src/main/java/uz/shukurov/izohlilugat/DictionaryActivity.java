package uz.shukurov.izohlilugat;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by Jasur SHukurov
 */
public class DictionaryActivity extends SQLiteOpenHelper {

    static SQLiteDatabase sqlite;
    static Context context;

    public DictionaryActivity(Context context)
    {
        super(context,"database",null, 1);
        this.context = context;
    }

    public void createDatabase()
    {
        boolean dbexist = checkDatabase();
        if(dbexist){}
        else
        {
            this.getReadableDatabase();
            try
            {
                copyDatabase();
            }
            catch(IOException e)
            {
            }
        }
    }

    public boolean checkDatabase()
    {
        SQLiteDatabase checkdb = null;

        try{
            String mypath = "/data/data/uz.shukurov.izohlilugat/databases/A.sqlite";
            checkdb = SQLiteDatabase.openDatabase(mypath,null, SQLiteDatabase.OPEN_READONLY);
        }
        catch(SQLiteException e)
        {

        }

        if(checkdb != null)
        {
            checkdb.close();
        }

        return checkdb != null? true : false;
    }

    public void copyDatabase() throws IOException
    {
        InputStream myInput = context.getAssets().open("A.sqlite");

        String outfilename = "/data/data/uz.shukurov.izohlilugat/databases/A.sqlite";

        OutputStream myOutput = new FileOutputStream(outfilename);

        byte[] buffer = new byte[1024];

        int length;

        while((length = myInput.read(buffer)) > 0)
        {
            myOutput.write(buffer, 0, length);
        }

        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public void openDatabase() throws SQLiteException
    {
        String mypath = "/data/data/uz.shukurov.izohlilugat/databases/A.sqlite";
        sqlite = SQLiteDatabase.openDatabase(mypath,null, SQLiteDatabase.OPEN_READWRITE);
    }

    @Override
    public synchronized void close() {
        if(sqlite != null)
            sqlite.close();

        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase da)
    {

    }

    @Override
    public void onUpgrade(SQLiteDatabase da, int OldVersion, int NewVersion)
    {

    }

    public static int countHistory (){
        int meh = 0;
        String query = "SELECT max(id) from history;";
        try {
            Cursor c = sqlite.rawQuery(query,null);
            c.moveToFirst();
            meh = Integer.valueOf(c.getString(c.getColumnIndex("max(id)")));
        }
        catch(Exception e) {
            return 0;
        }
        return meh;
    }

    //Favourite
    public static int countFavourite (){
        int meh = 0;
        String query = "SELECT max(id) from favourite;";
        try {
            Cursor c = sqlite.rawQuery(query,null);
            c.moveToFirst();
            meh = Integer.valueOf(c.getString(c.getColumnIndex("max(id)")));
        }
        catch(Exception e) {
            return 0;
        }
        return meh;
    }

    public static boolean repetitionHistory(String word){
        String query = "SELECT word FROM history WHERE word = '" + word + "'";
        Cursor c = sqlite.rawQuery(query,null);
        c.moveToFirst();
        try
        {
            c.getString(c.getColumnIndex("word"));
        }
        catch(Exception e)
        {
            return false;
        }
        return true;
    }


    //Favourite
    public static boolean repetitionFavourite(String word){
        String query = "SELECT word FROM favourite WHERE word = '" + word + "'";
        Cursor c = sqlite.rawQuery(query,null);
        c.moveToFirst();
        try
        {
            c.getString(c.getColumnIndex("word"));
        }
        catch(Exception e)
        {
            return false;
        }
        return true;
    }

    public static int countMax()
    {
        String query = "SELECT max(_id) FROM dictionary";
        Cursor c = sqlite.rawQuery(query,null);
        c.moveToFirst();

        return Integer.valueOf(c.getString(c.getColumnIndex("max(_id)")));
    }

    public static void Addword(Word word)
    {
        String query = "INSERT INTO dictionary VALUES (" + (countMax() + 1) + ",\"" + word.getWord() + "\", \"" + word.getDefinition() + "\")";
        sqlite.execSQL(query);
    }


    public static void deleteWord(String word)
    {
        String query = "DELETE FROM dictionary WHERE word = '" + word + "'";
        sqlite.execSQL(query);
    }

    public static void updateWord(Word word)
    {
        String query = "UPDATE dictionary SET meaning = '" + word.getDefinition() + "' WHERE word = '" + word.getWord() + "'";
        sqlite.execSQL(query);
    }


    public static void addtoHistory(String words)
    {
        if(countHistory() >= 11)
        {
            String query = "DELETE FROM history WHERE id = (SELECT min(id) FROM history)";
            sqlite.execSQL(query);
        }
        String query = "INSERT INTO history VALUES (" + (countHistory() + 1) + ",\"" + words + "\")";
        sqlite.execSQL(query);
    }


//Favourite


    public static void deleteFromFavourite(String words)
    {
        String query = "DELETE FROM favourite WHERE word = '" + words + "'";
        sqlite.execSQL(query);
    }



    public static void addtoFavourite(String words)
    {
        if(countFavourite() >= 11)
        {
            String query = "DELETE FROM favourite WHERE id = (SELECT min(id) FROM favourite)";
            sqlite.execSQL(query);
        }
        String query = "INSERT INTO favourite VALUES (" + (countFavourite() + 1) + ",\"" + words + "\")";
        sqlite.execSQL(query);
    }



    public static void clearHistory()
    {
        String query = "DELETE FROM history";
        sqlite.execSQL(query);
    }

//Favourite
    public static void clearFavourite()
    {
        String query = "DELETE FROM favourite";
        sqlite.execSQL(query);
    }


    public static ArrayList<String> getHistory()
    {
        String query = "SELECT * FROM history";
        Cursor c = sqlite.rawQuery(query,null);
        ArrayList<String> arrlist = new ArrayList<>();

        c.moveToFirst();
        while(!c.isAfterLast())
        {
            if(c.getString(c.getColumnIndex("word")) != null)
                arrlist.add(c.getString(c.getColumnIndex("word")));
            c.moveToNext();
        }

        return arrlist;
    }

    public static ArrayList<String> getDictionary()
    {
        String query = "SELECT * FROM dictionary";
        Cursor c = sqlite.rawQuery(query,null);
        ArrayList<String> arrlist = new ArrayList<>();

        c.moveToFirst();
        while(!c.isAfterLast())
        {
            if(c.getString(c.getColumnIndex("word")) != null)
                arrlist.add(c.getString(c.getColumnIndex("word")));
            c.moveToNext();
        }

        return arrlist;
    }

    // FAVOURITE
    public static ArrayList<String> getFavourite()
    {
        String query = "SELECT * FROM favourite";
        Cursor c = sqlite.rawQuery(query,null);
        ArrayList<String> arrlist = new ArrayList<>();

        c.moveToFirst();
        while(!c.isAfterLast())
        {
            if(c.getString(c.getColumnIndex("word")) != null)
                arrlist.add(c.getString(c.getColumnIndex("word")));
            c.moveToNext();
        }

        return arrlist;
    }



    public static ArrayList<Word> passWord()
    {
        ArrayList<Word> database =  new ArrayList<>();
        String word = "";
        String definition = "";
        String query = "SELECT * FROM dictionary";
        Cursor c = sqlite.rawQuery(query,null);
        c.moveToFirst();
        while (!c.isAfterLast())
        {
            if(c.getString(c.getColumnIndex("word")) != null)
            {
                word = (c.getString(c.getColumnIndex("word")));
            }
            if(c.getString(c.getColumnIndex("meaning")) != null)
            {
                definition = (c.getString(c.getColumnIndex("meaning")));
            }
            database.add(new Word(word,definition));
            c.moveToNext();
        }

        return database;
    }
}