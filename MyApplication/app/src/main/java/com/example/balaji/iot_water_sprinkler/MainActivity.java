package com.example.balaji.iot_water_sprinkler;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText edit_update1;
    Button button_update1;
    TextView text_time1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edit_update1 = (EditText) findViewById(R.id.edit_update);
        button_update1 = (Button) findViewById(R.id.button_update);
        text_time1 = (TextView) findViewById(R.id.textview_time);
        button_update1.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.button_update:
                new Thread(new Runnable() {
                    public void run() {

                        try {
                            URL url = new URL("http://54.183.57.169:8080/Project_Test_Android/test_servlet");
                            URLConnection connection = url.openConnection();

                            String string_update_time = edit_update1.getText().toString();
                            Log.d("Update Value", string_update_time);

                            connection.setDoOutput(true);
                            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
                            out.write(string_update_time);
                            out.close();
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }).start();
                break;
        }
    }
}
