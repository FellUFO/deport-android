package com.android.bottom.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.bottom.R;
import com.android.bottom.ui.order.DocumentFragment;
import com.google.android.material.textfield.TextInputEditText;

public class OrderActivity extends AppCompatActivity implements View.OnClickListener {

    private ConstraintLayout constraintLayout;
    private EditText inputCount;
    private EditText inputNumber;
    private EditText inputPersonnel;
    private EditText inputDeportNum;
    private Button submit;
    private Button scanCode;
    final String[] items = new String[]{"继续添加","完成添加"};
    private int index;
    private String object;
    private int count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        Intent intent = getIntent();
        object = intent.getStringExtra("object");
        inputCount = (EditText)findViewById(R.id.input_count);
        inputNumber = (EditText)findViewById(R.id.input_number);
        inputPersonnel = (EditText)findViewById(R.id.input_personnel);
        inputDeportNum = (EditText)findViewById(R.id.input_deportNum);
        submit = (Button)findViewById(R.id.submit);
        scanCode = (Button)findViewById(R.id.scan_code);
        submit.setOnClickListener(this);
        scanCode.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.submit:
//                先获取各个输入框的文本数据
                if (inputCount.getText() == null ) {
                    Toast.makeText(this,"文本有误，请重新输入！", Toast.LENGTH_SHORT).show();
                    refresh();
                }
                count = Integer.parseInt(inputCount.getText().toString());
                String number = inputNumber.getText().toString();
                String personnel = inputPersonnel.getText().toString();
                String deportNum = inputDeportNum.getText().toString();

                getAlertDialog();
                break;
            case R.id.scan_code:
                Toast.makeText(this,"正在打开扫码界面", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this,ConfirmActivity.class);
                intent.putExtra("count",inputCount.getText().toString());
                intent.putExtra("name",inputNumber.getText().toString());
                startActivity(intent);
                break;
        }
    }

    public void getAlertDialog(){
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("请选择你的操作")
                .setIcon(R.mipmap.ic_launcher)
                .setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        index = i;
                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加"Yes"按钮
                    @SuppressLint("ResourceType")
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (index){
                            case 0:
//                                        Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
//                                        startActivityForResult(intent, REQUEST_CODE);
                                Toast.makeText(OrderActivity.this, inputCount.getText().toString(), Toast.LENGTH_SHORT).show();

                                break;
                            case 1:
                                Toast.makeText(OrderActivity.this, "正在载入...", Toast.LENGTH_SHORT).show();
                                break;
                        }
//                        Toast.makeText(getActivity(), "这是确定按钮" + "点的是：" + items[index], Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加取消
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(OrderActivity.this, "这是取消按钮", Toast.LENGTH_SHORT).show();
                    }
                }).create();
        alertDialog.show();
    }


    public void refresh() {
        inputCount.setText("");
        inputNumber.setText("");
        inputPersonnel.setText("");
        inputDeportNum.setText("");
    }
}
