package com.example.alarm;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private TimePicker timePicker;
    private AlarmManager alarmManager;
    CheckBox cbSun, cbMon, cbTue, cbWed, cbThu, cbFri, cbSat;

    static String TAG="MainActivity";

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timePicker=findViewById(R.id.tp_timepicker);
        alarmManager= (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);

        cbSun=findViewById(R.id.cb_sun);
        cbMon=findViewById(R.id.cb_mon);
        cbTue=findViewById(R.id.cb_thu);
        cbWed=findViewById(R.id.cb_wed);
        cbThu=findViewById(R.id.cb_thu);
        cbFri=findViewById(R.id.cb_fri);
        cbSat=findViewById(R.id.cb_sat);

    }// onCreate()..

    public void regist(View view) {

        boolean[] week = { false, cbSun.isChecked(), cbMon.isChecked(), cbTue.isChecked(), cbWed.isChecked(),
                cbThu.isChecked(), cbFri.isChecked(), cbSat.isChecked() }; // cbSun을 1번부터 사용하기 위해 배열 0번은 false로 고정

        if(!cbSun.isChecked() &&  !cbMon.isChecked() &&  !cbTue.isChecked() && !cbWed.isChecked() &&  !cbThu.isChecked() && !cbFri.isChecked() && !cbSat.isChecked()){
            Toast.makeText(this, "요일을 선택해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        int hour;
        int minute;
        hour =timePicker.getHour();
        minute =timePicker.getMinute();

        Intent intent = new Intent(this, Alarm.class);
        intent.putExtra("weekday", week);
        PendingIntent pIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE); //PendingIntent.FLAG_UPDATE_CURRENT

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Date today = new Date();
        long intervalDay = 24 * 60 * 60 * 1000;// 24시간

        long selectTime=calendar.getTimeInMillis();
        long currenTime=System.currentTimeMillis();

        //만약 설정한 시간이, 현재 시간보다 작다면 알람이 부정확하게 울리기 때문에 다음날 울리게 설정
        if(currenTime>selectTime){
            selectTime += intervalDay;
        }

        Log.e(TAG,"등록 버튼을 누른 시간 : "+today+"  설정한 시간 : "+calendar.getTime());

        Log.d(TAG,"calendar.getTimeInMillis()  : "+calendar.getTimeInMillis());

        // 지정한 시간에 매일 알림
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, selectTime,  intervalDay, pIntent);

    }// regist()..

    public void unregist(View view) {
        Intent intent = new Intent(this, Alarm.class);
        PendingIntent pIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        alarmManager.cancel(pIntent);
    }// unregist()..

}// MainActivity class..
