package com.android.deport.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.deport.R;
import com.android.deport.data.DeportRoomDatabase;
import com.android.deport.data.dao.DocumentMasterDao;
import com.android.deport.data.dao.DocumentSlaveDao;
import com.android.deport.data.dao.ProductMessageDao;
import com.android.deport.data.entity.DocumentMaster;
import com.android.deport.data.entity.DocumentSlave;
import com.android.deport.data.entity.MasterAndSlave;
import com.android.deport.utils.OrderIDUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OrderActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * 创建所需变量
     */
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
    private DocumentMaster documentMaster = new DocumentMaster();
    private List<DocumentSlave> documentSlaves = new ArrayList<>();
    private String result;
    final String orderNum = OrderIDUtil.getOrderNum();
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
        setUpHintTextSize(inputNumber,"请输入商品编号");
        setUpHintTextSize(inputPersonnel,"请输入员工编号");
        setUpHintTextSize(inputDeportNum,"请输入仓库编号");
        setUpHintTextSize(inputCount,"请输入数量");
    }

    /**
     * 设置输入框提示字体的大小
     * @param editText
     * @param hintText
     */
    public void setUpHintTextSize(EditText editText,String hintText){
        SpannableString s = new SpannableString(hintText);
        AbsoluteSizeSpan textSize = new AbsoluteSizeSpan(14,true);
        s.setSpan(textSize, 0 ,s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        editText.setHint(s);
    }

    /**
     * 点击事件
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.submit:
                // 先获取各个输入框的文本数据
                if ("".equals(inputCount.getText().toString())|| "".equals(inputNumber.getText().toString())||"".equals(inputPersonnel.getText().toString()) || "".equals(inputDeportNum.getText().toString())) {
                    Toast.makeText(this,"文本有误，请重新输入！", Toast.LENGTH_SHORT).show();
                } else {
                    DocumentSlave documentSlave = new DocumentSlave();
                    count = Integer.parseInt(inputCount.getText().toString());
                    String number = inputNumber.getText().toString();
                    Integer personnel = Integer.valueOf(inputPersonnel.getText().toString());
                    Integer deportNum =  Integer.valueOf(inputDeportNum.getText().toString());

                    documentMaster.setOrderId(orderNum);
                    //添加属性
                    documentSlave.setProductId(number);
                    documentSlave.setCount(count);
                    documentSlave.setMasterId(documentMaster.getOrderId());
                    //添加属性
                    documentMaster.setDeportId(deportNum);
                    documentMaster.setOperator(personnel);
                    documentMaster.setGenerate(new Date());
                    documentMaster.setObject(object);
                    //将对象添加进集合
                    documentSlaves.add(documentSlave);
                    //创建对话框
                    getAlertDialog(deportNum,personnel);
                }
                break;
            case R.id.scan_code:
                //创建吐司
                Toast.makeText(this,"正在打开扫码界面", Toast.LENGTH_SHORT).show();
                Map map = new HashMap();
                //绑定数据
                map.put("count",inputCount.getText().toString());
                map.put("name",inputNumber.getText().toString());
                List<Map> list = new ArrayList<>();
                list.add(map);
                Bundle bundle = new Bundle();
                ArrayList arrayList = new ArrayList();
                arrayList.add(list);
                bundle.putParcelableArrayList("list",arrayList);
                //使用intent跳转Activity
                Intent intent = new Intent(this,ConfirmActivity.class);
                //绑定数据
                intent.putExtras(bundle);
                //开始跳转
                startActivity(intent);
                break;
        }
    }

    /**
     * 创建对话框
     * @param deportNum
     * @param personnel
     */
    public void getAlertDialog(final Integer deportNum, final Integer personnel){
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
                                Toast.makeText(OrderActivity.this, inputCount.getText().toString(), Toast.LENGTH_SHORT).show();
                                refresh();
                                break;
                            case 1:
                                Toast.makeText(OrderActivity.this, "正在载入...", Toast.LENGTH_SHORT).show();
                                // 发送请求给服务器,开启子线程来执行
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        // 将数据存入后台数据库
                                        //根据是否有网络连接来判断是写入本地数据库还是传给后台
                                        Log.d("isInternet",MainActivity.isInternet+"");
                                        if (MainActivity.isInternet) {
                                            Log.d("isInternet",MainActivity.isInternet+"");
                                            MasterAndSlave masterAndSlave = new MasterAndSlave();
                                            masterAndSlave.setMaster(documentMaster);
                                            for (DocumentSlave documentSlave : documentSlaves) {
                                                Log.d("documentSlaves", documentSlave.getProductId());
                                            }
                                            masterAndSlave.setSlaves(documentSlaves);
                                            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                                            Gson gson = new GsonBuilder()
                                                    .setDateFormat("yyyy-MM-dd HH:mm:ss")
                                                    .create();
                                            String content = gson.toJson(masterAndSlave);
                                            Log.i("documentMasterJSON",content);
                                            RequestBody body = RequestBody.create(JSON, content);
                                            String url = "http://192.168.0.116:8080/createDocument";
                                            final OkHttpClient okHttpClient = new OkHttpClient();
                                            final Request request = new Request.Builder()
                                                    .url(url)
                                                    .post(body)
                                                    .build();
                                            try {
                                                Call call = okHttpClient.newCall(request);
                                                call.enqueue(new Callback() {
                                                    @Override
                                                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                                        if (e instanceof SocketTimeoutException) {
                                                            int index = 0;
                                                            // 重新提交验证   在这里最好限制提交次数
                                                            if (index <= 5) {
                                                                okHttpClient.newCall(call.request()).enqueue(this);
                                                                index++;
                                                            }
                                                            Log.e("SocketTimeoutException",e.getMessage());
                                                        }
                                                        if (e instanceof ConnectException) {
                                                            Log.e("frost_connection",e.getMessage());
                                                        }
                                                    }
                                                    @Override
                                                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                                        final String responseBody = Objects.requireNonNull(response.body()).string();
                                                        Log.i("response", responseBody);
                                                        Message message = new Message();
                                                        message.obj = responseBody;
                                                        handler.sendMessage(message);
                                                        Intent intent = new Intent(OrderActivity.this, MainActivity.class);
                                                        if (responseBody.contains("成功")) {
                                                            startActivity(intent);
                                                        } else {
                                                            //刷新页面
                                                            refresh();
                                                        }
                                                    }
                                                });
                                                //清空数据
                                                documentSlaves.clear();
                                                documentMaster = new DocumentMaster();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        } else {
                                            documentMaster.setState(0); //添加未上传标识
                                            Log.d("isInternet",MainActivity.isInternet+"");
                                            //写入本地数据库并对本地数据库里的商品数量进行增减
                                            DeportRoomDatabase db = DeportRoomDatabase.getDataBase(getApplicationContext());
                                            DocumentMasterDao dmDao = db.documentMasterDao();
                                            DocumentSlaveDao dsDao = db.documentSlaveDao();
                                            ProductMessageDao pmDao = db.productMessageDao();
                                            dmDao.insert(documentMaster);
                                            dsDao.insertListDocument(documentSlaves);
                                            switch (documentMaster.getObject()) {
                                                case "入库":
                                                    Log.d("object","入库");
                                                    for (DocumentSlave slave : documentSlaves) {
                                                        pmDao.addCountById(slave.getCount(),slave.getProductId());
                                                    }
                                                    break;
                                                case "出库":
                                                    Log.d("object","出库");
                                                    for (DocumentSlave slave : documentSlaves) {
                                                        pmDao.reduceCountById(slave.getCount(), slave.getProductId());
                                                    }
                                                    break;
                                            }
                                            onResume();
                                            Intent intent = new Intent(OrderActivity.this, MainActivity.class);
                                            startActivity(intent);
                                        }
                                    }
                                }).start();
                                break;
                        }
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加取消
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(OrderActivity.this, "这是取消按钮", Toast.LENGTH_SHORT).show();
                    }
                }).create();
        alertDialog.show();
    }

    /**
     * 清楚输入框内的数据
     */
    public void refresh() {
        inputCount.setText("");
        inputNumber.setText("");
        inputPersonnel.setText("");
        inputDeportNum.setText("");
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            result = (String) msg.obj;
            Toast.makeText(OrderActivity.this, result, Toast.LENGTH_SHORT).show();
        }
    };
}
