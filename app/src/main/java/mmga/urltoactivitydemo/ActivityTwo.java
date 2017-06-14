package mmga.urltoactivitydemo;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.text.MessageFormat;

public class ActivityTwo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);
        setTitle("Activity Two");

        TextView textId = (TextView) findViewById(R.id.text_id);

        Intent intent = getIntent();
        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            Uri data = intent.getData();
            String id = data.getQueryParameter("id");
            textId.setText(MessageFormat.format("id = {0}", id));
        }
    }
}
