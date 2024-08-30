package com.example.u7865708va1;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private TextView textViewTitle;
    private Button buttonFetchNextData;
    int postId = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewTitle = findViewById(R.id.titleTextView);
        buttonFetchNextData = findViewById(R.id.buttonFetchData);
        fetchNextPostTitle();

        buttonFetchNextData.setOnClickListener(l -> fetchNextPostTitle());

    }

    private void fetchNextPostTitle() {
        new Thread(() -> {
            try {
                URL url = new URL("https://jsonplaceholder.typicode.com/posts/" + postId);
                JSONObject jsonResponse = getParsedJsonObject(url);
                String title = jsonResponse.getString("title");
                postId++;

                // Update the UI
                runOnUiThread(() -> textViewTitle.setText(title));

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> textViewTitle.setText(R.string.failed_to_fetch_data));
            }
        }).start();
    }

    private  JSONObject getParsedJsonObject(URL url) {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            //Read the response from the connection using BufferedReader
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            //Accumulates the response in a StringBuilder by reading line by line.
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Parse the JSON response and return
            return new JSONObject(response.toString());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
