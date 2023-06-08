package kr.ac.yuhan.croffle.medicaldictionary;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RecordDeleteRequest extends StringRequest {
    private final static String recordDeleteURL = "http://10.0.2.2:8012/MedicalDictionary_php/RecordDelete.php";
    private Map<String, String> recordDeleteMap;

    public RecordDeleteRequest(String id, String termEng, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, recordDeleteURL, listener, errorListener);

        recordDeleteMap = new HashMap<>();
        recordDeleteMap.put("id", id);
        recordDeleteMap.put("termEng", termEng);
    }
    //POST 방식으로 보내질 매개변수들을 HashMap 형태로 모아서 보냄
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return recordDeleteMap;
    }
}
