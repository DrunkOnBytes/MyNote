package com.arjun.mynotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.io.IOException;

public class Activity2 extends AppCompatActivity {

    EditText text;
    String note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        final Intent intent=getIntent();

        text=(EditText)findViewById(R.id.editText);

        note="";

        MainActivity.notes.add(note);

        if(intent.getIntExtra("index",-1)!=-1)
            MainActivity.notes.remove(intent.getIntExtra("index", -1));



            text.setText(intent.getStringExtra("note"));

            text.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    note=charSequence.toString();

                        MainActivity.notes.remove(MainActivity.notes.size()-1);
                        MainActivity.notes.add(note);

                    try {
                        MainActivity.sharedPreferences.edit().putString("notes", ObjectSerializer.serialize(MainActivity.notes)).apply();
                    }
                    catch (IOException e){
                        e.printStackTrace();
                    }

                    MainActivity.listView.setAdapter(MainActivity.arrayAdapter);

                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

    }

}
