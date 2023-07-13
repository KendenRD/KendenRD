package com.kenden.kendenrmapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kenden.kendenrmapp.models.Reader;

import java.util.ArrayList;
import java.util.List;


public class ReaderFragment extends Fragment {
    private ListView listReader;
    private ArrayAdapter<String> adapterR;
    private List<String> listReaderD;
    private DatabaseReference mDB;
    private String READER_KEY = "Reader";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reader, container, false);
        listReader = view.findViewById(R.id.listReader);
        listReaderD = new ArrayList<>();
        adapterR = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, listReaderD);
        listReader.setAdapter(adapterR);
        mDB = FirebaseDatabase.getInstance().getReference(READER_KEY);

        showListReader();

        return view;
    }

    private void showListReader()
    {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if(listReaderD.size() > 0)listReaderD.clear();
                for(DataSnapshot ds : snapshot.getChildren())
                {
                    Reader reader = ds.getValue(Reader.class);
                    listReaderD.add(reader.dName);
                    assert reader != null;
                }
                adapterR.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mDB.addValueEventListener(valueEventListener);
    }
}