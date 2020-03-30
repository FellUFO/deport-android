package com.android.bottom.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.bottom.R;

public class ConfirmActivity extends AppCompatActivity {


    private TextView verifiedCount;
    private TextView needCount;
    public Button button;
    private EditText editText;
    private int index=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);
        final Intent intent = getIntent();
        verifiedCount = (TextView)findViewById(R.id.verified_count);
        needCount = (TextView)findViewById(R.id.need_count);
        button = (Button)findViewById(R.id.button);
        editText = (EditText)findViewById(R.id.editText);
        needCount.setText(intent.getStringExtra("count"));
        verifiedCount.setText(index+"");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText.getText().toString() == intent.getStringExtra("name")) {
                    verifiedCount.setText((index+=1)+"");
                }
                if (Integer.valueOf(verifiedCount.getText().toString()) == Integer.valueOf(intent.getStringExtra("count"))) {
                    Toast.makeText(ConfirmActivity.this, "已全部核实", Toast.LENGTH_SHORT).show();
                    button.setEnabled(false);
                }
            }
        });
    }
}
