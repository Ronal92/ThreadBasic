package com.jinwoo.android.threadbasic;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    //  핸들러 메시지에 담겨오는 what에 대한 정의
    public static final int SET_TEXT = 100;
    TextView txtResult;
    Button btnStart, btnStop;

    boolean flag = false;

    Thread thread;

    // 초
    int sec = 0;


    Handler handler = new Handler(){
        // 메세지를 받아서 처리하는 역할
        @Override
        public void handleMessage(Message msg) {
            // 메세지가 처리해야 하는 구분값(식별자).
            switch(msg.what){
                case SET_TEXT:
                    txtResult.setText(msg.arg1 + "");
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtResult = (TextView)findViewById(R.id.textResult);
        btnStart = (Button)findViewById(R.id.btnStart);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag){
                    Toast.makeText(MainActivity.this, "실행중입니다.",Toast.LENGTH_SHORT).show();
                }else {
                    flag = true;
                    thread = new CustomThread();
                    thread.start();
                }
            }
        });

        btnStop = (Button)findViewById(R.id.btnStop);
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopProgram();
            }
        });


    }

    public void stopProgram(){
        flag = false;
    }

    class CustomThread extends Thread{
        @Override
        public void run(){
            // thread 안에서 무한반복할때는
            // thread 를 중단시킬 수 있는 키값을 꼭 세팅해서
            // 메인 thread가 종료시에 같이 종료될 수 있도록 해야한다.
            // 왜!! : 경우에 따라 interrupt로 thread가 종료되지 않을 수 있기 때문에.....
            while(flag) {
                // 1. 메세지와 데이터를 보낼 때
                // 초 정보가 담긴 메세지 생성.
                Message msg = new Message();
                msg.what = SET_TEXT;
                msg.arg1 = sec;

                // 메세지를 넘겨준다.
                handler.sendMessage(msg);
                sec++;

                // 2. 메세지만 보낼 때
                // sendEmptyMessage에는 what에 해당하는 동작명령 상수를 넘겨준다.
                // handler.sendEmptyMessage(SET_TEXT);
                try{
                    Thread.sleep(1000);
                }catch(Exception e){
                    e.printStackTrace();
                }
             }
        }
    }

    @Override
    protected void onDestroy() {
        flag = false;
        thread.interrupt();
        super.onDestroy();
    }
}
