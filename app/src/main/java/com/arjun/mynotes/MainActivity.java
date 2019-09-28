package com.arjun.mynotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    static ArrayList<String> notes;
    static SharedPreferences sharedPreferences;
    static ListView listView;
    static ArrayAdapter<String> arrayAdapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        Intent intent=new Intent(getApplicationContext(), Activity2.class);
        intent.putExtra("note","");//Share data with other Activities
        intent.putExtra("index", -1);
        startActivity(intent);

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notes=new ArrayList<String>();

        sharedPreferences=this.getSharedPreferences("com.arjun.mynotes", Context.MODE_PRIVATE);


        try {
            if((ArrayList<String>)ObjectSerializer.deserialize(sharedPreferences.getString("notes",""))!=null)
                notes=(ArrayList<String>)ObjectSerializer.deserialize(sharedPreferences.getString("notes",""));

        } catch (IOException e) {

            e.printStackTrace();
        }

        listView=(ListView)findViewById(R.id.list);

        arrayAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, notes);

        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent=new Intent(getApplicationContext(), Activity2.class);

                intent.putExtra("note",notes.get(i));//Share data with other Activities
                intent.putExtra("index", i);

                startActivity(intent);

            }
        });


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View v, final int index, long arg3) {

                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Do you want to delete it?")
                        .setMessage("Are you sure?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                notes.remove(index);
                                try {
                                    sharedPreferences.edit().putString("notes", ObjectSerializer.serialize(notes)).apply();
                                }
                                catch (IOException e){
                                    e.printStackTrace();
                                }
                                listView.setAdapter(arrayAdapter);

                            }
                        })
                        .setNegativeButton("No", null)
                        .show();

                return true;
            }
        });
    }
}
