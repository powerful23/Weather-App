package com.csci571.weather;
import android.content.Context;
import android.content.Intent;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;


import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;


public class ResultActivity extends AppCompatActivity {

    private String city;
    private String state;
    private String hourly="";
    private String summary;
    private String temp;
    private String daily="";
    private String unit;

    private CallbackManager callbackManager;
    ShareDialog shareDialog;

    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_result);

        TextView txt1,txt_temp,txt_temp2,preciVal,
                chanceRainVal,windSpeedVal,dewPointVal,
                humidityVal,visibilityVal,sunriseVal,sunsetVal;
        String image;
        int img_id;
        ImageView weather_img;

        Intent intent = getIntent();

        txt1 = (TextView) findViewById(R.id.txt1);
        txt_temp = (TextView) findViewById(R.id.txt_temp);
        txt_temp2 = (TextView) findViewById(R.id.txt_temp2);
        preciVal = (TextView) findViewById(R.id.preciVal);
        chanceRainVal = (TextView) findViewById(R.id.chanceRainVal);
        windSpeedVal = (TextView) findViewById(R.id.windSpeedVal);
        dewPointVal = (TextView) findViewById(R.id.dewPointVal);
        humidityVal = (TextView) findViewById(R.id.humidityVal);
        visibilityVal = (TextView) findViewById(R.id.visibilityVal);
        sunriseVal = (TextView) findViewById(R.id.sunriseVal);
        sunsetVal = (TextView) findViewById(R.id.sunsetVal);
        weather_img = (ImageView) findViewById(R.id.weather_img);
        ImageButton loginButton = (ImageButton) findViewById(R.id.imageFb);
        Button viewMap = (Button) findViewById(R.id.btnMap);
        Button details = (Button) findViewById(R.id.btnDetails);

        city = intent.getStringExtra("city");
        unit = intent.getStringExtra("unit");

        String txt1_str = intent.getStringExtra("summary")+" in "+ city +", " +intent.getStringExtra("state");
        String txt_temp_str = intent.getStringExtra("temp")+unit;
        String txt_temp2_str = "L:"+intent.getStringExtra("current_mintemp")+unit+" | H:"+intent.getStringExtra("current_maxtemp")+unit;
        String dewPointVal_str = intent.getStringExtra("dewPoint")+unit;

        txt1.setText(txt1_str);
        txt_temp.setText(txt_temp_str);
        txt_temp2.setText(txt_temp2_str);

        preciVal.setText(intent.getStringExtra("preci"));
        chanceRainVal.setText(intent.getStringExtra("chanceRain"));
        windSpeedVal.setText(intent.getStringExtra("windSpeed"));
        dewPointVal.setText(dewPointVal_str);
        humidityVal.setText(intent.getStringExtra("humidity"));
        visibilityVal.setText(intent.getStringExtra("visibility"));
        sunriseVal.setText(intent.getStringExtra("sunrise"));
        sunsetVal.setText(intent.getStringExtra("sunset"));
        summary = intent.getStringExtra("summary");
        temp = intent.getStringExtra("temp");
        hourly = intent.getStringExtra("hourly");
        daily = intent.getStringExtra("daily");
        state = intent.getStringExtra("state");

        image = intent.getStringExtra("weather_img");
        image = image.substring(0, image.length() - 4);
        img_id = getResources().getIdentifier(image, "drawable", getPackageName());
        weather_img.setImageResource(img_id);

        //facebook
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);

        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent();
                intent2.putExtra("hourly",hourly);
                intent2.putExtra("daily",daily);
                intent2.putExtra("city",city);
                intent2.putExtra("state",state);
                intent2.putExtra("unit",unit);
                intent2.setClass(ResultActivity.this,DetailsActivity.class);  //从IntentActivity跳转到SubActivity
                startActivity(intent2);  //开始跳转
            }
        });

        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                //   finish();\
                Log.d("share", "succeed");
                context = getApplicationContext();
                Toast toast = Toast.makeText(context, "Facebook Post Successfully", Toast.LENGTH_LONG);
                toast.show();
            }

            @Override
            public void onCancel() {
                Log.d("share", "cancel");
                context = getApplicationContext();
                Toast toast = Toast.makeText(context, "Facebook Post Cancelled", Toast.LENGTH_LONG);
                toast.show();
            }

            @Override
            public void onError(FacebookException exception) {
                System.out.println("onError");
            }
        });



        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                if (ShareDialog.canShow(ShareLinkContent.class)) {
                    ShareLinkContent linkContent = new ShareLinkContent.Builder()
                            .setContentTitle("Current Weather in "+city+", "+state)
                            .setContentDescription(summary+", "+temp+unit)
                            .setImageUrl(Uri.parse("http://cs-server.usc.edu:58950/images/" + intent.getStringExtra("weather_img")))
                            .setContentUrl(Uri.parse("http://forecast.io"))
                            .build();

                    shareDialog.show(linkContent);
                }
            }
        });

        viewMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Intent intentLocal = getIntent();
                intent.putExtra("lat",intentLocal.getStringExtra("latitude"));
                intent.putExtra("lng",intentLocal.getStringExtra("longtitude"));
                intent.setClass(ResultActivity.this, MapActivity.class);  //从IntentActivity跳转到SubActivity
                startActivity(intent);  //开始跳转
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent data)
    {
        super.onActivityResult(requestCode, responseCode, data);
        callbackManager.onActivityResult(requestCode, responseCode, data);
    }
}

