package ro.pub.cs.systems.eim.practicaltest01.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ro.pub.cs.systems.eim.practicaltest01.R;
import ro.pub.cs.systems.eim.practicaltest01.general.Constants;
import ro.pub.cs.systems.eim.practicaltest01.service.PracticalTest01Service;

public class PracticalTest01MainActivity extends AppCompatActivity {

    private EditText nextTerm;
    private TextView allTerms;
    private Button addButton, computeButton;
    private int sumOfAll = 0;

    private IntentFilter intentFilter = new IntentFilter();
    private MessageBroadcastReceiver messageBroadcastReceiver = new MessageBroadcastReceiver();
    private class MessageBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(Constants.BROADCAST_RECEIVER_TAG, intent.getStringExtra(Constants.BROADCAST_RECEIVER_EXTRA));
        }
    }

    private ButtonClickListener buttonClickListener = new ButtonClickListener();
    private class ButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.add_button) {
                if (!nextTerm.getText().toString().isEmpty()) {
                    if (allTerms.getText().toString().isEmpty()) {
                        String toAdd = nextTerm.getText().toString();
                        allTerms.setText(toAdd);
                    } else {
                        String extracted = nextTerm.getText().toString();
                        String toAdd = allTerms.getText().toString() + "+" + extracted;
                        allTerms.setText(toAdd);
                    }
                    Intent intent = new Intent(getApplicationContext(), PracticalTest01SecondaryActivity.class);
                    intent.putExtra(Constants.ALL_TERMS, allTerms.getText().toString());
                    startActivityForResult(intent, 2);
                }
            } else if (view.getId() == R.id.compute_button) {
                Intent intent = new Intent(getApplicationContext(), PracticalTest01SecondaryActivity.class);
                intent.putExtra(Constants.ALL_TERMS, allTerms.getText().toString());
                startActivityForResult(intent, 2);
            }
            if (sumOfAll > Constants.SUM_OVER) {
                Intent intent = new Intent(getApplicationContext(), PracticalTest01Service.class);
                intent.putExtra(Constants.SUM, sumOfAll);
                getApplicationContext().startService(intent);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test01_main);

        nextTerm = (EditText)findViewById(R.id.next_term);
        allTerms = (TextView)findViewById(R.id.all_terms);

        addButton = (Button)findViewById(R.id.add_button);
        addButton.setOnClickListener(buttonClickListener);
        computeButton = (Button)findViewById(R.id.compute_button);
        computeButton.setOnClickListener(buttonClickListener);

        intentFilter.addAction(Constants.actionTypes[0]);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == 2) {
            Toast.makeText(this, "The activity returned with sum result " + resultCode, Toast.LENGTH_LONG).show();
            sumOfAll = resultCode;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString(Constants.RESULT, String.valueOf(sumOfAll));
        Log.d("I saved", String.valueOf(sumOfAll));
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState.containsKey(Constants.RESULT)) {
            Log.d("I restored", String.valueOf(sumOfAll));
            allTerms.setText(savedInstanceState.getString(Constants.RESULT));
        }
    }

    @Override
    protected void onDestroy() {
        Intent intent = new Intent(this, PracticalTest01Service.class);
        stopService(intent);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(messageBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        unregisterReceiver(messageBroadcastReceiver);
        super.onPause();
    }
}
