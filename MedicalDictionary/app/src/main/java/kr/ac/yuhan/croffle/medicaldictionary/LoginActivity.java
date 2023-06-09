package kr.ac.yuhan.croffle.medicaldictionary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private EditText editID, editPW;
    private Button signUp, login;
    private SharedPreferences sharedPreferences;
    boolean dbFlag = true;         // db 테이블 및 데이터 생성 여부

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
        setContentView(R.layout.activity_login);

        editID = findViewById(R.id.editID);
        editPW = findViewById(R.id.editPW);
        signUp = findViewById(R.id.signUp);
        login = findViewById(R.id.login);

        // SharedPreferences 인스턴스 가져오기
        sharedPreferences = getSharedPreferences("memberPreference", MODE_PRIVATE);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSignUp = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intentSignUp);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        // 앱 실행 시, DB 테이블 생성 및 데이터 입력
        if(dbFlag){
            createDB();
        }
    }

    public void createDB(){
        dbFlag = false;
        // 앱 실행 시, DB 테이블 생성 및 데이터 입력
        Response.Listener<String> dbResponseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {  }
        };

        Response.ErrorListener dbErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dbFlag = true;
                Toast.makeText(getApplicationContext(), "테이블 생성 및 데이터 입력 중 오류 발생", Toast.LENGTH_SHORT).show();
            }
        };

        DBTableRequest dbTableRequest = new DBTableRequest(dbResponseListener, dbErrorListener);
        dbTableRequest.setShouldCache(false);

        RequestQueue dbQueue = Volley.newRequestQueue(getApplicationContext());
        dbQueue.add(dbTableRequest);
    }

    public void saveUserInfo(String userID, String userName, String userPW) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_id", userID);
        editor.putString("user_name", userName);
        editor.putString("user_pw", userPW);
        editor.apply(); // apply()를 사용하여 비동기적으로 저장

    }

    private void login() {
        final String userID = editID.getText().toString().trim();
        final String userPW = editPW.getText().toString().trim();

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String success = jsonObject.getString("success");
                    if (success.equals("1")) {
                        Toast.makeText(getApplicationContext(), "로그인 성공!", Toast.LENGTH_SHORT).show();

                        String userName = jsonObject.getString("user_name");
                        saveUserInfo(userID, userName, userPW);

                        //수정
                        Intent intent = new Intent(getApplicationContext(), ChangeMenu.class);
                        intent.putExtra("user_id", userID); // 사용자 ID를 전달
                        startActivity(intent);
                        finish();

                    } else {
                        Toast.makeText(getApplicationContext(), "로그인 실패!", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    String errorMessage = "서버 응답 처리 중 오류가 발생했습니다.";
                    Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String errorMessage = "로그인 처리시 에러발생!";
                Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        };

        // Volley로 로그인 양식 웹전송
        LoginRequest loginRequest = new LoginRequest(userID, userPW, responseListener, errorListener);
        loginRequest.setShouldCache(false);

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(loginRequest);
    }
}
