package com.csci571.weather;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Zhuo on 2015/12/5.
 */
public class DetailsActivity extends AppCompatActivity{

    private TableRow[] rows;
    private Button btn1;
    private Button btn2;
    private TableLayout tableLayout;
    private LinearLayout tableLayout2;
    private LinearLayout subLayout;
    private int hours;
    private String unit;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            hours = 24;

            drawMainLayout();

            btn1.setBackgroundColor(getResources().getColor(R.color.blue));
            btn2.setBackgroundColor(getResources().getColor(R.color.gray));

            tableLayout2.setVisibility(TableLayout.GONE);
            btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    tableLayout.setVisibility(TableLayout.VISIBLE);
                    tableLayout2.setVisibility(TableLayout.GONE);

                    for ( int i = hours; i < hours*2; i++) rows[i].setVisibility(TableLayout.GONE);
                    rows[hours*2].setVisibility(TableLayout.VISIBLE);

                    btn1.setBackgroundColor(getResources().getColor(R.color.blue));
                    btn2.setBackgroundColor(getResources().getColor(R.color.gray));
                }

            });


            btn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tableLayout.setVisibility(TableLayout.GONE);
                    tableLayout2.setVisibility(TableLayout.VISIBLE);
                    btn2.setBackgroundColor(getResources().getColor(R.color.blue));
                    btn1.setBackgroundColor(getResources().getColor(R.color.gray));

                }

            });

        }

    private void drawMainLayout(){
        Intent intent = getIntent();
        String city = intent.getStringExtra("city");
        String state = intent.getStringExtra("state");
        String tv1_str = "More Details for "+city+", "+state;
        String btn1_str = "Next 24 Hours";
        unit = intent.getStringExtra("unit");

        LinearLayout rootLayout = new LinearLayout(this);
        rootLayout.setOrientation(LinearLayout.VERTICAL);
        LayoutParams rootLayoutParam = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        setContentView(rootLayout, rootLayoutParam);

            //scroll
            ScrollView scrollView = new ScrollView(this);
            LayoutParams scrollViewParam = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            scrollView.setLayoutParams(scrollViewParam);
            rootLayout.addView(scrollView);


                //linearlayout
                subLayout = new LinearLayout(this);
                subLayout.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams subLayoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                subLayout.setLayoutParams(subLayoutParams);
                scrollView.addView(subLayout);

                    //textView
                    LinearLayout.LayoutParams textViewParam = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                    textViewParam.setMargins(30,30,30,30);
                    TextView tv1 = new TextView(this);
                    tv1.setTypeface(null, Typeface.BOLD);
                    tv1.setText(tv1_str);
                    tv1.setTextSize(30);
                    tv1.setLayoutParams(textViewParam);
                    subLayout.addView(tv1);

                    //buttonLinear
                    LinearLayout buttonLayout = new LinearLayout(this);
                    buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                    buttonLayoutParams.setMargins(30,0,30,30);
                    buttonLayout.setLayoutParams(buttonLayoutParams);
                    subLayout.addView(buttonLayout);

                        //button1
                        btn1 = new Button(this);
                        LinearLayout.LayoutParams btnLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                        btn1.setText(btn1_str);
                        btn1.setLayoutParams(btnLayoutParams);
                        //   btn1.setBackground(getResources().getDrawable(R.drawable.roundedbutton));
                        buttonLayout.addView(btn1);

                        //button2
                        btn2 = new Button(this);
                        btn2.setText("Next 7 Days");
                        btn2.setLayoutParams(btnLayoutParams);
                        buttonLayout.addView(btn2);

                    createTableOne(this);
                    //table2
                    createTableTwo();
    }


    private void createTableTwo(){
        tableLayout2 = new LinearLayout(this);
        LinearLayout.LayoutParams tableLayoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        //tableLayout2.setStretchAllColumns(true);
        tableLayoutParams.setMargins(30, 0, 30, 0);
        tableLayout2.setLayoutParams(tableLayoutParams);
        tableLayout2.setOrientation(LinearLayout.VERTICAL);
        subLayout.addView(tableLayout2);


        TextView[] info1 = new TextView[7];
        TextView[] info2 = new TextView[7];
        //horizental
        LinearLayout[] rows2 = new LinearLayout[7];
        //vertical
        LinearLayout[] regions = new LinearLayout[7];
        LinearLayout[] regions2 = new LinearLayout[7];

        ImageView[] images = new ImageView[7];
        int[] colors = {getResources().getColor(R.color.red),getResources().getColor(R.color.green),getResources().getColor(R.color.pink),
                getResources().getColor(R.color.orange),getResources().getColor(R.color.brown),
                getResources().getColor(R.color.purple),getResources().getColor(R.color.blue)};

        LinearLayout.LayoutParams rowsParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        rowsParams.bottomMargin = 10;

        LinearLayout.LayoutParams regionsParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams regionsParams2 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(80,80);
        imageParams.gravity = Gravity.END;

        LinearLayout.LayoutParams infoOneParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        infoOneParams.bottomMargin=25;

        Intent intent = getIntent();
        String daily = intent.getStringExtra("daily");

        try {
            JSONArray dailyJsonArray = new JSONArray(daily);
            String info1_str;
            String info2_str;

            for (int i = 0; i < 7; i++) {

                info1_str = dailyJsonArray.getJSONObject(i).getString("day") + ", " + dailyJsonArray.getJSONObject(i).getString("date");
                info2_str = "Min: " + dailyJsonArray.getJSONObject(i).getString("mintemp") + unit+" | Max: " + dailyJsonArray.getJSONObject(i).getString("maxtemp")+unit;
                rows2[i] = new LinearLayout(this);
                rows2[i].setLayoutParams(rowsParams);
                rows2[i].setBackgroundColor(colors[i]);
                rows2[i].setOrientation(LinearLayout.HORIZONTAL);
                tableLayout2.addView(rows2[i]);

                regions[i] = new LinearLayout(this);
                regions[i].setLayoutParams(regionsParams);
                regions[i].setOrientation(LinearLayout.VERTICAL);
                rows2[i].addView(regions[i]);

                regions2[i] = new LinearLayout(this);
                regions2[i].setLayoutParams(regionsParams2);
                regions2[i].setOrientation(LinearLayout.VERTICAL);
                rows2[i].addView(regions2[i]);

                info1[i] = new TextView(this);
                info1[i].setText(info1_str);
                info1[i].setTextSize(25);
                info1[i].setLayoutParams(infoOneParams);
                regions[i].addView(info1[i]);

                info2[i] = new TextView(this);
                info2[i].setText(info2_str);
                info2[i].setTextSize(25);
                info2[i].setLayoutParams(infoOneParams);
                regions[i].addView(info2[i]);

                images[i] = new ImageView(this);
                String image = dailyJsonArray.getJSONObject(i).getString("weather_img");
                image = image.substring(0, image.length() - 4);
                int img_id = getResources().getIdentifier(image, "drawable", getPackageName());
                images[i].setImageResource(img_id);

                images[i].setLayoutParams(imageParams);
                regions2[i].addView(images[i]);
            }
        }
        catch(JSONException e ) {
            Log.e("jsonerror","error");
        }
    }


    private void createTableOne(Context context){
        String th1_str = "Time";
        String th2_str = "Summary";
        String th3_str = "Temp("+unit+")";

        //table1
        tableLayout = new TableLayout(context);
        TableLayout.LayoutParams tableLayoutParams = new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        tableLayout.setStretchAllColumns(true);
        tableLayoutParams.setMargins(30,0,30,0);
        tableLayout.setLayoutParams(tableLayoutParams);
        subLayout.addView(tableLayout);

        //table head
        final TableRow tableHead = new TableRow(this);
        tableLayout.addView(tableHead);

        TableRow.LayoutParams tableEleParams = new TableRow.LayoutParams(0, LayoutParams.WRAP_CONTENT);

        tableEleParams.weight = 1;
        TableRow.LayoutParams tableEleParams2 = new TableRow.LayoutParams(0, LayoutParams.WRAP_CONTENT);

        tableEleParams2.weight = 1;
        TableRow.LayoutParams tableEleParams3 = new TableRow.LayoutParams(0, LayoutParams.WRAP_CONTENT);

        tableEleParams3.weight = 1;
        //table head element
        TextView th1 = new TextView(this);
        th1.setText(th1_str);
        th1.setTextSize(20);
        th1.setLayoutParams(tableEleParams);
        th1.setGravity(Gravity.START);
        tableHead.addView(th1);
        //table head element
        TextView th2 = new TextView(this);
        th2.setText(th2_str);
        th2.setTextSize(20);
        th2.setGravity(Gravity.CENTER);
        th2.setLayoutParams(tableEleParams2);
        tableHead.addView(th2);
        //table head element
        TextView th3 = new TextView(this);
        th3.setText(th3_str);
        th3.setTextSize(20);
        th3.setGravity(Gravity.END);
        th3.setLayoutParams(tableEleParams3);
        tableHead.addView(th3);

        //table rows
        TextView[] info = new TextView[hours*4];
        rows = new TableRow[hours*2+1];
        ImageView[] images = new ImageView[hours*2];
        TableRow.LayoutParams tableEleParams4 = new TableRow.LayoutParams(0, 40);
        Intent intent = getIntent();
        String hourly = intent.getStringExtra("hourly");
        try {
            JSONArray hourlyJsonArray = new JSONArray(hourly);
            for (int i = 0; i < hours*2; i++) {
                rows[i] = new TableRow(this);

                if (i % 2 == 0)
                    rows[i].setBackgroundColor(getResources().getColor(R.color.gray));
                if ( i >= hours) rows[i].setVisibility(TableLayout.GONE);
                tableLayout.addView(rows[i]);

                info[i] = new TextView(this);
                info[i].setText(hourlyJsonArray.getJSONObject(i).getString("time"));
                info[i].setTextSize(22);
                info[i].setGravity(Gravity.START);
                info[i].setLayoutParams(tableEleParams);
                rows[i].addView(info[i]);

                images[i] = new ImageView(this);
                String image = hourlyJsonArray.getJSONObject(i).getString("weather_img");
                image = image.substring(0, image.length() - 4);
                int img_id = getResources().getIdentifier(image, "drawable", getPackageName());
                images[i].setImageResource(img_id);
                images[i].setLayoutParams(tableEleParams4);
                rows[i].addView(images[i]);

                info[i + 48] = new TextView(this);
                info[i + 48].setText(hourlyJsonArray.getJSONObject(i).getString("temp"));
                info[i + 48].setTextSize(22);
                info[i + 48].setGravity(Gravity.END);
                info[i + 48].setLayoutParams(tableEleParams3);
                rows[i].addView(info[i + 48]);
            }

            rows[48] = new TableRow(this);
            //         rows[i].setLayoutParams(tableRowParams);
            rows[48].setBackgroundColor(getResources().getColor(R.color.gray));
            tableLayout.addView(rows[48]);

            TextView tmp = new TextView(this);
            tmp.setText("");
            tmp.setTextSize(22);
            tmp.setLayoutParams(tableEleParams);
            rows[48].addView(tmp);

            ImageButton imagebtn = new ImageButton(this);

            int img_id = getResources().getIdentifier("plus", "drawable", getPackageName());
            imagebtn.setImageResource(img_id);
            imagebtn.setLayoutParams(tableEleParams2);
            rows[48].addView(imagebtn);

            TextView tmp2 = new TextView(this);
            tmp2.setText("");
            tmp2.setTextSize(22);
            tmp2.setLayoutParams(tableEleParams3);
            rows[48].addView(tmp2);

            imagebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for ( int i = 24; i < 48; i++) rows[i].setVisibility(TableLayout.VISIBLE);
                    rows[48].setVisibility(TableLayout.GONE);

                }
            });
        }
        catch(JSONException e )
        {
            Log.e("jsonerror", "detailsError");
        }
    }



}
