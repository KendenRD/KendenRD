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
import com.kenden.kendenrmapp.models.Formular;
import com.kenden.kendenrmapp.models.Reader;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReaderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReaderFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ListView listReader;
    private ArrayAdapter<String> adapterR;
    private List<String> listReaderD;
    private DatabaseReference mDB;
    private String READER_KEY = "Reader";

    public ReaderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReaderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReaderFragment newInstance(String param1, String param2) {
        ReaderFragment fragment = new ReaderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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