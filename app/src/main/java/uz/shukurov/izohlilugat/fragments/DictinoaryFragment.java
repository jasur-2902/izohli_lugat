package uz.shukurov.izohlilugat.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import uz.shukurov.izohlilugat.DictionaryActivity;
import uz.shukurov.izohlilugat.R;
import uz.shukurov.izohlilugat.SearchActivity;
import uz.shukurov.izohlilugat.adapter.DictionaryCursorAdapter;


public class DictinoaryFragment extends Fragment {
    private static final String TAG = DictinoaryFragment.class.getName();


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final ListView dictionaryList = getView().findViewById(R.id.favorites_list);

        // Loading new words as the user is scrolling
        // This way, it's a lot more optimized, and better for UI thread :)
        final Cursor cursor = DictionaryActivity.getDictionaryCursor();
        final DictionaryCursorAdapter adapter = new DictionaryCursorAdapter(getContext(), cursor, 0);
        dictionaryList.setAdapter(adapter);

        // As user clicks on a word it should open that word
        // When I did it in 2016(long time ago), I used activity, which is inefficient
        // this should be done in fragments
        dictionaryList.setOnItemClickListener((parent, clickView, position, id) -> {
            // Get the cursor from the adapter

            // Retrieve the string you need from the cursor
            final String clicked = cursor.getString(cursor.getColumnIndex("latin"));

            // Check and add to history
            if (!DictionaryActivity.repetitionHistory(clicked)) {
                DictionaryActivity.addtoHistory(clicked);
            }

            // Start the new activity with the clicked item
            Intent intent = new Intent(getContext(), SearchActivity.class);
            intent.putExtra("something", clicked);
            startActivity(intent);
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_second, container, false);
    }

}
