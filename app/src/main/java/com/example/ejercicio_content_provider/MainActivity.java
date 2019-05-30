package com.example.ejercicio_content_provider;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.provider.CallLog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private ListView list;
    private ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
       allCalls();
    }

    private void init() {

        list = findViewById(R.id.listaLv);
    }

    public void allCalls(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CALL_LOG}, PermissionsCalls.PERMISSIONS_REQUEST_READ_CALL_LOG);
        }else{
            List<String> calls = getCalls();
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, calls);
            list.setAdapter(adapter);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[]results){
        if(requestCode ==PermissionsCalls.PERMISSIONS_REQUEST_READ_CALL_LOG){
            if(results[0] == PackageManager.PERMISSION_GRANTED){
                allCalls();
            }else{
                Toast.makeText(this, "Se necesita permisos para mostrar el contenido",
                       Toast.LENGTH_SHORT ).show();
            }
        }
    }

    public List<String> getCalls() {
        List<String> calls = new ArrayList<>();
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(CallLog.Calls.CONTENT_URI, null, null, null, null);

        if (cursor.moveToFirst()){
            do{
                String call = cursor.getString((cursor.getColumnIndex(CallLog.Calls.NUMBER)));
                calls.add(call);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return calls;


    }

}
