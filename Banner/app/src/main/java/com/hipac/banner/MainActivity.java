package com.hipac.banner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnDefault = findViewById(R.id.btn_default);
        Button btnAccordion = findViewById(R.id.btn_Accordion);
        Button btnCubeIn = findViewById(R.id.btn_CubeIn);
        Button btnCubeOut = findViewById(R.id.btn_CubeOut);
        Button btnForegroundToBg = findViewById(R.id.btn_ForegroundToBackground);
        Button btnBgToForeground = findViewById(R.id.btn_BackgroundToForeground);
        btnDefault.setOnClickListener(this);
        btnAccordion.setOnClickListener(this);
        btnCubeIn.setOnClickListener(this);
        btnCubeOut.setOnClickListener(this);
        btnForegroundToBg.setOnClickListener(this);
        btnBgToForeground.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this,BannerActivity.class);
         switch (view.getId()){
             case R.id.btn_default:
                 intent.putExtra("transform","defaultT");
                 startActivity(intent);
                 break;
             case R.id.btn_Accordion:
                 intent.putExtra("transform","Accordion");
                 startActivity(intent);
                 break;
             case R.id.btn_CubeIn:
                 intent.putExtra("transform","CubeIn");
                 startActivity(intent);
                 break;
             case R.id.btn_CubeOut:
                 intent.putExtra("transform","CubeOut");
                 startActivity(intent);
                 break;
             case R.id.btn_ForegroundToBackground:
                 intent.putExtra("transform","ForegroundToBg");
                 startActivity(intent);
                 break;
             case R.id.btn_BackgroundToForeground:
//                 intent.putExtra("transform","BgToForeground");
//                 startActivity(intent);
                 startActivity(new Intent(this,PhotoActivity.class));
                 break;
         }
    }
}
