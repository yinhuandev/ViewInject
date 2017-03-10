package com.yinhuan.viewinject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.yinhuan.viewinject.view.ViewInject;
import com.yinhuan.viewinject.view.annotation.BindView;
import com.yinhuan.viewinject.view.annotation.ContentView;

@ContentView(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.button)
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewInject.inject(this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"hello",Toast.LENGTH_SHORT).show();
            }
        });

    }


}
