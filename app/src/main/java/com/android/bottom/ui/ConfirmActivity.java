package com.android.bottom.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.bottom.R;

import java.util.ArrayList;
import java.util.Map;

public class ConfirmActivity extends AppCompatActivity {


    private TextView verifiedCount;
    private TextView needCount;
    public Button button;
    public Button buttonReturn;
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
        buttonReturn = (Button)findViewById(R.id.button_return);
        editText = (EditText)findViewById(R.id.editText);
        verifiedCount.setText(index+"");
        ArrayList<Parcelable> arrayList = intent.getParcelableArrayListExtra("list");
        ArrayList<Map> maps = (ArrayList<Map>) arrayList.get(0);
        Map map = maps.get(0);
        final String count = (String) map.get("count");
        final String name = (String)map.get("name");
        needCount.setText(count);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name.equals(editText.getText().toString())) {
                    verifiedCount.setText((index+=1)+"");
                } else {
                    Toast.makeText(ConfirmActivity.this,"请输入当前需要核实的产品编号",Toast.LENGTH_SHORT).show();
                }
                if (Integer.valueOf(verifiedCount.getText().toString()) == Integer.valueOf(count)) {
                    Toast.makeText(ConfirmActivity.this, "已全部核实", Toast.LENGTH_SHORT).show();
                    button.setEnabled(false);
                    buttonReturn.setVisibility(View.VISIBLE);
                }
            }
        });
        buttonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
