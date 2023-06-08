package kr.ac.yuhan.croffle.medicaldictionary;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RecordViewRequest extends StringRequest {
    private final static String recordURL = "http://10.0.2.2:8012/MedicalDictionary_php/RecordSelect.php";
    private Map<String, String> recordMap;

    public RecordViewRequest(String id, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Request.Method.POST, recordURL, listener, errorListener);

        recordMap = new HashMap<>();
        recordMap.put("id", id);
    }
    //POST 방식으로 보내질 매개변수들을 HashMap 형태로 모아서 보냄
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return recordMap;
    }
}
