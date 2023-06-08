package com.example.testappcustomer;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * The type Recipient.
 */
public class Recipient {
    /**
     * Width and height of QRCode.
     */
  private static final int WIDTHHEIGHTNR = 400;
    /**
     * Customer Name.
     */
  private String name;

    private URL url;

    /**
     * Instantiates a new Recipient.
     */
    public Recipient() {}

    public void setURL(URL url) {
        this.url = url;
    }
    /**
     * Instantiates a new Recipient.
     *
     * @param name the name
     */
    public Recipient(String name) {
        this.name = name;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }


    /**
     * Generates a QR code based on the provided text and displays
     * it in the specified ImageView.
     * The text is trimmed to remove leading and trailing whitespace
     * before generating the QR code.
     * The generated QR code is then set as the image in the
     * specified ImageView.
     * @param qrCodeImageView The ImageView in which to display the
     *                        generated QR code.
     */
    public void generateQRCode(ImageView qrCodeImageView) {
        String text = name.trim();
        MultiFormatWriter writer = new MultiFormatWriter();
        try {
            BitMatrix matrix = writer.encode(text,
                    BarcodeFormat.QR_CODE, WIDTHHEIGHTNR, WIDTHHEIGHTNR);
            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap bitmap = encoder.createBitmap(matrix);
            qrCodeImageView.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }



    /**
     * Sends a HTTP-POST request to a specified URL which is
     * the database with the recipient's name as JSON web token in
     * the request body.
     * The method runs in a separate thread to perform the
     * network operation.
     * The console displays a HTTP code whether the JSON web token
     * was post successful or not
     */
    public void sendPost() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://131.173.65.77:3000/test-user");
                    HttpURLConnection conn = (HttpURLConnection)
                            url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type",
                            "application/json;charset=UTF-8");
                    conn.setRequestProperty("Accept",
                            "application/json");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("name", name);

                    Log.i("JSON",
                            jsonParam.toString());
                    DataOutputStream os =
                            new DataOutputStream(conn.getOutputStream());
                    os.writeBytes(jsonParam.toString());
                    os.flush();
                    os.close();
                    Log.i("STATUS",
                            String.valueOf(conn.getResponseCode()));
                    Log.i("MSG",
                            conn.getResponseMessage());

                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

}
