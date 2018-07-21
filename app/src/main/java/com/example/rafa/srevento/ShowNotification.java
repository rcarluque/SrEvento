package com.example.rafa.srevento;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ShowNotification extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_notification);

        String titulo = getIntent().getStringExtra("NOT_TITLE");
        String body = getIntent().getStringExtra("NOT_BODY");

        TextView tv_titulo = (TextView) findViewById(R.id.tv_titulo);
        TextView tv_body = (TextView) findViewById(R.id.tv_body);

        tv_titulo.setText(titulo);
        tv_body.setText(body);

    }
}
