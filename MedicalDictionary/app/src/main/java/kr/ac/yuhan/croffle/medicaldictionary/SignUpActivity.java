package kr.ac.yuhan.croffle.medicaldictionary;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    EditText editName, editID, editPW, checkPW;
    Button signUp, checkBtn, checkID;
    String ID1, ID2, PW1;
    private AlertDialog dialog;
    private boolean validate = false;

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
        setContentView(R.layout.activity_signup);

        signUp = (Button) findViewById(R.id.signUp);
        checkBtn = (Button) findViewById(R.id.checkBtn);
        checkID = (Button) findViewById(R.id.checkID);

        editName = (EditText) findViewById(R.id.editName);
        editID = (EditText) findViewById(R.id.editID);
        editPW = (EditText) findViewById(R.id.editPW);
        checkPW = (EditText) findViewById(R.id.checkPW);

        signUp.setOnClickListener(this);
        checkBtn.setOnClickListener(this);
        checkID.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.checkID:
                checkID();
                break;

            case R.id.checkBtn:
                if (editPW.getText().toString().equals(checkPW.getText().toString())) {
                    Toast.makeText(SignUpActivity.this, "비밀번호가 일치합니다.", Toast.LENGTH_LONG).show();
                    PW1 = editPW.getText().toString(); // 일치한 비밀번호 저장
                } else {
                    Toast.makeText(SignUpActivity.this, "비밀번호가 다릅니다.", Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.signUp:
                if (isEditTextEmpty(editID)) {
                    Toast.makeText(SignUpActivity.this, "아이디에 공백이 있습니다.", Toast.LENGTH_LONG).show();
                } else if(isEditTextEmpty(editPW)) {
                    Toast.makeText(SignUpActivity.this, "비밀번호에 공백이 있습니다.", Toast.LENGTH_LONG).show();
                } else if(isEditTextEmpty(editName)) {
                    Toast.makeText(SignUpActivity.this, "이름에 공백이 있습니다.", Toast.LENGTH_LONG).show();
                }
                else {
                    reCheck();
                }
                break;
        }
    }

    private boolean isEditTextEmpty(EditText editText) {
        return editText.getText().toString().trim().isEmpty();
    }


    private void checkID() {
        String userID = editID.getText().toString();
        if (validate) {
            return; //검증 완료
        }

        if (userID.equals("")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
            dialog = builder.setMessage("아이디를 입력하세요.").setPositiveButton("확인", null).create();
            dialog.show();
            return;
        }

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    if (success) {
                        ID1 = editID.getText().toString();
                        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                        dialog = builder.setMessage("사용할 수 있는 아이디입니다.").setPositiveButton("확인", null).create();
                        dialog.show();
                        //editID.setEnabled(false); //아이디값 고정
                        validate = true; //검증 완료
                        // 입력된 값 저장 - 아이디1
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                        dialog = builder.setMessage("이미 존재하는 아이디입니다.").setNegativeButton("확인", null).create();
                        dialog.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        ValidateRequest validateRequest = new ValidateRequest(userID, responseListener);
        RequestQueue queue = Volley.newRequestQueue(SignUpActivity.this);
        queue.add(validateRequest);

    }

    // ID와 PW를 다시 체크하는 함수
    private void reCheck() {
        String password = editPW.getText().toString();
        String confirmPassword = checkPW.getText().toString();

        if (password.equals(confirmPassword)) {
            reCheckID();
        } else {
            Toast.makeText(SignUpActivity.this, "비밀번호가 다릅니다.", Toast.LENGTH_LONG).show();
        }
    }

    // ID를 다시 체크하는 함수
    private void reCheckID() {
        String userID = editID.getText().toString();

        if (validate) {
            if (userID.equals(ID1)) {
                signup();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                dialog = builder.setMessage("아이디 중복확인을 다시 진행해야 합니다.").setNegativeButton("확인", null).create();
                dialog.show();
                validate = false; // 중복확인 여부 초기화
            }
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
            dialog = builder.setMessage("아이디 중복확인을 먼저 진행해야 합니다.").setNegativeButton("확인", null).create();
            dialog.show();
        }
    }

    private void signup() {
        String userID = editID.getText().toString();
        String userPW = editPW.getText().toString();
        String userName = editName.getText().toString();

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String success = jsonObject.getString("success");
                    if (success.equals("1")) {  // 회원가입 완료
                        Toast.makeText(getApplicationContext(), "회원가입 성공!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "회원가입 실패!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "회원가입 처리시 에러발생!", Toast.LENGTH_SHORT).show();
            }
        };

        SignUpRequest signupRequest = new SignUpRequest(userID, userPW, userName, responseListener, errorListener);
        signupRequest.setShouldCache(false);

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(signupRequest);


    }
}