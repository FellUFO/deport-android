package com.android.deport.ui.notifications;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.android.deport.R;
import com.android.deport.ui.MainActivity;
import com.android.deport.utils.DateUtil;
import com.android.deport.utils.ToastUtil;
import com.android.deport.viewmodel.NotificationsViewModel;

import java.util.Date;

public class NotificationsFragment extends Fragment{

    private Switch aSwitch;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel = ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        aSwitch = (Switch)root.findViewById(R.id.enable);
        // 从SharedPreferences获取数据:
        SharedPreferences prefs = getActivity().getSharedPreferences("user", getActivity().MODE_PRIVATE);
        boolean switchState = prefs.getBoolean("flag", false);
        aSwitch.setChecked(switchState);
        switchState();
        return root;
    }

    /**
     * 根据switch开关状态的变化来做出相应的操作
     */
    private void switchState() {
        try {
            aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, final boolean b) {
                    if (b) {
                        Toast.makeText(getActivity(),"离线模式已开启！",Toast.LENGTH_SHORT).show();
                        MainActivity.isInternet = false;
                        Log.d("isInternet",MainActivity.isInternet+"");
                        //将数据保存至SharedPreferences:
                        SharedPreferences preferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean("flag", true);
                        editor.putString("currentDate", DateUtil.getStringDate(new Date()));
                        editor.apply();
                    } else {
                        MainActivity.isInternet = true;
                        Log.d("isInternet",MainActivity.isInternet+"");
                        //将数据保存至SharedPreferences:
                        SharedPreferences preferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean("flag", false);
                        editor.apply();
                        ToastUtil.getShortToast(R.string.tip,getContext());
                    }
                }
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }



}