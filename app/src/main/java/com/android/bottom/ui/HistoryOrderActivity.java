package com.android.bottom.ui;

import android.os.Bundle;

import com.android.bottom.R;
import com.android.bottom.data.entity.DocumentMaster;
import com.android.bottom.data.entity.DocumentSlave;
import com.android.bottom.data.entity.Unit;
import com.android.bottom.data.adapters.SectionsPagerAdapter;
import com.android.bottom.ui.historyorder.OfflineFragment;
import com.android.bottom.ui.historyorder.OnLineFragment;
import com.android.bottom.utils.DateUtil;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayout.ViewPagerOnTabSelectedListener;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;



public class HistoryOrderActivity extends AppCompatActivity {


    private final String TAG = "HistoryOrderActivity";
    private final String[] TITLES = {"在线单据","离线单据"};
    private ViewPager mViewPager;
    private List<Fragment> fragments = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_order);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this,getSupportFragmentManager(),fragments);
        mViewPager = findViewById(R.id.view_pager);
        fragments.add(new OnLineFragment());
        fragments.add(new OfflineFragment());
        mViewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        //在viewpager和适配等初始化以及适配完毕了以后,调用下面的方法将Tablayout与ViewPager绑定起来.
        tabs.setupWithViewPager(mViewPager);
        List<Unit<DocumentMaster, DocumentSlave>> documents = new ArrayList<>();
        tabs.addOnTabSelectedListener(new ViewPagerOnTabSelectedListener(mViewPager) {
            @Override
            public void onTabSelected(@NonNull TabLayout.Tab tab) {
                super.onTabSelected(tab);
                String title = tab.getText().toString();
                Log.d(TAG,title);
                if (TITLES[0].equals(title)) {
                    Log.d(TAG,"0");
                } else if (TITLES[1].equals(title)) {
                    Log.d(TAG, "1");
                }
            }
        });
    }
}