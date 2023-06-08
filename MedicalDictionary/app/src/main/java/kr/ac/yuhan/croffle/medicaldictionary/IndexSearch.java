package kr.ac.yuhan.croffle.medicaldictionary;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class IndexSearch extends AppCompatActivity {
    SearchAdapter indexAdapter;
    private Button engIndexBtn, korIndexBtn;
    String selectedIndex;
    SharedPreferences sharedPreferences;
    String view_id;           // 사용자 id
    int indexFlag = -1;          // 0:한글 인덱스 / 1:영어 인덱스 구분
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_index);

        korIndexBtn = findViewById(R.id.kor_btn);
        engIndexBtn = findViewById(R.id.eng_btn);

        //아이디값받기
        sharedPreferences = getSharedPreferences("memberPreference",MODE_PRIVATE);
        view_id = sharedPreferences.getString("user_id","");

        korIndexBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 인덱스 선택
                selectedIndex = null;
                // 인덱스 선택
                final String[] korIndexArray = new String[]{"ㄱ", "ㄴ", "ㄷ", "ㄹ", "ㅁ", "ㅂ", "ㅅ", "ㅇ",
                        "ㅈ", "ㅊ", "ㅋ", "ㅌ", "ㅍ", "ㅎ"};
                AlertDialog.Builder dig = new AlertDialog.Builder(IndexSearch.this);
                dig.setTitle("검색할 용어의 초성 선택");
                dig.setIcon(R.mipmap.ic_launcher);
                dig.setItems(korIndexArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        korIndexBtn.setText(korIndexArray[which]);
                        selectedIndex = korIndexArray[which];
                        if(selectedIndex != null){
                            indexFlag = 0;
                            if(indexFlag == 0){
                                engIndexBtn.setText(R.string.initEngBtn);
                                indexSearch();
                            }
                        }
                    }
                });
                dig.setPositiveButton("닫기", null);
                dig.show();
            }
        });

        engIndexBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedIndex = null;
                // 인덱스 선택
                final String[] engIndexArray = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L",
                        "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
                AlertDialog.Builder dig = new AlertDialog.Builder(IndexSearch.this);
                dig.setTitle("검색할 용어의 초성 선택");
                dig.setIcon(R.mipmap.ic_launcher);
                dig.setItems(engIndexArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        engIndexBtn.setText(engIndexArray[which]);
                        selectedIndex = engIndexArray[which];
                        if(selectedIndex != null){
                            indexFlag = 1;
                            if(indexFlag == 1){
                                korIndexBtn.setText(R.string.initKorBtn);
                                indexSearch();
                            }
                        }
                    }
                });
                dig.setPositiveButton("닫기", null);
                dig.show();
            }
        });

    }


    private void init(){
        RecyclerView recyclerView = findViewById(R.id.recycle_view1);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        indexAdapter = new SearchAdapter(recyclerView);
        recyclerView.setAdapter(indexAdapter);          // adapter 설정
        indexAdapter.setRecordClickListener(new SearchAdapter.recordButtonClickListener() {
            @Override
            public void onRecordClick(ViewHolder holder, View view, final int position) { // 인터페이스 정의
                SearchData data = indexAdapter.getData(position);
                if(data.getTermRecord().equals("n")) {
                    data.setTermRecord("y");
                    indexAdapter.notifyItemChanged(position);
                    recordInsert(data.getTermEng(), data.getTermKor(), data.getTermExplain());
                }else if(data.getTermRecord().equals("y")){
                    data.setTermRecord("n");
                    indexAdapter.notifyItemChanged(position);
                    recordDelete(data.getTermEng());
                }
            }
        });
    }
    // 단어장 삭제
    private void recordDelete(String termEng){
        Response.Listener<String> recDeleteResponseListener = new Response.Listener<String>() {
            // 요청을 보내고 응답 받음
            @Override
            public void onResponse(String response) {
                try{
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String success = jsonObject.getString("success");
                    if(success.equals("fail")){
                        Toast.makeText(getApplicationContext(), "단어장 삭제 실패", Toast.LENGTH_LONG).show();
                    }
                }catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),"에러 발생: 통신 환경(php·android 코드) 확인", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"에러 발생:서버 연결 확인", Toast.LENGTH_LONG).show();
            }
        };
        RecordDeleteRequest recordDeleteRequest = new RecordDeleteRequest(view_id.toString(), termEng, recDeleteResponseListener, errorListener);
        recordDeleteRequest.setShouldCache(false);

        RequestQueue recDeleteQueue = Volley.newRequestQueue(getApplicationContext());
        recDeleteQueue.add(recordDeleteRequest);

    }

    // 단어장 등록
    private void recordInsert(String termEng, String termKor, String termExplain){
        Response.Listener<String> recInsertResponseListener = new Response.Listener<String>() {
            // 요청을 보내고 응답 받음
            @Override
            public void onResponse(String response) {
                try{
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String success = jsonObject.getString("success");
                    if(success.equals("fail")){
                        Toast.makeText(getApplicationContext(), "단어장 등록 실패", Toast.LENGTH_LONG).show();
                    }
                }catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),"에러 발생: 통신 환경(php·android 코드) 확인", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"에러 발생:서버 연결 확인", Toast.LENGTH_LONG).show();
            }
        };
        RecordInsertRequest recordInsertRequest = new RecordInsertRequest(view_id.toString(), termEng, termKor, termExplain, recInsertResponseListener, errorListener);
        recordInsertRequest.setShouldCache(false);

        RequestQueue recInsertQueue = Volley.newRequestQueue(getApplicationContext());
        recInsertQueue.add(recordInsertRequest);
    }

    private void getData(String eng, String kor, String explain, String record){
        SearchData itemData = new SearchData(eng, kor, explain, record);
        indexAdapter.addItem(itemData);
    }

    // 인덱스 검색
    private void indexSearch(){
        Response.Listener<String> indexResponseListener = new Response.Listener<String>() {
            // 요청을 보내고 응답 받음
            @Override
            public void onResponse(String response) {
                try{
                    init();
                    JSONArray jsonArray = new JSONArray(response);
                    for(int i=0; i<jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        if(i == 0){
                            String success = jsonObject.getString("success");
                            if(success.equals("empty")) {
                                Toast.makeText(getApplicationContext(), "검색 결과 없음", Toast.LENGTH_LONG).show();
                                break;
                            }else if(success.equals("fail")){
                                Toast.makeText(getApplicationContext(), "검색 실패", Toast.LENGTH_LONG).show();
                                break;
                            }
                        }else {
                            String dicTermEng = jsonObject.getString("termEng");
                            String dicTermKor = jsonObject.getString("termKor");
                            String dicExplain = jsonObject.getString("termExplain");
                            String dicRecord = jsonObject.getString("termRecord");

                            getData(dicTermEng, dicTermKor, dicExplain, dicRecord);
                        }
                    }
                }catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),"에러 발생: 통신 환경(php·android 코드) 확인", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"에러 발생:서버 연결 확인", Toast.LENGTH_LONG).show();
            }
        };

        // Volley로 회원양식 웹으로 전송
        IndexSearchRequest indexSearchRequest = new IndexSearchRequest(view_id.toString(), selectedIndex, indexResponseListener, errorListener);
        indexSearchRequest.setShouldCache(false);

        RequestQueue indexQueue = Volley.newRequestQueue(getApplicationContext());
        indexQueue.add(indexSearchRequest);
    }


}
