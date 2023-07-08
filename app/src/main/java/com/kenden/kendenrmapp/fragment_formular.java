package com.kenden.kendenrmapp;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kenden.kendenrmapp.models.Formular;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_formular#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_formular extends Fragment {


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;

    private Context context;
    private String mParam2;
    private ListView listForm;
    private ArrayAdapter<String> adapterF;
    private List<String> listFormD;
    private List<String> listFormDD;
    private List<Formular> listTemp;
    private DatabaseReference mDataBF;
    private String FORMULAR_KEY = "formular";

    public fragment_formular() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_formular.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_formular newInstance(String param1, String param2) {
        fragment_formular fragment = new fragment_formular();
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
        View view = inflater.inflate(R.layout.fragment_formular, container, false);

        listForm = view.findViewById(R.id.listForm);
        listFormD = new ArrayList<>();
        listFormDD = new ArrayList<>();
        listTemp = new ArrayList<>();
        adapterF = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, listFormD);
        listForm.setAdapter(adapterF);
        mDataBF = FirebaseDatabase.getInstance().getReference(FORMULAR_KEY);

        showListForm();
        setOnClickItem();





        return view;
    }

    private void showListForm()
    {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if(listFormD.size() > 0)listFormD.clear();
                if(listFormDD.size() > 0)listFormDD.clear();
                if(listTemp.size() > 0)listTemp.clear();
                for(DataSnapshot ds : snapshot.getChildren())
                {
                    Formular formular = ds.getValue(Formular.class);
                    listFormD.add(formular.name);
                    listTemp.add(formular);
                    listFormDD.add(formular.book);

                    assert formular != null;
                }
                adapterF.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mDataBF.addValueEventListener(valueEventListener);
    }

    private void setOnClickItem() {
        listForm.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Formular formular = listTemp.get(position);

                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());//вызов всп. окна
                dialog.setTitle(formular.name);
                dialog.setMessage(formular.dateTime);

                LayoutInflater inflater = LayoutInflater.from(getActivity());//берем шаблон строк
                View fragment_form = inflater.inflate(R.layout.fragment_form, null);
                dialog.setView(fragment_form);

                final ListView listField = fragment_form.findViewById(R.id.listF);
                final ArrayAdapter<String> adapterFF = new ArrayAdapter<>(getActivity(), android.R.layout.select_dialog_item, listFormDD);
                listField.setAdapter(adapterFF);
                adapterFF.notifyDataSetChanged();


                dialog.setNegativeButton("Отменить", new DialogInterface.OnClickListener() {//кнопка отменить впл. окно
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                    }
                });

                dialog.setPositiveButton("Оформить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        //проверка на пустоту строки


                        //оформление читателя

                        //bookField.setText(formular.book);
                    }
                });
                dialog.show();
            }
        });
    }
}