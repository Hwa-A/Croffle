package kr.ac.yuhan.croffle.medicaldictionary;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RecordInsertRequest extends StringRequest {
    private final static String recordInsertURL = "http://10.0.2.2/MedicalDictionary_php/RecordInsert.php";
    private Map<String, String> recordInsertMap;

    public RecordInsertRequest(String id, String termEng, String termKor, String termExplain,  Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, recordInsertURL, listener, errorListener);

        recordInsertMap = new HashMap<>();
        recordInsertMap.put("id", id);
        recordInsertMap.put("termEng", termEng);
        recordInsertMap.put("termKor", termKor);
        recordInsertMap.put("termExplain", termExplain);
    }
    //POST 방식으로 보내질 매개변수들을 HashMap 형태로 모아서 보냄
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return recordInsertMap;
    }
}
