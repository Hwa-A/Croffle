package kr.ac.yuhan.croffle.medicaldictionary;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class UserInfoRequest extends StringRequest {
    private static final String URL = "http://10.0.2.2:8012/MedicalDictionary_php/medicaldic_member_userinfo.php";
    private Map<String, String> params;

    public UserInfoRequest(String userID, String userName, String userPW, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, URL, listener, errorListener);
        params = new HashMap<>();
        params.put("user_id", userID);
        params.put("user_name", userName);
        params.put("user_pw", userPW);
    }

    @Override
    protected Map<String, String> getParams() {
        return params;
    }
}