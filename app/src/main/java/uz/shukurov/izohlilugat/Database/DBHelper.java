package uz.shukurov.izohlilugat.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import uz.shukurov.izohlilugat.R;


public class DBHelper extends SQLiteOpenHelper {
    final String LOG_TAG = "myLogs";
    Context context;

    public DBHelper(Context context) {
        // конструктор суперкласса
        super(context, "myDB", null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(LOG_TAG, "--- onCreate database ---");
        // создаем таблицу с полями
        db.execSQL("create table " + context.getString(R.string.mytable) + " ("
                + "id integer primary key autoincrement,"
                + "latin text,"
                + "saebiz text,"
                + "diacritic text,"
                + "cyrillic text" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
