package com.kenden.kendenrmapp;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kenden.kendenrmapp.models.Formular;

import java.util.ArrayList;
import java.util.List;


public class fragment_formular extends Fragment {
    private ListView listForm;
    private ArrayAdapter<String> adapterF;
    private List<String> listFormD;
    private List<String> listFormDD;
    private List<Formular> listTemp;
    private DatabaseReference mDataBF;
    private String FORMULAR_KEY = "formular";
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
                if(listTemp.size() > 0)listTemp.clear();
                for(DataSnapshot ds : snapshot.getChildren())
                {
                    Formular formular = ds.getValue(Formular.class);
                    listFormD.add(formular.name);
                    listTemp.add(formular);

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
                ValueEventListener valueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        if(listFormDD.size() > 0)listFormDD.clear();
                        for(DataSnapshot ds : snapshot.getChildren())
                        {
                            Formular formular1 = ds.getValue(Formular.class);
                            listFormDD.add(formular1.book);

                            assert formular1 != null;
                        }
                        adapterFF.notifyDataSetChanged();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                };
                mDataBF.addValueEventListener(valueEventListener);

                dialog.setNegativeButton("Отменить", new DialogInterface.OnClickListener() {//кнопка отменить впл. окно
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                    }
                });

                dialog.setPositiveButton("Принять книги", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {

                    }
                });
                dialog.show();
            }
        });
    }
}