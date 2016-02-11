package com.csci571.weather;

import android.content.Intent;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.hamweather.aeris.communication.AerisEngine;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private int stateId = 0;

    private Button btn1;
    private Button btnAbout;
    private Button btnClear;

    private EditText city;
    private EditText street;

    private Spinner state;

    private RadioGroup radiogroup;
    private RadioButton huashi;
    private RadioButton sheshi;

    private TextView errorText;

    private ImageView forecastIo;

    private final String[] states={"","AL","AK","AZ","AR","CA","CO","CT","DE","DC","FL","GA","HI",
            "ID","IL","IN","IA","KS","KY","LA","ME","MD","MA","MI","MN","MS","MO","MT","NE","NV","NH",
            "NJ","NM","NY","NC", "ND","OH","OK","OR","PA","RI","SC","SD","TN","TX","UT","VT","VA","WA","WV","WI","WY"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AerisEngine.initWithKeys(this.getString(R.string.aeris_client_id), this.getString(R.string.aeris_client_secret), this);

        //button
        btn1 = (Button)findViewById(R.id.btn1);
        btnAbout = (Button)findViewById(R.id.about);
        btnClear = (Button)findViewById(R.id.clear);

        //edittext
        street = (EditText)findViewById(R.id.streetText);
        city = (EditText)findViewById(R.id.cityText);
        errorText = (TextView)findViewById(R.id.error);

        //imageview
        forecastIo = (ImageView)findViewById(R.id.logo);

        //radiobutton
        huashi = (RadioButton)findViewById(R.id.huashi);
        sheshi = (RadioButton)findViewById(R.id.sheshi);
        huashi.toggle();
        radiogroup = (RadioGroup)findViewById(R.id.radiogroup);

        //spinner
        state = (Spinner)findViewById(R.id.state_spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.state_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        state.setAdapter(adapter);

        state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                stateId = position;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        forecastIo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("http://forecast.io/"));
                startActivity(intent);
            }
        });


        btnAbout.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick (View v){
                errorText.setText("");
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, AboutActivity.class);
                startActivity(intent);

        }
        });

        btnClear.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick (View v){
                street.setText("");
                city.setText("");
                state.setSelection(0);
                huashi.setChecked(true);
                sheshi.setChecked(false);
                errorText.setText("");
        }
        });

        btn1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View v){
                if (!validation()) return;
                String degree;
                String unit;

                if (radiogroup.getCheckedRadioButtonId() == R.id.huashi) {
                    degree = "Fahrenheit";
                    unit = "°F";
                } else {
                    degree = "Celsius";
                    unit = "°C";
                }
                String url = "http://csci571-env.us-west-2.elasticbeanstalk.com/index.php?addr=" + street.getText().toString() + "&city=" + city.getText().toString() + "&state=" + states[stateId] + "&degree=" + degree;
                startDownload(url,unit);
        }
        });
    }

    private boolean validation(){
        errorText.setText("");
        String error;
        if (street.getText().toString().equals("")){
            error = "please enter the street address";
            errorText.setText(error);
            return false;
        }
        else if ( city.getText().toString().equals("")){
            error = "please enter the city address";
            errorText.setText(error);
            return false;
        }
        else if (stateId == 0){
            error = "please select the state";
            errorText.setText(error);
            return false;
        }
        return true;
    }

    private void startDownload(String url, String unit){
        DownloadTask downloadTask = new DownloadTask();
        downloadTask.execute(url);

        String jsonStr;
        String cityStr;

        cityStr = city.getText().toString();

        try {
            jsonStr = downloadTask.get();
            JSONObject jObj = new JSONObject(jsonStr);
            Intent intent = new Intent();
            //one day
            intent.putExtra("city", cityStr);
            intent.putExtra("state", states[stateId]);
            intent.putExtra("latitude", jObj.getString("latitude"));
            intent.putExtra("longtitude", jObj.getString("longtitude"));
            intent.putExtra("summary", jObj.getString("summary"));
            intent.putExtra("temp", jObj.getString("temp"));
            intent.putExtra("unit", unit);
            intent.putExtra("weather_img", jObj.getString("weather_img"));
            intent.putExtra("preci", jObj.getString("preci"));
            intent.putExtra("chanceRain", jObj.getString("chanceRain"));
            intent.putExtra("windSpeed", jObj.getString("windSpeed"));
            intent.putExtra("dewPoint", jObj.getString("dewPoint"));
            intent.putExtra("humidity", jObj.getString("humidity"));
            intent.putExtra("visibility", jObj.getString("visibility"));
            intent.putExtra("sunrise", jObj.getString("sunrise"));
            intent.putExtra("sunset", jObj.getString("sunset"));
            intent.putExtra("message", jObj.getString("message"));
            intent.putExtra("hourly", jObj.getString("hourly"));
            intent.putExtra("daily", jObj.getString("daily"));
            intent.putExtra("current_mintemp", jObj.getString("current_mintemp"));
            intent.putExtra("current_maxtemp", jObj.getString("current_maxtemp"));

            intent.setClass(MainActivity.this, ResultActivity.class);
            startActivity(intent);
        } catch (InterruptedException e1) {
            Log.e("jsonError", "interrupted Exception");
        } catch (ExecutionException e2) {
            Log.e("jsonError", "execution Exception");
        } catch (JSONException e3) {
            Log.e("jsonError", "json Exception");
        }
    }
}

