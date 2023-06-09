package kr.ac.yuhan.croffle.medicaldictionary;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class DBTableRequest extends StringRequest {
    private final static String DBTableURL = "http://10.0.2.2/MedicalDictionary_php/DBConfig.php";
    private Map<String, String> DBTableMap;

    public DBTableRequest(Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, DBTableURL, listener, errorListener);

        DBTableMap = new HashMap<>();
    }
    //POST 방식으로 보내질 매개변수들을 HashMap 형태로 모아서 보냄
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return DBTableMap;
    }
}