package com.example.testappcustomer;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;


import androidx.appcompat.app.AppCompatActivity;

import com.example.testapp.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.DataOutputStream;
import java.util.ArrayList;

import android.util.Log;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "All Users";
    private ListView listView;
    private ArrayAdapter<String> adapter;
    EditText PersonName;
    Button button_generate;
    ImageView qr_code;
    Button button_get_name;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.testapp.R.layout.activity_main);

        PersonName = findViewById(R.id.PersonName);
        button_generate = findViewById(R.id.button_generate);
        qr_code = findViewById(R.id.qr_code);
        button_get_name = findViewById(R.id.button_get_name);

        button_generate.setOnClickListener(view -> {
            String name = PersonName.getText().toString().trim();
            Recipient recipient = new Recipient(name);
            recipient.sendPost();
            recipient.generateQRCode(qr_code);
        });

        button_get_name.setOnClickListener(view -> {
            new DownloadJsonTask().execute("http://131.173.65.77:3000/all-users");
            listView = findViewById(R.id.user_list_view);
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<String>());
            listView.setAdapter(adapter);
        });
    }


    public void generateQRCode() {
        String text = PersonName.getText().toString().trim();
        MultiFormatWriter writer = new MultiFormatWriter();
        try {
            BitMatrix matrix = writer.encode(text, BarcodeFormat.QR_CODE, 400, 400);
            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap bitmap = encoder.createBitmap(matrix);
            qr_code.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }



    private class DownloadJsonTask extends AsyncTask<String, Void, ArrayList<String>> {
        @Override
        protected ArrayList<String> doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                connection.connect();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();

              /*  return stringBuilder.toString();
            } catch (IOException e) {
                Log.e(TAG, "Error downloading JSON data", e);
                return null;
            }*/
                ArrayList<String> list = new ArrayList<>();
                JSONArray jsonArray = new JSONArray(stringBuilder.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String value = jsonObject.getString("name");
                    list.add(value);
                }

                return list;

            } catch (IOException | JSONException e) {
                Log.e(TAG, "Error downloading or decoding JSON data", e);
                return null;
            }
        }

        /* @Override
         protected void onPostExecute(String result) {
             if (result != null) {
                 Log.d(TAG, "Downloaded JSON data: " + result);
             }
         }*/
        protected void onPostExecute(ArrayList<String> result) {
            adapter.clear();
            if (result != null) {
                for (String value : result) {
                    adapter.add(value);
                }
            }


        }
    }
}

