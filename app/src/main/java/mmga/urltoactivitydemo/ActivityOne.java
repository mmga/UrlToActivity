package mmga.urltoactivitydemo;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class ActivityOne extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one);
        setTitle("Activity One");
    }
}
