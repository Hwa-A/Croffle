package kr.ac.yuhan.croffle.medicaldictionary;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class IndexSearchRequest extends StringRequest {
    private final static String indexURL = "http://10.0.2.2/MedicalDictionary_php/IndexSearch.php";
    private Map<String, String> indexMap;

    public IndexSearchRequest(String id, String termIndex, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, indexURL, listener, errorListener);

        indexMap = new HashMap<>();
        indexMap.put("id", id);
        indexMap.put("termIndex", termIndex);
    }
    //POST 방식으로 보내질 매개변수들을 HashMap 형태로 모아서 보냄
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return indexMap;
    }
}
