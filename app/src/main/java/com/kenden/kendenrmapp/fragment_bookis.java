package com.kenden.kendenrmapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;


public class fragment_bookis extends Fragment {





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final AutoCompleteTextView actv;
        final String[] readerList = {
                "Оюн Кан-оол Чургуй оглу",
                "Монгуш Хулер Чамзыевич",
                "Иванов Иван Иванович",
                "Ондар Геннадий Васильевич",
                "Вакуленко Василий Михайлович",
                "Хертек Кара-оол Шулууевич",
                "Шестаков Алексей Алексеевич",
                "Лукашин Федор Андреевич",
                "Салчак Чамзы Евгеньевич"
        };
        actv = getView().findViewById(R.id.complete);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(container.getContext(), android.R.layout.simple_list_item_1, readerList);
        actv.setThreshold(1);
        actv.setAdapter(arrayAdapter);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bookis, container, false);
    }
}