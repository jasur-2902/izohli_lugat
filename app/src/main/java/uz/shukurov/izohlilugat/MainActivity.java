package uz.shukurov.izohlilugat;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.GregorianCalendar;

import uz.shukurov.izohlilugat.Database.CreateDatabase;
import uz.shukurov.izohlilugat.Database.DBHelper;


public class MainActivity extends AppCompatActivity {

    Toolbar tb;
    Context context;
    SearchView searchView;
    TextToSpeech convert2speech;
    Dictionary dic;
    TextToSpeech.OnInitListener ttsinit;
    boolean startFlag = true;
    TextView titleTextView,text;
    private static final String FONT_SIZE_KEY = "fontsize";
    private static final String TEXT_VALUE_KEY = "textvalue";
    private SeekBar seekBar;
    private SharedPreferences prefs,mSharedPrefs;
    private String prefName = "MyPref";
    String clicked;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    replaceFirst();
                    return true;
                case R.id.navigation_dashboard:
                    replaceSecond();
                    return true;
                case R.id.navigation_notifications:
                    replaceThird();
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tb.setTitle("");
        tb.setSubtitle("");
        titleTextView = (TextView) findViewById(R.id.tv_app_title);
        titleTextView.setText( getString(R.string.app_name));



        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.query("mytable", null, null, null, null, null, null);
        if (c.getCount() == 0) {
            new CreateDatabase().createDatabaseFirstTimeAppLaunch(getApplicationContext());
        }
        c.close();
        dbHelper.close();





        DictionaryActivity DicActivity = new DictionaryActivity(this);
        DicActivity.createDatabase();
        DicActivity.openDatabase();
        dic = Dictionary.getInstance();
        context = this;
        alarmTime();
        setFirst();


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        View view = navigation.findViewById(R.id.navigation_dashboard);
        view.performClick();


    }




    public void alarmTime(){
        Intent myIntent = new Intent(MainActivity.this, AlarmReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this,
                0, myIntent, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                GregorianCalendar.getInstance().getTimeInMillis(),
                repeatTime(),
                pendingIntent
        );
    }

    public long repeatTime(){
        return 86500000;
    }



    private void changeFontSize() {


        final AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("O'zizga qulay");
        alert.setMessage("Shriftni tanlang!");
        prefs = getSharedPreferences(prefName, MODE_PRIVATE);

        //---set the TextView font size to the previously saved values---
        int fontSize = prefs.getInt(FONT_SIZE_KEY, 100);

        LinearLayout linear=new LinearLayout(this);

        linear.setOrientation(LinearLayout.VERTICAL);
        text =new TextView(this);
        text.setText("Izohli lug'at");
        text.setPadding(10, 10, 10, 10);

        final SeekBar seekBar = new SeekBar(this);

        linear.addView(seekBar);
        linear.addView(text);

        seekBar.setProgress(fontSize);

        text.setTextSize(seekBar.getProgress());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                //---change the font size of the EditText---
                text.setTextSize(progress);
            }
        });

        alert.setView(linear);

        alert.setPositiveButton("Ok",new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                Toast.makeText(getApplicationContext(), "Shrift o'rnatildi",
                        Toast.LENGTH_LONG).show();

                prefs = getSharedPreferences(prefName, MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt(FONT_SIZE_KEY, (int) seekBar.getProgress());
                editor.commit();


                mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor mEditor = mSharedPrefs.edit();
                int mProgress = seekBar.getProgress();
                mEditor.putInt("mMySeekBarProgress", mProgress).commit();

            }
        });

        alert.setNegativeButton("Yopish",new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {


            }
        });

        alert.show();
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.meh, menu);
        inflater.inflate(R.menu.options_menu, menu);

        searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.searchbar));
        searchView.setQueryHint(getString(R.string.hint));

        final ListView a = (ListView) findViewById(R.id.listView2);

        a.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String selected = (String) parent.getItemAtPosition(position);
