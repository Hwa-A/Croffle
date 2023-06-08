package kr.ac.yuhan.croffle.medicaldictionary;

import android.content.Context;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.util.Locale;

public class TTSHelper implements TextToSpeech.OnInitListener {
    private TextToSpeech tts;
    private Context context;

    public TTSHelper(Context context){
        this.context = context;
        tts = new TextToSpeech(context,this);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(Locale.ENGLISH);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(context, "TTS language not supported", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "TTS initialization failed", Toast.LENGTH_SHORT).show();
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void speak(String text) {
        if (tts != null) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }
    public void shutdown() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }
}