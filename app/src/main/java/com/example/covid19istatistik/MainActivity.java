package com.example.covid19istatistik;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    TextView vaka,ölüm,iyilesen,aktif;
    ScrollView scrollView;
    PieChart pieChart;
    Button veribtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        veribtn=findViewById(R.id.veribtn);
        pieChart=findViewById(R.id.graph);
        aktif=findViewById(R.id.aktif);
        vaka=findViewById(R.id.vaka);
        ölüm=findViewById(R.id.ölüm);
        iyilesen=findViewById(R.id.iyilesen);
        scrollView=findViewById(R.id.scrolview);



        veribtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pieChart.setVisibility(View.VISIBLE);
                pieChart.clearChart();
                Timer timer=new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        verileriAl();
                    }
                },500);
            }
        });
    }

    private void verileriAl(){
        String url="https://disease.sh/v3/covid-19/all";
        StringRequest request=new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response.toString());
                            vaka.setText(jsonObject.getString("cases"));
                           ölüm.setText(jsonObject.getString("deaths"));
                            aktif.setText(jsonObject.getString("active"));
                            iyilesen.setText(jsonObject.getString("recovered"));


                            pieChart.addPieSlice(new PieModel("Vakalar",Float.parseFloat(vaka.getText().toString()),
                                    Color.parseColor("#CDA67F")));
                            pieChart.addPieSlice(new PieModel("Ölüler",Float.parseFloat(ölüm.getText().toString()),
                                    Color.parseColor("#56B7F1")));
                            pieChart.addPieSlice(new PieModel("Aktif",Float.parseFloat(aktif.getText().toString()),
                                    Color.parseColor("#FED70E")));
                            pieChart.addPieSlice(new PieModel("İyilesen",Float.parseFloat(iyilesen.getText().toString()),
                                    Color.parseColor("#FE6DA8")));

                            scrollView.setVisibility(View.VISIBLE);

                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                            scrollView.setVisibility(View.VISIBLE);
                        }
                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(request);
    }
}