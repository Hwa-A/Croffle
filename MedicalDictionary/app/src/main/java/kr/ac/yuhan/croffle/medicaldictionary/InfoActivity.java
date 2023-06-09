package kr.ac.yuhan.croffle.medicaldictionary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

public class InfoActivity extends AppCompatActivity {
    private TextView txtUserID;
    private Button infoCheck, infoDelete, infoLogout;
    private SharedPreferences sharedPreferences;
    private String userID; // 로그인한 사용자의 아이디 저장 변수

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        txtUserID = findViewById(R.id.txtUserID);
        infoCheck = findViewById(R.id.infoCheck);
        infoDelete = findViewById(R.id.infoDelete);
        infoLogout = findViewById(R.id.infoLogout);

        // SharedPreferences 인스턴스 가져오기
        sharedPreferences = getSharedPreferences("memberPreference", MODE_PRIVATE);

        // 전달된 사용자 ID 값 받아오기
        Intent intent = getIntent();
        userID = intent.getStringExtra("user_id");

        if (userID != null) {
            txtUserID.setText(userID + "님의 정보");
        } else {
            txtUserID.setText("사용자 ID를 불러올 수 없습니다.");
        }

        infoCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentC = new Intent(getApplicationContext(), UserInfoActivity.class);
                intentC.putExtra("user_id", userID);
                startActivity(intentC);
            }
        });

        infoDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteUserInfo();
            }
        });

        infoLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                // 로그인 정보 삭제 및 로그인 화면으로 이동
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void deleteUserInfo() {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    // 서버에서 받은 JSON 응답을 파싱
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    if (jsonObject.has("success")) {
                        String success = jsonObject.getString("success");
                        if (success.equals("1")) {
                            // 회원 탈퇴 성공
                            Toast.makeText(getApplicationContext(), "회원 탈퇴가 완료되었습니다.", Toast.LENGTH_SHORT).show();
                            // 로그인 정보 삭제 및 로그인 화면으로 이동
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.clear();
                            editor.apply();
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else if (success.equals("-1")) {
                            // 회원 탈퇴 실패
                            if (jsonObject.has("error")) {
                                String error = jsonObject.getString("error");
                                Toast.makeText(getApplicationContext(), "회원 탈퇴에 실패했습니다. 오류: " + error, Toast.LENGTH_SHORT).show();
                            }  else {
                                Toast.makeText(getApplicationContext(), "서버 응답 처리 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
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
                Toast.makeText(getApplicationContext(), "서버 응답 처리 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        };

        // Volley로 회원 탈퇴 요청
        DeleteUserRequest deleteUserRequest = new DeleteUserRequest(userID, responseListener, errorListener);
        deleteUserRequest.setShouldCache(false);

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(deleteUserRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // SharedPreferences에서 회원 정보 가져오기
        String savedUserID = sharedPreferences.getString("user_id", "");
        String savedUserName = sharedPreferences.getString("user_name", "");

        userID = savedUserID;

        if (savedUserID.equals(userID)) {
            // 현재 로그인한 사용자의 정보와 SharedPreferences에 저장된 정보가 일치하는 경우에만 회원 정보 표시
            txtUserID.setText(savedUserID + "님의 정보");
            txtUserID.setTextColor(Color.parseColor("#154D3A"));
        } else {
            // 회원 정보가 일치하지 않는 경우 로그인 화면으로 이동
            Toast.makeText(getApplicationContext(), "회원 정보를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
