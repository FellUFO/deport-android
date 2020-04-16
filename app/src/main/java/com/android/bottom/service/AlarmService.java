package com.android.bottom.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.android.bottom.data.DeportRoomDatabase;
import com.android.bottom.data.dao.ProductMessageDao;
import com.android.bottom.data.entity.ProductMessage;
import com.android.bottom.ui.MainActivity;
import com.android.bottom.utils.DateUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AlarmService extends Service {

    private int index = 0;
    private List<ProductMessage> products = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        startForeground(1,new Notification());
    }

    /**
     * 更新时间
     */
    private static final int INTERVALS = 60*1000*60*10; //十小时更新一次
    private static final int PENDING_REQUEST = 0;

    public AlarmService() {
    }

    /**
     * 调用service都会执行该方法
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        index++;
        //这里进行任务处理
        Log.i("working","第"+index+"次任务处理中");
        //从后台获取数据写入本地数据库
        if (MainActivity.isInternet) {
            getData();  //获取商品数据
        }
        //获取库位信息

        //通过AlarmManager定时启动广播
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        long triggerAtTime = SystemClock.elapsedRealtime()+INTERVALS;
        Intent i = new Intent(this,AlarmReceive.class);
        PendingIntent pIntent = PendingIntent.getBroadcast(this, PENDING_REQUEST, i, PENDING_REQUEST);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pIntent);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * 获取后台商品数据并存入本地数据库
     */
    public void getData(){
        //获取所有商品
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.i("message","创建线程");
                    String url = "http://192.168.0.116:8080/getAllProduct";
                    OkHttpClient client = new OkHttpClient();
                    final Request request = new Request.Builder().url(url).build();
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String data = response.body().string();
                        ParsingJSONData(data,products);
                        //将数据存进本地数据库
                        DeportRoomDatabase dataBase = DeportRoomDatabase.getDataBase(getApplicationContext());
                        ProductMessageDao pmDao = dataBase.productMessageDao();
                        LiveData<List<ProductMessage>> listLiveData = pmDao.selectAllProduct();
                        List<ProductMessage> productMessages = listLiveData.getValue();
                        if (productMessages == null || productMessages.isEmpty()) {
                            List<Long> longs = pmDao.insertProducts(products);
                        } else {
                            //先清空，在插入
                            pmDao.deleteAll();
                            int index = pmDao.updateAll(products);
                        }
                    } else {
                        Log.i("message", "没有成功");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 解析响应数据
     * @param data
     * @param productMessages
     */
    public void  ParsingJSONData(String data, List<ProductMessage> productMessages) {
        JSONObject jsonData = null;
        try {
            jsonData = new JSONObject(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONArray jsonArray = jsonData.optJSONArray("data");
        for (int i =0; i<jsonArray.length(); i++) {
            ProductMessage productMessage = new ProductMessage();
            JSONObject jsonObject = jsonArray.optJSONObject(i);
            productMessage.setProductId(jsonObject.optString("productId"));
            productMessage.setProductName(jsonObject.optString("productName"));
            productMessage.setAddTime(DateUtil.getDate(jsonObject.optString("addTime")));
            productMessage.setUpdateTime(DateUtil.getDate(jsonObject.optString("updateTime")));
            productMessage.setCategory(jsonObject.optString("category"));
            productMessage.setMessage(jsonObject.optString("message"));
            productMessage.setCount(jsonObject.optInt("count"));
            productMessage.setWarehouseId(jsonObject.optInt("warehouseId"));
            productMessages.add(productMessage);
        }
    }
}
