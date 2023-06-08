package com.example.testappcustomer;


import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.testapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * The class Main activity.
 */
public class MainActivity extends AppCompatActivity {
    /**
     * The tag for all users.
     */
    private static final String TAG = "All Users";
    /**
     * The Listview.
     */
    private ListView listView;
    /**
     * The arraylist for the names.
     */
    private ArrayAdapter<String> adapter;
    /**
     * The Person name.
     */
    private EditText personName;
    /**
     * The Button generate.
     */
    private Button buttonGenerate;


    /**
     * The Qr code.
     */
    private ImageView qrCode;
    /**
     * The Button get name.
     */
    private Button buttonGetName;


    /**
     * This method is called when the activity is created.
     * It initializes the layout and views, and sets up the
     * click listeners for the "Generate" and "Get Name" buttons.
     *
     * The "Generate" button click listener retrieves the name
     * entered in the "PersonName" field, creates a
     * recipient object with the entered name,
     * sends a POST request to a server using the
     * recipient's information and generates a QR code
     * representing the recipient's data.
     * The QR code is then displayed in the corresponding view.
     *
     * The "Get Name" button click listener initiates a task to download
     * JSON data from the specified URL ("http://131.173.65.77:3000/all-users")
     * It sets up a ListView and an ArrayAdapter to
     * display the downloaded user names in the list view.
     *
     * @param savedInstanceState The saved instance state of the activity
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.testapp.R.layout.activity_main);

        personName = findViewById(R.id.PersonName);
        buttonGenerate = findViewById(R.id.button_generate);
        qrCode = findViewById(R.id.qr_code);
        buttonGetName = findViewById(R.id.button_get_name);

        buttonGenerate.setOnClickListener(view -> {
            String name = personName.getText().toString().trim();
            Recipient recipient = new Recipient(name);
            recipient.sendPost();
            recipient.generateQRCode(qrCode);
        });

        buttonGetName.setOnClickListener(view -> {
            new DownloadJsonTask().execute("http://131.173.65.77:3000/all-users");
            listView = findViewById(R.id.user_list_view);
            adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1,
                    new ArrayList<String>());
            listView.setAdapter(adapter);
        });
    }

    /**
     * This AsyncTask subclass performs a background
     * task to download JSON data from the specified URL.
     * The downloaded JSON data is then parsed to extract
     * the "name" field from each JSON object and the extracted
     * names are stored in an ArrayList.
     *
     */
    private class DownloadJsonTask extends AsyncTask<String, Void, ArrayList<String>> {
        @Nullable
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

        /**
         * This method is called after the background
         * task (JSON data download and parsing) is completed.
         * It updates the adapter with the downloaded names,
         * clearing the previous data and adding the new names.
         * If the result is not null, the names are added to the adapter.
         *
         * @param result The ArrayList containing the
         *               extracted names from the downloaded
         *               JSON data.
         */
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

