package uz.shukurov.izohlilugat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public class SearchActivity extends AppCompatActivity {

    private static final String FONT_SIZE_KEY = "fontsize";
    private String prefName = "MyPref";
    private Toolbar tb;
    private String zeword;
    private TextView titleTextView;
    private final Dictionary mDictionary = Dictionary.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        zeword = intent.getStringExtra("something");


        if (mDictionary.getDefinitionFromDatabase(zeword).isEmpty()) {
            setContentView(R.layout.wordnotfound);
        } else {
            setContentView(R.layout.dictionary_main);
            TextView wordbox = findViewById(R.id.wordbox);
            TextView definitionbox = findViewById(R.id.definitionbox);

            SharedPreferences prefs = getSharedPreferences(prefName, MODE_PRIVATE);

            int fontSize = prefs.getInt(FONT_SIZE_KEY, 15);
            definitionbox.setTextSize(fontSize);

            wordbox.setText(zeword);
            definitionbox.setText(mDictionary.getDefinitionFromDatabase(zeword).replaceAll("%n", "\n\n"));
            tb = findViewById(R.id.toolbar);

            setSupportActionBar(tb);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            tb.setTitle("");
            tb.setSubtitle("");
            titleTextView = (TextView) findViewById(R.id.tv_app_title);
            titleTextView.setText(getString(R.string.app_name));
        }

        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onPrepareOptionsMenu(final Menu menu) {

        if (DictionaryActivity.repetitionFavourite(zeword)) {

            menu.findItem(R.id.f).setIcon(R.drawable.ic_star_on);
        }
        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu2, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (item.getItemId() == R.id.f) {
            if (DictionaryActivity.repetitionFavourite(zeword)) {

                item.setIcon(R.drawable.ic_star);
                DictionaryActivity.deleteFromFavourite(zeword);
            } else {
                item.setIcon(R.drawable.ic_star_on);
                DictionaryActivity.addtoFavourite(zeword);
            }
        }

        return true;
    }


    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            String daWord = data.getStringExtra("something");
            if (mDictionary.getDefinitionFromDatabase(daWord) == null) {
                setContentView(R.layout.wordnotfound);
            } else {
                setContentView(R.layout.dictionary_main);
                TextView wordbox = findViewById(R.id.wordbox);
                TextView definitionbox = findViewById(R.id.definitionbox);
                wordbox.setText(daWord);
                definitionbox.setText(mDictionary.getDefinitionFromDatabase(daWord).replaceAll("%n", "\n"));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    tb = findViewById(R.id.toolbar);
                    setSupportActionBar(tb);
                    getSupportActionBar().setDisplayShowTitleEnabled(false);
                    tb.setTitle("");
                    tb.setSubtitle("");
                    titleTextView = findViewById(R.id.tv_app_title);
                    titleTextView.setText(getString(R.string.app_name));
                    zeword = daWord;
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
