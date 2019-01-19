package uz.shukurov.izohlilugat;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;



public class SecondFragment extends Fragment {


    ListView historyList;
    ListAdapter adapter;
    ArrayList<String> arr;
    String clicked;

    public SecondFragment() {
        // Required empty public constructor
    }

    public static SecondFragment newInstance(){
        return new SecondFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second,container,false);


        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        historyList = (ListView) getView().findViewById(R.id.historyList);
        arr = DictionaryActivity.getDictionary();
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
