package com.android.deport.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.android.deport.R;
import com.android.deport.utils.DateUtil;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DatePickerActivity extends AppCompatActivity {

    private static final String TAG = "DatePickerActivity";


    @BindView(R.id.start_date)
    Button mBtnDatePicker;
    @BindView(R.id.tv_date)
    TextView mTvDate;
    @BindView(R.id.end_date)
    Button endDatePicker;
    @BindView(R.id.text_end_date)
    TextView textEndDate;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_picker);
        ButterKnife.bind(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Calendar calendar = Calendar.getInstance();
        mTvDate.setText(DateUtil.getStringDate(calendar.getTime(),"yyyy-MM-dd HH:mm:ss"));
        textEndDate.setText(DateUtil.getStringDate(calendar.getTime(),"yyyy-MM-dd HH:mm:ss"));
    }

    @OnClick(R.id.start_date)
    public void onViewClicked() {
        final Calendar calendar = Calendar.getInstance();
        mTvDate.setText(DateUtil.getStringDate(calendar.getTime(),"yyyy-MM-dd HH:mm:ss"));
        DatePickerDialog dialog = new DatePickerDialog(DatePickerActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Log.d(TAG, "onDateSet: year: " + year + ", month: " + month + ", dayOfMonth: " + dayOfMonth);
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        mTvDate.setText(DateUtil.getStringDate(calendar.getTime(),"yyyy-MM-dd HH:mm:ss"));
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    @OnClick(R.id.end_date)
    public void onClickedPicker() {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(DatePickerActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Log.d(TAG, "onDateSet: year: " + year + ", month: " + month + ", dayOfMonth: " + dayOfMonth);
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        textEndDate.setText(DateUtil.getStringDate(calendar.getTime(),"yyyy-MM-dd HH:mm:ss"));
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

}
