package com.android.bottom.ui.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.android.bottom.MainActivity;
import com.android.bottom.R;

public class HomeFragment extends Fragment {

    final String[] items = new String[]{"扫码","手动输入"};
    private int index;
    private int REQUEST_CODE = 5;
    private HomeViewModel homeViewModel;
    private Button enter;
    private Button out;
    private Button historyOrder;
    private Button locationManagement;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        return root;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        enter = (Button)getActivity().findViewById(R.id.enter);
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAlertDialog();
            }
        });
        out = (Button)getActivity().findViewById(R.id.out);
        out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAlertDialog();
            }
        });
        historyOrder = (Button)getActivity().findViewById(R.id.history_order);
        historyOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
            }
        });

    }

    public void getAlertDialog(){
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle("请选择你的操作")
                .setIcon(R.mipmap.ic_launcher)
                .setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        index = i;
                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加"Yes"按钮
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (index){
                            case 0:
//                                        Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
//                                        startActivityForResult(intent, REQUEST_CODE);
                                Toast.makeText(getActivity(), "请手动输入入库商品", Toast.LENGTH_SHORT).show();
                                break;
                            case 1:
                                Toast.makeText(getActivity(), "请手动输入入库商品", Toast.LENGTH_SHORT).show();
                                break;
                        }
                        Toast.makeText(getActivity(), "这是确定按钮" + "点的是：" + items[index], Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加取消
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getActivity(), "这是取消按钮", Toast.LENGTH_SHORT).show();
                    }
                }).create();
        alertDialog.show();
    }
}
