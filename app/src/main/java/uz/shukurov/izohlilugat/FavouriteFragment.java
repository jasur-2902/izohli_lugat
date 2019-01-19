package uz.shukurov.izohlilugat;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;


public class FavouriteFragment extends Fragment {

    ListView historyList;
    ListAdapter adapter;
    ArrayList<String> arr;
    String clicked;
    Button clear;
    public FavouriteFragment() {
        // Required empty public constructor
    }

    public static FavouriteFragment newInstance(){
        return new FavouriteFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_first,container,false);

        clear = (Button) view.findViewById(R.id.clearButton);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearButtonOnClick();
            }
        });
        return view;
    }

    private void clearButtonOnClick() {
        DictionaryActivity.clearFavourite();
        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, DictionaryActivity.getHistory());
        historyList.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        historyList = (ListView) getView().findViewById(R.id.historyList);
        arr = DictionaryActivity.getFavourite();
        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, arr);
        historyList.setAdapter(adapter);
        historyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                clicked = (String)parent.getItemAtPosition(position);
//                Intent intent = new Intent();
//                intent.putExtra("something",clicked);
//                setResult(RESULT_OK,intent);
//                finish();
                if (!DictionaryActivity.repetitionHistory(clicked)) {
                    DictionaryActivity.addtoHistory(clicked);
                }
                Intent intent = new Intent(getContext(), SearchActivity.class);
                intent.putExtra("something",clicked);
                startActivity(intent);
            }
        });
    }

}
