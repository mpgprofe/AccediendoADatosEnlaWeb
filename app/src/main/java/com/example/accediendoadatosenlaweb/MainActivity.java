package com.example.accediendoadatosenlaweb;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AsyncNotedAppOp;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    Button buttonCSV, buttonJSON, buttonXML;
    ListView lista;
    ProgressDialog progressDialog;
    static String SERVIDOR = "http://192.168.0.185";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonCSV = findViewById(R.id.buttonCSV);
        buttonJSON = findViewById(R.id.buttonCSV);
        buttonXML = findViewById(R.id.buttonXML);
        lista = findViewById(R.id.lista);

        buttonCSV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DescargarCSV descargarCSV = new DescargarCSV();
                descargarCSV.execute("/webmanuel/listadoCSV.php");
            }
        });


    }

   private class DescargarCSV extends AsyncTask<String, Void, Void>{
        String total ="";
       @Override
       protected void onPreExecute() {
           super.onPreExecute();
           progressDialog = new ProgressDialog(MainActivity.this);
           progressDialog.setTitle("Descargando datos");
           progressDialog.setIndeterminate(true);
           progressDialog.show();

       }

       @Override
       protected void onPostExecute(Void aVoid) {
           super.onPostExecute(aVoid);

           ArrayAdapter<String> adapter;
           List<String> list  = new ArrayList<String>();

           String[] lineas= total.split("\n");

           for(String linea: lineas){
               String[] campos = linea.split(";");
               String dato = "ID: "+campos[0];
               dato+="MODELO: "+campos[1];
               dato+="MARCA: "+campos[2];
               dato+="PRECIO: "+campos[3];
               list.add(dato);
           }
           adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item,list);
           lista.setAdapter(adapter);

           progressDialog.dismiss();

       }

       @Override
       protected void onProgressUpdate(Void... values) {
           super.onProgressUpdate(values);
       }

       @Override
       protected Void doInBackground(String... strings) {

           String script = strings[0];
           URL url;
           HttpURLConnection httpURLConnection;

           try {
               url = new URL(SERVIDOR+script);
               httpURLConnection = (HttpURLConnection) url.openConnection();

               if (httpURLConnection.getResponseCode() == HttpsURLConnection.HTTP_OK){
                   InputStream inputStream = httpURLConnection.getInputStream();
                   BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

                   String linea ="";
                   while((linea = br.readLine())!=null){
                       total+=linea;
                   }
                   br.close();
                   inputStream.close();

               }



           } catch (MalformedURLException e) {
               e.printStackTrace();
           } catch (IOException e) {
               e.printStackTrace();
           }


           return null;
       }
   }

}