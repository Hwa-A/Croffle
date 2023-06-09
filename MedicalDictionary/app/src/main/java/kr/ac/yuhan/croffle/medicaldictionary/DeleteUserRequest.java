package kr.ac.yuhan.croffle.medicaldictionary;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class DeleteUserRequest extends StringRequest {
    private final static String deleteUserURL = "http://10.0.2.2/MedicalDictionary_php/medicaldic_member_delete.php";
    private Map<String, String> deleteUserMap;

    public DeleteUserRequest(String userID, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, deleteUserURL, listener, errorListener);

        deleteUserMap = new HashMap<>();
        deleteUserMap.put("user_id", userID);
    }
    //POST 방식으로 보내질 매개변수들을 HashMap 형태로 모아서 보냄
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return deleteUserMap;
    }
}