package kr.ac.yuhan.croffle.medicaldictionary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RecordView extends AppCompatActivity {
    SearchAdapter recordAdapter;
    private SharedPreferences sharedPreferences;
    String view_id;         // 사용자 id

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_record);
        sharedPreferences = getSharedPreferences("memberPreference", MODE_PRIVATE);
        view_id = sharedPreferences.getString("user_id","fail");

        recordView();
    }

    private void init(){
        RecyclerView recyclerView = findViewById(R.id.recycle_view3);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        recordAdapter = new SearchAdapter(recyclerView);
        recyclerView.setAdapter(recordAdapter);               // adapter 설정
        recordAdapter.setRecordClickListener(new SearchAdapter.recordButtonClickListener() {
            @Override
            public void onRecordClick(ViewHolder holder, View view, final int position) { // 인터페이스 정의
                SearchData data = recordAdapter.getData(position);
                if(data.getTermRecord().equals("y")){
                    data.setTermRecord("n");
                    recordAdapter.removeItem(position);
                    recordAdapter.notifyItemRemoved(position);
                    recordAdapter.notifyItemRangeChanged(position, recordAdapter.getItemCount());
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
        RecordDeleteRequest recordDeleteRequest = new RecordDeleteRequest(view_id, termEng, recDeleteResponseListener, errorListener);
        recordDeleteRequest.setShouldCache(false);

        RequestQueue recDeleteQueue = Volley.newRequestQueue(getApplicationContext());
        recDeleteQueue.add(recordDeleteRequest);

    }

    private void getData(String eng, String kor, String explain, String record){
        SearchData itemData = new SearchData(eng, kor, explain, record);
        recordAdapter.addItem(itemData);
    }

    // 단어장 출력
    private void recordView(){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            // 요청을 보내고 응답 받았을 때
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
                                Toast.makeText(getApplicationContext(), "등록된 단어 없음", Toast.LENGTH_LONG).show();
                                break;
                            }else if(success.equals("fail")){
                                Toast.makeText(getApplicationContext(), "단어장 검색 실패", Toast.LENGTH_LONG).show();
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
        RecordViewRequest recordViewRequest = new RecordViewRequest(view_id, responseListener, errorListener);
        recordViewRequest.setShouldCache(false);

        RequestQueue recordQueue = Volley.newRequestQueue(getApplicationContext());
        recordQueue.add(recordViewRequest);
    }
}
