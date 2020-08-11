package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    String list[]={"CodersArts ","TCS","Google","Apple","Microsoft","TCS","Sof Stack","Infosys","Tech Mahidra","LPU","Tech Stuf","Always Happy","Tech News","ITC"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView=findViewById(R.id.lv);
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list);
        listView.setAdapter(arrayAdapter);
        registerForContextMenu(listView);


    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);
        menu.setHeaderTitle("Select the Option");
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.home)
        {
            Toast.makeText(this, "Welcome to home ", Toast.LENGTH_SHORT).show();
        }
        else if (item.getItemId()==R.id.info)
        {
            Toast.makeText(this, "Welcome To Info", Toast.LENGTH_SHORT).show();
        }
        else
        {
            return false;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        switch (id) {
            case R.id.home:
                Toast.makeText(this, "Welcome to home", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.info:
                Toast.makeText(this, "Welcome to Info", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.logout:
                Toast.makeText(this, "logout", Toast.LENGTH_SHORT).show();
                return true;
            default:

                return super.onOptionsItemSelected(item);
        }
    }

}
