package mmga.urltoactivitydemo;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            Intent intent1 = new Intent(this, ActivityTwo.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent1);
            finish();
        } else if (Intent.ACTION_MAIN.equals(intent.getAction())) {
            Intent intent1 = new Intent();
            intent1.setClass(this, MainActivity.class);
            startActivity(intent1);
            finish();
        }
    }
}
