package com.example.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView lv;
    SearchView sv;
    ArrayAdapter<String> adapter;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv=findViewById(R.id.lv);
        sv=findViewById(R.id.sb);
        button=findViewById(R.id.btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,TabActivity.class);
                startActivity(intent);
            }
        });
        final ArrayList<String> arrayList=new ArrayList<String>();
        arrayList.add("LPU");
        arrayList.add("CU");
        arrayList.add("IIT");
        arrayList.add("IIM");
        arrayList.add("IISC");
        arrayList.add("AMITY");
        arrayList.add("SRM");
        arrayList.add("AIIMS");
        arrayList.add("NIT");
        arrayList.add("LU");
        arrayList.add("BHU");
        arrayList.add("DTU");
        arrayList.add("DU");
        arrayList.add("AU");
        arrayList.add("AMU");
        arrayList.add("JNU");
        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrayList);
        lv.setAdapter(adapter);

        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(arrayList.contains(query)){
                    adapter.getFilter().filter(query);
                }else{
                    Toast.makeText(MainActivity.this, "College Not Found",Toast.LENGTH_LONG).show();
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        }

    }

