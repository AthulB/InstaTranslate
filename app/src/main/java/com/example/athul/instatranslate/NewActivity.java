package com.example.athul.instatranslate;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


/**
 * Created by Athul on 10-03-2018.
 */

public class NewActivity extends AppCompatActivity {
    TextView textView;
    TextView textView1;
    //
    String text,text1;
    Spinner spinner;
    String item;
    String [] lang = {"Bulgarian","Dutch","English","German","Greek","Gujarati","Hindi","Italian","Marathi","Malayalam","Russian","Spanish",};
    boolean connected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);
        textView = (TextView) findViewById(R.id.textView);
        textView1 = (TextView) findViewById(R.id.textView2);

        text = getIntent().getExtras().getString("data");
        text1 = text.replaceAll("\\n"," ");
        textView.setText(text1);
        spinner = (Spinner)findViewById(R.id.spinner2);
        textView.setMovementMethod(new ScrollingMovementMethod());
        textView1.setMovementMethod(new ScrollingMovementMethod());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,lang);
        spinner.setAdapter(adapter);
        textView1.setText("Select the language to be translated to....");


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String Value= spinner.getSelectedItem().toString();
                switch(Value){
                    case "Bulgarian": item="bg";
                        break;
                    case "Dutch": item="nl";
                        break;
                    case "English": item="en";
                        break;
                    case "German": item="de";
                        break;
                    case "Greek": item="el";
                        break;
                    case "Gujarati": item="gu";
                        break;
                    case "Hindi": item="hi";
                        break;
                    case "Italian": item="it";
                        break;
                    case "Marathi": item="mr";
                        break;
                    case "Malayalam": item="ml";
                        break;
                    case "Russian": item="ru";
                        break;
                    case "Spanish": item="es";
                        break;
                    default : item="bg";
                }
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }




    public void translate(View view) {
        textView1.setText("Your Translation will be available soon......");
        new JsonTask().execute("https://translate.yandex.net/api/v1.5/tr.json/translate?key=trnsl.1.1.20180309T152246Z.4f19a932b29b98f4.e5ce75fa97145270e537e8ecc2d3aa80ed4d1631&text="+text1+"&lang=en-"+item);
    }

    class JsonTask extends AsyncTask<String, String, String> {

        HttpsURLConnection connection = null;
        BufferedReader reader = null;
        StringBuffer stringBuffer;

        @Override
        protected String doInBackground(String... strings) {

            URL url = null;
            try {
                url = new URL(strings[0]);
                connection = (HttpsURLConnection) url.openConnection();
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(inputStream));

                stringBuffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    stringBuffer.append(line);
                }

                String jsonFile =  stringBuffer.toString();
                JSONObject jsonObject = new JSONObject(jsonFile);
                JSONArray jsonArray = jsonObject.getJSONArray("text");
                String Text = jsonArray.getString(0);
                return Text;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                    connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                //we are connected to a network
                connected = true;
            }
            else
                connected = false;
            if(connected==true) {
                textView1.setText(s);
            }
            else
            {
                textView1.setText("Please Connect to the Internet");
            }
        }
    }

}


