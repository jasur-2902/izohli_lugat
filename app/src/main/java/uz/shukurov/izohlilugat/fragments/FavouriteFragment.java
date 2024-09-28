package uz.shukurov.izohlilugat.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import uz.shukurov.izohlilugat.DictionaryActivity;
import uz.shukurov.izohlilugat.R;
import uz.shukurov.izohlilugat.SearchActivity;

public class FavouriteFragment extends Fragment {
    private ListView historyList;
    private ListAdapter adapter;
    private ArrayList<String> arr;
    private String clicked;
    private Button clear;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_first, container, false);

        clear = view.findViewById(R.id.favorite_clear_button);
        clear.setOnClickListener(view1 -> clearButtonOnClick());
        return view;
    }

    private void clearButtonOnClick() {
        DictionaryActivity.clearFavourite();
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, DictionaryActivity.getFavourite());
        historyList.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        historyList = getView().findViewById(R.id.favorites_list);
        arr = DictionaryActivity.getFavourite();
        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, arr);
        historyList.setAdapter(adapter);
        historyList.setOnItemClickListener((parent, view, position, id) -> {
            clicked = (String) parent.getItemAtPosition(position);
            if (!DictionaryActivity.repetitionHistory(clicked)) {
                DictionaryActivity.addtoHistory(clicked);
            }
            Intent intent = new Intent(getContext(), SearchActivity.class);
            intent.putExtra("something", clicked);
            startActivity(intent);
        });
    }

}
