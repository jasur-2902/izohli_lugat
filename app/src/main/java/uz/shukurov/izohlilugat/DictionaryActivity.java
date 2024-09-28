package uz.shukurov.izohlilugat;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by Jasur SHukurov
 */
public class DictionaryActivity extends SQLiteOpenHelper {

    private static final String TAG = DictionaryActivity.class.getName();
    static SQLiteDatabase sqlite;
    static Context context;

    public DictionaryActivity(Context context) {
        super(context, "dictionary", null, 1);
        this.context = context;
    }

    public void createDatabase() {

        final boolean dbexist = checkDatabase();
        if (dbexist) {
            Log.d(TAG, "database exists, do nothing!");
        } else {
            Log.d(TAG, "database doesn't exist, copying!");
            this.getReadableDatabase();
            try {
                copyDatabase();
            } catch (IOException e) {
                Log.e(TAG, "Exception while copying database.", e);
            }
        }
    }

//    public boolean checkDatabase() {
//        SQLiteDatabase checkdb = null;
//
//        try {
//            checkdb = SQLiteDatabase.openDatabase("/data/data/uz.shukurov.izohlilugat/databases/dictionary.sqlite", null, SQLiteDatabase.OPEN_READONLY);
//        } catch (final Exception e) {
//            Log.w(TAG, "DB doesn't exist", e);
//            return false;
//        }
//
//        if (checkdb != null) {
//            checkdb.close();
//        }
//
//        return checkdb != null;
//    }

    public boolean checkDatabase() {
        File dbFile = context.getDatabasePath("dictionary.sqlite");
        return dbFile.exists();
    }

    public void copyDatabase() throws IOException {
        final InputStream myInput = context.getAssets().open("dictionary.sqlite");

        String outfilename = "/data/data/uz.shukurov.izohlilugat/databases/dictionary.sqlite";

        OutputStream myOutput = new FileOutputStream(outfilename);

        byte[] buffer = new byte[1024];

        int length;

        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public void openDatabase() throws SQLiteException {
        String mypath = "/data/data/uz.shukurov.izohlilugat/databases/dictionary.sqlite";
        sqlite = SQLiteDatabase.openDatabase(mypath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    @Override
    public synchronized void close() {
        if (sqlite != null)
            sqlite.close();

        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase da) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase da, int OldVersion, int NewVersion) {

    }

    public static int countHistory() {
        int meh = 0;
        String query = "SELECT max(id) from history;";
        try {
            Cursor c = sqlite.rawQuery(query, null);
            c.moveToFirst();
            meh = Integer.valueOf(c.getString(c.getColumnIndex("max(id)")));
        } catch (Exception e) {
            return 0;
        }
        return meh;
    }

    //Favourite
    public static int countFavourite() {
        int meh = 0;
        String query = "SELECT max(id) from favourite;";
        try {
            Cursor c = sqlite.rawQuery(query, null);
            c.moveToFirst();
            meh = Integer.valueOf(c.getString(c.getColumnIndex("max(id)")));
        } catch (Exception e) {
            return 0;
        }
        return meh;
    }

    public static boolean repetitionHistory(String word) {
        String escapedWord = word.replace("'", "''");
        String query = "SELECT word FROM history WHERE word = '" + escapedWord + "'";
        Cursor c = sqlite.rawQuery(query, null);
        c.moveToFirst();
        try {
            c.getString(c.getColumnIndex("word"));
        } catch (Exception e) {
            return false;
        }
        return true;
    }


    //Favourite
    public static boolean repetitionFavourite(String word) {
        String escapedWord = word.replace("'", "''");
        String query = "SELECT word FROM favourite WHERE word = '" + escapedWord + "'";
        Cursor c = sqlite.rawQuery(query, null);
        c.moveToFirst();
        try {
            c.getString(c.getColumnIndex("word"));
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static int countMax() {
        String query = "SELECT max(_id) FROM izohli";
        Cursor c = sqlite.rawQuery(query, null);
        c.moveToFirst();

        return Integer.valueOf(c.getString(c.getColumnIndex("max(_id)")));
    }

    public static void addtoHistory(String words) {
        if (countHistory() >= 11) {
            String query = "DELETE FROM history WHERE id = (SELECT min(id) FROM history)";
            sqlite.execSQL(query);
        }
        String query = "INSERT INTO history VALUES (" + (countHistory() + 1) + ",\"" + words + "\")";
        sqlite.execSQL(query);
    }


    //Favourite
    public static void deleteFromFavourite(String words) {
        String query = "DELETE FROM favourite WHERE word = '" + words + "'";
        sqlite.execSQL(query);
    }

    public static void addtoFavourite(String words) {
        if (countFavourite() >= 11) {
            String query = "DELETE FROM favourite WHERE id = (SELECT min(id) FROM favourite)";
            sqlite.execSQL(query);
        }
        String query = "INSERT INTO favourite VALUES (" + (countFavourite() + 1) + ",\"" + words + "\")";
        sqlite.execSQL(query);
    }

    public static void clearFavourite() {
        String query = "DELETE FROM favourite";
        sqlite.execSQL(query);
    }

    // History
    public static void clearHistory() {
        String query = "DELETE FROM history";
        sqlite.execSQL(query);
    }

    public static ArrayList<String> getHistory() {
        String query = "SELECT * FROM history";
        Cursor c = sqlite.rawQuery(query, null);
        ArrayList<String> arrlist = new ArrayList<>();

        c.moveToFirst();
        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("word")) != null)
                arrlist.add(c.getString(c.getColumnIndex("word")));
            c.moveToNext();
        }

        return arrlist;
    }

    public static ArrayList<String> getDictionary() {
        String query = "SELECT * FROM izohli";
        Cursor c = sqlite.rawQuery(query, null);
        ArrayList<String> arrlist = new ArrayList<>();

        c.moveToFirst();
        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("latin")) != null)
                arrlist.add(c.getString(c.getColumnIndex("latin")));
            c.moveToNext();
        }

        return arrlist;
    }

    public static Cursor getDictionaryCursor() {
        String query = "SELECT id AS _id, latin, latin_def FROM izohli";
        return sqlite.rawQuery(query, null);
    }

    // FAVOURITE
    public static ArrayList<String> getFavourite() {
        String query = "SELECT * FROM favourite";
        Cursor c = sqlite.rawQuery(query, null);
        ArrayList<String> arrlist = new ArrayList<>();

        c.moveToFirst();
        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("word")) != null)
                arrlist.add(c.getString(c.getColumnIndex("word")));
            c.moveToNext();
        }

        return arrlist;
    }


    public static ArrayList<Word> passWord() {
        ArrayList<Word> database = new ArrayList<>();
        String word = "";
        String query = "SELECT * FROM izohli";

        String definition = "";
        try {
            Cursor c = sqlite.rawQuery(query, null);
            c.moveToFirst();
            while (!c.isAfterLast()) {
                if (c.getString(c.getColumnIndex("latin")) != null) {
                    word = (c.getString(c.getColumnIndex("latin")));
                }
                if (c.getString(c.getColumnIndex("latin_def")) != null) {
                    definition = (c.getString(c.getColumnIndex("latin_def")));
                }
                database.add(new Word(word, definition));
                c.moveToNext();
            }

        } catch (final Exception e) {
            Log.e(TAG, "Something went wrong!", e);
        }
        return database;
    }
}