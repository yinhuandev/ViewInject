package com.yinhuan.viewinject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.yinhuan.viewinject.view.ViewInject;
import com.yinhuan.viewinject.view.annotation.BindView;
import com.yinhuan.viewinject.view.annotation.ContentView;
import com.yinhuan.viewinject.view.annotation.OnClick;

@ContentView(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.button1)
    Button button1;
    @BindView(R.id.button2)
    Button button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewInject.inject(this);
    }

    @OnClick({R.id.button1,R.id.button2})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.button1:
                Toast.makeText(MainActivity.this,"hello",Toast.LENGTH_SHORT).show();
                break;
            case R.id.button2:
                Toast.makeText(MainActivity.this,"world",Toast.LENGTH_SHORT).show();
                break;
        }
    }

}
