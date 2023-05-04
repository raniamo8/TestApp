package com.example.testappcustomer;

import static com.example.testappcustomer.Recipient.getNameList;

import com.example.testappcustomer.Recipient;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.PersistableBundle;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.w3c.dom.Text;

import android.util.Log;

public class MainActivity extends AppCompatActivity {

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


        button_get_name.setOnClickListener(view ->{
            Recipient recipientGetName = new Recipient();
            recipientGetName.doInBackground();
        });
    }




}
