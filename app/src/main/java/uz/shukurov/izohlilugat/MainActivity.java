package uz.shukurov.izohlilugat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import uz.shukurov.izohlilugat.fragments.DictinoaryFragment;
import uz.shukurov.izohlilugat.fragments.FavouriteFragment;
import uz.shukurov.izohlilugat.fragments.HistoryFragment;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();
    Toolbar tb;
    Context context;
    SearchView searchView;
    Dictionary dic;
    TextView titleTextView, text;
    private static final String FONT_SIZE_KEY = "fontsize";
    private SharedPreferences prefs, mSharedPrefs;
    private String prefName = "MyPref";
    String clicked;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = item -> {
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
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tb = findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tb.setTitle("");
        tb.setSubtitle("");
        titleTextView = (TextView) findViewById(R.id.tv_app_title);
        titleTextView.setText(getString(R.string.app_name));


        DictionaryActivity DicActivity = new DictionaryActivity(this);
        DicActivity.createDatabase();
        DicActivity.openDatabase();
        dic = Dictionary.getInstance();
        context = this;
        setFirst();

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        View view = navigation.findViewById(R.id.navigation_dashboard);
        view.performClick();


    }

    private void changeFontSize() {


        final AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("O'zingizga qulay Shriftni tanlang!");
        alert.setMessage("");
        prefs = getSharedPreferences(prefName, MODE_PRIVATE);

        //---set the TextView font size to the previously saved values---
        int fontSize = prefs.getInt(FONT_SIZE_KEY, 100);

        LinearLayout linear = new LinearLayout(this);

        linear.setOrientation(LinearLayout.VERTICAL);
        text = new TextView(this);
        text.setText("Izohli lug'at");
        linear.setPadding(10,0,0,0);
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

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
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

        alert.setNegativeButton("Yopish", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {


            }
        });

        alert.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.meh, menu);
        inflater.inflate(R.menu.options_menu, menu);

        searchView = (SearchView) menu.findItem(R.id.searchbar).getActionView();
        searchView.setQueryHint(getString(R.string.hint));

        final ListView dictionaryListView = findViewById(R.id.listView2);

        dictionaryListView.setOnItemClickListener((parent, view, position, id) -> {
            clicked = (String) parent.getItemAtPosition(position);
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            intent.putExtra("something", clicked);
            startActivity(intent);
            if (!DictionaryActivity.repetitionHistory(clicked)) {
                DictionaryActivity.addtoHistory(clicked);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String searchText) {
                Intent intent = new Intent(context, SearchActivity.class);
                intent.putExtra("something", searchText = searchText.toUpperCase().replaceAll("A", "а"));
                startActivity(intent);
                if (!DictionaryActivity.repetitionHistory(searchText)) {
                    DictionaryActivity.addtoHistory(searchText);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String searchText) {
                Log.d(TAG, "onQueryTextChange() | searchText: " + searchText);
                if (!searchText.isEmpty()) {
                    dictionaryListView.setVisibility(View.VISIBLE);
                    ListAdapter ardap = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, dic.findFromDatabase(searchText));
                    dictionaryListView.setAdapter(ardap);
                } else {
                    dictionaryListView.setVisibility(View.GONE);
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

    private void setFirst() {
        FragmentTransaction ft = sanitizer(false);
        ft.add(R.id.content, new DictinoaryFragment());
        ft.commitAllowingStateLoss();
    }

    private void replaceFirst() {
        FragmentTransaction ft = sanitizer(true);
        ft.replace(R.id.content, new HistoryFragment());
        ft.commitAllowingStateLoss();


    }

    private void replaceSecond() {
        FragmentTransaction ft = sanitizer(true);
        ft.replace(R.id.content, new DictinoaryFragment());
        ft.commitAllowingStateLoss();
    }

    private void replaceThird() {
        FragmentTransaction ft = sanitizer(true);
        ft.replace(R.id.content, new FavouriteFragment());
        ft.commitAllowingStateLoss();
    }

    private FragmentTransaction sanitizer(boolean animation) {
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
