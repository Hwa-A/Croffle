package kr.ac.yuhan.croffle.medicaldictionary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TextSearch extends AppCompatActivity {
    SearchAdapter textAdapter;
    private Button resultBtn;
    private SharedPreferences sharedPreferences;
    EditText textSearch;
    String term;
    String view_id;           // 사용자 id
    // editText가 아닌 외부 영역 터치하는 경우, 키보드 숨김
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View focusView = getCurrentFocus();
        if(focusView != null){
            Rect rect = new Rect();
            focusView.getGlobalVisibleRect(rect);
            int x = (int)ev.getX(), y = (int)ev.getY();
            if(!rect.contains(x, y)){
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if(imm != null)
                    imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
                focusView.clearFocus();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_text);

        resultBtn = findViewById(R.id.result_btn);
        textSearch = findViewById(R.id.text_search_ed);

        //아이디값받기
        sharedPreferences = getSharedPreferences("memberPreferences",MODE_PRIVATE);
        view_id = sharedPreferences.getString("user_id","fail");

        resultBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                term = textSearch.getText().toString();
                String empFlag = term.replaceAll(" ", "");
                // 검색 값이 공백인 경우
                if(empFlag.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"검색어를 입력해주세요", Toast.LENGTH_LONG).show();
                }
                else {
                    textSearch();
                }
            }
        });
    }
    private void init(){
        RecyclerView recyclerView = findViewById(R.id.recycle_view2);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        textAdapter = new SearchAdapter(recyclerView);
        recyclerView.setAdapter(textAdapter);               // adapter 설정
        textAdapter.setRecordClickListener(new SearchAdapter.recordButtonClickListener() {
            @Override
            public void onRecordClick(ViewHolder holder, View view, final int position) { // 인터페이스 정의
                SearchData data = textAdapter.getData(position);
                if(data.getTermRecord().equals("n")) {
                    data.setTermRecord("y");
                    textAdapter.notifyItemChanged(position);
                    recordInsert(data.getTermEng(), data.getTermKor(), data.getTermExplain());
                }else if(data.getTermRecord().equals("y")){
                    data.setTermRecord("n");
                    textAdapter.notifyItemChanged(position);
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
        RecordInsertRequest recordInsertRequest = new RecordInsertRequest(view_id, termEng, termKor, termExplain, recInsertResponseListener, errorListener);
        recordInsertRequest.setShouldCache(false);

        RequestQueue recInsertQueue = Volley.newRequestQueue(getApplicationContext());
        recInsertQueue.add(recordInsertRequest);
    }

    private void getData(String eng, String kor, String explain, String record){
        SearchData itemData = new SearchData(eng, kor, explain, record);
        textAdapter.addItem(itemData);
    }
    
    // 텍스트 검색
    private void textSearch(){
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
        TextSearchRequest textSearchRequest = new TextSearchRequest(view_id, term, responseListener, errorListener);
        textSearchRequest.setShouldCache(false);

        RequestQueue textQueue = Volley.newRequestQueue(getApplicationContext());
        textQueue.add(textSearchRequest);
    }
}