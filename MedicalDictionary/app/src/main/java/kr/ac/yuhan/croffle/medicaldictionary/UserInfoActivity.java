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

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText editID, editName, editPW, checkPW;
    private Button btnUpdate, btnCancel, btnCheck;
    private SharedPreferences sharedPreferences;
    private String userID;

    // editText가 아닌 외부 영역 터치하는 경우, 키보드 숨김
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View focusView = getCurrentFocus();
        if (focusView != null) {
            Rect rect = new Rect();
            focusView.getGlobalVisibleRect(rect);
            int x = (int) ev.getX(), y = (int) ev.getY();
            if (!rect.contains(x, y)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null)
                    imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
                focusView.clearFocus();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);

        editID = findViewById(R.id.editID);
        editName = findViewById(R.id.editName);
        editPW = findViewById(R.id.editPW);
        checkPW = findViewById(R.id.testPW);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnCancel = findViewById(R.id.btnCancel);
        btnCheck = findViewById(R.id.btnPWTest);

        // SharedPreferences 인스턴스 가져오기
        sharedPreferences = getSharedPreferences("memberPreference", MODE_PRIVATE);

        // LoginActivity에서 전달한 회원 정보를 EditText에 설정하여 표시
        userID = sharedPreferences.getString("user_id", "");
        String userName = sharedPreferences.getString("user_name", "");
        String userPW = sharedPreferences.getString("user_pw", "");
        String userPWTest = sharedPreferences.getString("user_pw", "");
        editID.setText(userID);
        editName.setText(userName);
        editPW.setText(userPW);
        checkPW.setText(userPWTest);
        editID.setEnabled(false);
        editName.setEnabled(false);

        btnCheck.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPWTest:
                if(isPWCheck(editPW, checkPW)){
                    Toast.makeText(UserInfoActivity.this, "비밀번호가 일치합니다.", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(UserInfoActivity.this, "비밀번호가 다릅니다.", Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.btnUpdate:
                if (isEditTextEmpty(editID)) {
                    Toast.makeText(UserInfoActivity.this, "아이디에 공백이 있습니다.", Toast.LENGTH_LONG).show();
                } else if (isEditTextEmpty(editPW)) {
                    Toast.makeText(UserInfoActivity.this, "비밀번호에 공백이 있습니다.", Toast.LENGTH_LONG).show();
                } else if (isEditTextEmpty(editName)) {
                    Toast.makeText(UserInfoActivity.this, "이름에 공백이 있습니다.", Toast.LENGTH_LONG).show();
                } else if(!(isPWCheck(editPW, checkPW))){
                    Toast.makeText(UserInfoActivity.this, "비밀번호 중복 확인을 해주세요.", Toast.LENGTH_LONG).show();
                }
                else {
                    updateUserInfo();
                }
                break;

            case R.id.btnCancel:
                finish();
                break;
        }
    }

    private boolean isPWCheck(EditText pw1, EditText pw2){
        boolean pFlag = false;
        if((pw1.getText().toString()).equals(pw2.getText().toString())){
            pFlag = true;
        }
        return pFlag;
    }

    private boolean isEditTextEmpty(EditText editText) {
        return editText.getText().toString().trim().isEmpty();
    }

    private void updateUserInfo() {
        final String userName = editName.getText().toString().trim();
        final String userPW = editPW.getText().toString().trim();

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String success = jsonObject.getString("success");
                    if (success.equals("1")) {
                        Toast.makeText(getApplicationContext(), "회원 정보가 업데이트되었습니다.\n다시 로그인해주세요.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        String error = jsonObject.optString("error");
                        Toast.makeText(getApplicationContext(), "회원 정보 업데이트에 실패했습니다. 오류: " + error, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "서버 응답 처리 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(getApplicationContext(), "서버 응답 처리 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        };

        // 회원 정보 업데이트 요청 전송
        UserInfoRequest userInfoRequest = new UserInfoRequest(userID, userName, userPW, responseListener, errorListener);
        userInfoRequest.setShouldCache(false);

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(userInfoRequest);
    }
}
