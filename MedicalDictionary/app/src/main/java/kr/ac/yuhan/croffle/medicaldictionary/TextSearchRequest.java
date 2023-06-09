package kr.ac.yuhan.croffle.medicaldictionary;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class TextSearchRequest extends StringRequest {
    private final static String textURL = "http://10.0.2.2/MedicalDictionary_php/TextSearch.php";
    private Map<String, String> textMap;

    public TextSearchRequest(String id, String term, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, textURL, listener, errorListener);

        textMap = new HashMap<>();
        textMap.put("id", id);
        textMap.put("term", term);
    }
    //POST 방식으로 보내질 매개변수들을 HashMap 형태로 모아서 보냄
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return textMap;
    }
}
