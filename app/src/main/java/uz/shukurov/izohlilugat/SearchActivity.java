package uz.shukurov.izohlilugat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

/**
 * Created by yumak on 17-05-2016.
 */
public class SearchActivity extends AppCompatActivity {

    private MenuItem menuItem;
    private Menu menu;
    Toolbar tb;

    private SharedPreferences prefs,mSharedPrefs;
    private static final String FONT_SIZE_KEY = "fontsize";
    private static final String TEXT_VALUE_KEY = "textvalue";
    private String prefName = "MyPref";

    Context context;
    String zeword;
    TextView titleTextView;
    TextToSpeech convert2speech;
    Dictionary dic = Dictionary.getInstance();
    public static final int FAVOURITE_REQUEST = 1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        zeword = intent.getStringExtra("something");


        if(dic.getDefinitionFromDatabase(zeword).isEmpty())
        {
            setContentView(R.layout.wordnotfound);
        }
        else {
            setContentView(R.layout.dictionary_main);
            TextView wordbox = (TextView) findViewById(R.id.wordbox);
            TextView definitionbox = (TextView) findViewById(R.id.definitionbox);

            prefs = getSharedPreferences(prefName, MODE_PRIVATE);
//            int mProgress = mSharedPrefs.getInt("mMySeekBarProgress", 0);


            int fontSize = prefs.getInt(FONT_SIZE_KEY, 15);
            definitionbox.setTextSize(fontSize);

            wordbox.setText(zeword);
            definitionbox.setText(dic.getDefinitionFromDatabase(zeword).replaceAll("%n","\n\n"));
            tb = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(tb);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            tb.setTitle("");
            tb.setSubtitle("");
            titleTextView = (TextView) findViewById(R.id.tv_app_title);
            titleTextView.setText( getString(R.string.app_name));

        }
        updateMenuIcon();

        super.onCreate(savedInstanceState);
    }



    private void updateMenuIcon() {
     //   MenuItem bedMenuItem = menu.findItem(R.id.about);
       // bedMenuItem.setTitle("hello");
       // bedMenuItem.setIcon(R.drawable.dictionary_app_icon);
    }



    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        if(DictionaryActivity.repetitionFavourite(zeword)) {

            menu.findItem(R.id.f).setIcon(R.drawable.ic_star_on);
        }
        return super.onPrepareOptionsMenu(menu);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu2, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.f:

                if(DictionaryActivity.repetitionFavourite(zeword)) {

                    item.setIcon(R.drawable.ic_star);
                    DictionaryActivity.deleteFromFavourite(zeword);
                }
                else {
                    item.setIcon(R.drawable.ic_star_on);
                    DictionaryActivity.addtoFavourite(zeword);
                }

                break;
        }

        return true;
    }




    @Override
    public void onStart()
    {
        super.onStart();

    }

    public void addbuttonOnClick(View v)
    {
//        Intent intent = new Intent(this, AddActivity.class);
//        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1 && resultCode == RESULT_OK){
            String daWord = data.getStringExtra("something");
            if(dic.getDefinitionFromDatabase(daWord) == null)
            {
                setContentView(R.layout.wordnotfound);
            }
            else {
                setContentView(R.layout.dictionary_main);TextView wordbox = (TextView) findViewById(R.id.wordbox);
                TextView definitionbox = (TextView) findViewById(R.id.definitionbox);
                wordbox.setText(daWord);
                definitionbox.setText(dic.getDefinitionFromDatabase(daWord).replaceAll("%n","\n"));
                tb = (Toolbar) findViewById(R.id.toolbar);
                setSupportActionBar(tb);
                getSupportActionBar().setDisplayShowTitleEnabled(false);
                tb.setTitle("");
                tb.setSubtitle("");
                titleTextView = (TextView) findViewById(R.id.tv_app_title);
                titleTextView.setText( getString(R.string.app_name));
                zeword=daWord;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void deleteButtonOnClick(View v)
    {

//        startActivityForResult(new Intent(SearchActivity.this,FavouriteActivity.class),FAVOURITE_REQUEST);
//        final String word = ((TextView) findViewById(R.id.wordbox)).getText().toString();
//
//        AlertDialog.Builder alert = new AlertDialog.Builder(this);
//        alert.setTitle("Delete Confirmation").setMessage("Are you sure you want to delete the word '" + word + "'?");
//        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//            }
//        });
//        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dic.Delete(word);
//                finish();
//            }
//        });
//
//        alert.show();

    }

    public void updateButtonOnClick(View v)
    {

        // Toast.makeText(SearchActivity.this,zeword,Toast.LENGTH_LONG).show();

        if(!DictionaryActivity.repetitionFavourite(zeword))
        {
            DictionaryActivity.addtoFavourite(zeword);
        }


//        String currentWord = ((TextView) findViewById(R.id.wordbox)).getText().toString();
//        Intent intent = new Intent(this,UpdateActivity.class);
//        intent.putExtra("something",currentWord);
//        startActivityForResult(intent,1);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }


}
