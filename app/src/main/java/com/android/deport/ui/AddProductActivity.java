package com.android.deport.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.deport.R;
import com.android.deport.data.entity.ProductMessage;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddProductActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener ,View.OnClickListener{

    private ArrayAdapter<String> arr_adapter ;
    private EditText proName;
    private EditText category;
    private EditText note;
    private EditText count;
    private int index;
    private ProductMessage productMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        //数据
        Intent intent = getIntent();
        String responseBody = intent.getStringExtra("responseBody");
        Gson gson = new Gson();
        List data_list = gson.fromJson(responseBody, List.class);
        //适配器
        arr_adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data_list);
        //设置样式
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        spinner.setAdapter(arr_adapter);
        spinner.setOnItemSelectedListener(this);
        Button submitPro = (Button) findViewById(R.id.submit_pro);
        proName = (EditText)findViewById(R.id.pro_name);
        note = (EditText)findViewById(R.id.note);
        category = (EditText)findViewById(R.id.category);
        count = (EditText)findViewById(R.id.expected_count);
        submitPro.setOnClickListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String name =  arr_adapter.getItem(i);
        if (name != null) {
            index = Integer.parseInt(name.substring(0,1));
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    //添加商品
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.submit_pro) {
            if ("".equals(proName.getText().toString()) || "".equals(category.getText().toString()) ||
                    "".equals(note.getText().toString()) || "".equals(count.getText().toString())) {
                Toast.makeText(this, "请正确填写", Toast.LENGTH_SHORT).show();
            } else {
                productMessage = new ProductMessage();
                //组装对象
                {
                    productMessage.setWarehouseId(index);
                    productMessage.setCount(Integer.valueOf(count.getText().toString()));
                    productMessage.setMessage(note.getText().toString());
                    productMessage.setProductName(proName.getText().toString());
                    productMessage.setCategory(category.getText().toString());
                }
                new Thread(() -> {
                    Gson gson = new Gson();
                    String json = gson.toJson(productMessage);
                    OkHttpClient client = new OkHttpClient();
                    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                    final RequestBody requestBody = RequestBody.create(JSON, json);
                    Log.i("data", json);
                    String url = "http://192.168.0.116:8080/addProduct";
                    Request request = new Request.Builder()
                            .url(url)
                            .post(requestBody)
                            .build();
                    Call call = client.newCall(request);
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            Log.i("reponse", response.body().string());
                            finish();
                        }
                    });
                }).start();
            }
        }
    }
}