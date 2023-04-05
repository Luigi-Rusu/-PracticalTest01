package ro.pub.cs.systems.eim.practicaltest01.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ro.pub.cs.systems.eim.practicaltest01.R;
import ro.pub.cs.systems.eim.practicaltest01.general.Constants;

public class PracticalTest01SecondaryActivity extends AppCompatActivity {

    private String allTerms = "";
    private int sum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test01_main);

        Intent intent = getIntent();
        if (intent != null && intent.getExtras().containsKey(Constants.ALL_TERMS)) {
            allTerms = intent.getExtras().get(Constants.ALL_TERMS).toString();
        }

        if (!allTerms.isEmpty()) {
            for (int i = 0; i < allTerms.length(); ++i) {
                if (allTerms.charAt(i) != '+') {
                    sum += Integer.parseInt(String.valueOf(allTerms.charAt(i)));
                }
            }
            setResult(sum);
        } else {
            setResult(sum);
        }
        finish();
    }
}
