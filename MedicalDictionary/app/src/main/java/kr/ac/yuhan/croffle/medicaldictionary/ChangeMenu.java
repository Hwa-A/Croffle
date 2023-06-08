package kr.ac.yuhan.croffle.medicaldictionary;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChangeMenu extends AppCompatActivity {
    Button Ch_index_search;
    SharedPreferences sharedPreferences;
    String userID;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        sharedPreferences = getSharedPreferences("memberPreference",MODE_PRIVATE);
        userID = sharedPreferences.getString("user_id","");
        // 전달된 사용자 ID 값 받아오기
        Intent intent = getIntent();
        userID = intent.getStringExtra("user_id");



        //인덱스 검색 클릭시
        Ch_index_search = findViewById(R.id.ch_indexsearch);
        Ch_index_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),IndexSearch.class);
                startActivity(intent);

            }
        });

        //텍스트검색 버튼 클릭시
        Button Ch_text_search = (Button) findViewById(R.id.ch_textsearch);
        Ch_text_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),TextSearch.class);
                startActivity(intent);
            }
        });

        // 단어장 버튼 클릭시
        Button Ch_record = (Button) findViewById(R.id.ch_record);
        Ch_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),RecordView.class);
                startActivity(intent);
            }
        });
        
        //마이페이지 버튼 클릭시
        Button Ch_mypage = (Button) findViewById(R.id.ch_mypage);
        Ch_mypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), InfoActivity.class);
                intent.putExtra("user_id", userID); // 사용자 ID를 전달
                startActivity(intent);
                finish();
            }
        });
    }






}