//                searchView.setQuery(selected, false);
                clicked = (String)parent.getItemAtPosition(position);
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                intent.putExtra("something",clicked);
                startActivity(intent);
                if (!DictionaryActivity.repetitionHistory(clicked)) {
                    DictionaryActivity.addtoHistory(clicked);
                }
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String searchText) {

                Intent intent = new Intent(context, SearchActivity.class);

                intent.putExtra("something", searchText = searchText.toUpperCase().replaceAll("A","а"));
                startActivity(intent);
                if (!DictionaryActivity.repetitionHistory(searchText)) {
                    DictionaryActivity.addtoHistory(searchText);
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String searchText) {


                if (!searchText.equals("")) {
                    if (searchText.length() == 1) {
//                        searchText = searchText.toUpperCase();

                        String result = null;
                        SQLiteDatabase db;
                        DBHelper dbHelper = new DBHelper(context);
                        result = searchText;
                        db = dbHelper.getReadableDatabase();

                        // делаем запрос всех данных из таблицы mytable, получаем Cursor
                        // TODO надо будет изменить колонки которые надо вызывать т.е. не все колонки нам нужны
                        Cursor c = db.query(getString(R.string.mytable), null, null, null, null, null, null);


                        // определяем номера столбцов по имени в выборке
                        int idColIndex1 = c.getColumnIndex("latin");
                        Log.i("autolog", "idColIndex1: " + idColIndex1);
                        int idColIndex2 = c.getColumnIndex("cyrillic");
                        Log.i("autolog", "idColIndex2: " + idColIndex2);

                        Integer[] positionsLatin = {69, 70, 81, 82, 17, 18, 83, 84, 85, 86, 3, 4, 11, 12, 23, 24,
                                25, 26, 37, 38, 41, 42, 51, 52, 55, 56, 65, 66, 67, 68, 1, 2, 5, 6, 7, 8,
                                9, 10, 13, 14, 15, 16, 19, 20, 21, 22, 27, 28, 29, 30, 31, 32, 33, 34, 35,
                                36, 39, 40, 43, 44, 45, 46, 47, 48, 49, 50, 53, 54, 57, 58, 59, 60, 61, 62,
                                63, 64, 73, 74, 75, 76, 79, 80, 71, 72, 77, 78};

                        for (int i = 0; i < positionsLatin.length; i++) {
                            if (c.moveToPosition(positionsLatin[i])) {
                                //перемещаемся на позицию курсора в базе
                                ;

                                Log.i("autolog", "result до: " + result);

                                // получаем значения по номерам столбцов и пишем в String s1 и s2
                                String s1 = c.getString(idColIndex1);
                                Log.i("autolog", "s1: " + s1);
                                String s2 = c.getString(idColIndex2);
                                Log.i("autolog", "s2: " + s2);

                                if (!s1.isEmpty()) {
                                    //заменяем все символы в тексте
                                    result = result.replaceAll(s1, s2);
                                }

                                Log.i("autolog", "result после: " + result);
                                // переход на следующую строку
                                // а если следующей нет (текущая - последняя), то false - выходим из цикла
                            }

                        }

                        searchText=result.toUpperCase();;


                        c.close();
                        dbHelper.close();

                    } else if (searchText.length() > 1) {

//                        searchText = searchText.toUpperCase();
                        String result = null;
                        SQLiteDatabase db;
                        DBHelper dbHelper = new DBHelper(context);
                        result = searchText;
                        db = dbHelper.getReadableDatabase();

                        // делаем запрос всех данных из таблицы mytable, получаем Cursor
                        // TODO надо будет изменить колонки которые надо вызывать т.е. не все колонки нам нужны
                        Cursor c = db.query(getString(R.string.mytable), null, null, null, null, null, null);


                        // определяем номера столбцов по имени в выборке
                        int idColIndex1 = c.getColumnIndex("latin");
                        Log.i("autolog", "idColIndex1: " + idColIndex1);
                        int idColIndex2 = c.getColumnIndex("cyrillic");
                        Log.i("autolog", "idColIndex2: " + idColIndex2);

                        Integer[] positionsLatin = {69, 70, 81, 82, 17, 18, 83, 84, 85, 86, 3, 4, 11, 12, 23, 24,
                                25, 26, 37, 38, 41, 42, 51, 52, 55, 56, 65, 66, 67, 68, 1, 2, 5, 6, 7, 8,
                                9, 10, 13, 14, 15, 16, 19, 20, 21, 22, 27, 28, 29, 30, 31, 32, 33, 34, 35,
                                36, 39, 40, 43, 44, 45, 46, 47, 48, 49, 50, 53, 54, 57, 58, 59, 60, 61, 62,
                                63, 64, 73, 74, 75, 76, 79, 80, 71, 72, 77, 78};

                        for (int i = 0; i < positionsLatin.length; i++) {
                            if (c.moveToPosition(positionsLatin[i])) {
                                //перемещаемся на позицию курсора в базе
                                ;

                                Log.i("autolog", "result до: " + result);

                                // получаем значения по номерам столбцов и пишем в String s1 и s2
                                String s1 = c.getString(idColIndex1);
                                Log.i("autolog", "s1: " + s1);
                                String s2 = c.getString(idColIndex2);
                                Log.i("autolog", "s2: " + s2);

                                if (!s1.isEmpty()) {
                                    //заменяем все символы в тексте
                                    result = result.replaceAll(s1, s2).replaceAll("ch","ч").replaceAll("sh","ш").replaceAll("o'","ў").replaceAll("g'","ғ");
                                }

                                Log.i("autolog", "result после: " + result);
                                // переход на следующую строку
                                // а если следующей нет (текущая - последняя), то false - выходим из цикла
                            }

                        }

                        searchText=result.toUpperCase();


                        c.close();
                        dbHelper.close();
                    }


                    a.setVisibility(View.VISIBLE);
                    ListAdapter ardap = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, dic.findFromDatabase(searchText));
                    a.setAdapter(ardap);
                } else {
                    a.setVisibility(View.GONE);
                }
                return false;
            }
        });






        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.fontChange:
                changeFontSize();
                break;

        }

        return true;
    }

    private void setFirst(){
        FragmentTransaction ft = sanitizer(false);
        ft.add(R.id.content, new SecondFragment());
        ft.commitAllowingStateLoss();
    }

    private void replaceFirst(){
        FragmentTransaction ft = sanitizer(true);
        ft.replace(R.id.content, new FirstFragment());
        ft.commitAllowingStateLoss();


    }

    private void replaceSecond(){
        FragmentTransaction ft = sanitizer(true);
        ft.replace(R.id.content, new SecondFragment());
        ft.commitAllowingStateLoss();
    }
    private void replaceThird(){
        FragmentTransaction ft = sanitizer(true);
        ft.replace(R.id.content, new FavouriteFragment());
        ft.commitAllowingStateLoss();
    }

    private FragmentTransaction sanitizer(boolean animation){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentById(R.id.content);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        if (animation) {
            ft.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        }
        return ft;
    }

}
