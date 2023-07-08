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
import com.kenden.kendenrmapp.models.Book;
import com.kenden.kendenrmapp.models.Reader;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_catalogizator#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_catalogizator extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ListView listCat;
    private ArrayAdapter<String> adapterC;
    private List<String> listCatD;
    private DatabaseReference mDBase;
    private String BOOK_KEY = "Book";

    public fragment_catalogizator() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_catalogizator.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_catalogizator newInstance(String param1, String param2) {
        fragment_catalogizator fragment = new fragment_catalogizator();
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
        View view = inflater.inflate(R.layout.fragment_catalogizator, container, false);

        listCat = view.findViewById(R.id.listCat);
        listCatD = new ArrayList<>();
        adapterC = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, listCatD);
        listCat.setAdapter(adapterC);
        mDBase = FirebaseDatabase.getInstance().getReference(BOOK_KEY);

        showListCat();

        return view;
    }

    private void showListCat()
    {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if(listCatD.size() > 0)listCatD.clear();
                for(DataSnapshot ds : snapshot.getChildren())
                {
                    Book book = ds.getValue(Book.class);
                    listCatD.add(book.dTitle);
                    assert book != null;
                }
                adapterC.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mDBase.addValueEventListener(valueEventListener);
    }
}