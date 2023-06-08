package kr.ac.yuhan.croffle.medicaldictionary;

import android.animation.ValueAnimator;
import android.text.method.ScrollingMovementMethod;
import android.util.SparseBooleanArray;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



// 데이터 값에 따라 변경되어 보여질 뷰
public class ViewHolder extends RecyclerView.ViewHolder {
    TextView termEng;
    TextView termKor;
    TextView termExplain;
    ImageButton ibtnRecord;                    // 단어장 등록/취소 버튼
    LinearLayout linearLayout;
    SearchViewHolderItemClickListener searchViewHolderItemClickListener;
    TTSHelper ttsHelper;
    ImageButton tts_btn;
    private SearchAdapter.recordButtonClickListener recordClickListener;

    public ViewHolder(@NonNull View itemView, RecyclerView reView){
        super(itemView);        // 부모 클래스의 생성자를 호출

        termEng = itemView.findViewById(R.id.eng_tv);
        termKor = itemView.findViewById(R.id.kor_tv);
        termExplain = itemView.findViewById(R.id.explain_tv);
        ibtnRecord = itemView.findViewById(R.id.record_ibn);
        linearLayout = itemView.findViewById(R.id.linearLayout);

        termExplain.setMovementMethod(ScrollingMovementMethod.getInstance());
        // 텍스트 뷰 안에 스크롤 터치
        termExplain.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                reView.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        // 이미지 버튼 클릭한 경우, 정의한 인터페이스 실행
        ibtnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = getBindingAdapterPosition();
                if(position != RecyclerView.NO_POSITION){
                    if(recordClickListener != null){
                       recordClickListener.onRecordClick(ViewHolder.this, v, position);
                    }
                }
            }
        });
        // linearlayout 클릭한 경우, 정의한 인터페이스 실행
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchViewHolderItemClickListener.searchViewHolderItemClick();
            }
        });
        ttsHelper = new TTSHelper(itemView.getContext());
        tts_btn = itemView.findViewById(R.id.listen_btn);
        tts_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String termEng_1 = termEng.getText().toString();
                ttsHelper.speak(termEng_1);
            }
        });
    }
    // 뷰 홀더와 뷰 데이터를 바인딩
    public void onBind(SearchData searchData, int position, SparseBooleanArray selectedItems){
        termEng.setText(searchData.getTermEng());
        termKor.setText(searchData.getTermKor());
        termExplain.setText(searchData.getTermExplain());
        String termRecord = searchData.getTermRecord();
        switch (termRecord){
            case "n":
                ibtnRecord.setImageResource(R.drawable.unselect_heart);
                break;
            case "y":
                ibtnRecord.setImageResource(R.drawable.select_heart);
                break;
        }
        changeVisibility(selectedItems.get(position));
    }
    // 확장 여부에 따라 뷰 홀더 보일지 설정
    private void changeVisibility(final boolean isExpanded){
        // ValueAnimator.ofInt : 뷰가 변할 값 설정
        ValueAnimator va = isExpanded ? ValueAnimator.ofInt(0, 250) : ValueAnimator.ofInt(250, 0);
        va.setDuration(300); // Animation 실행 시간
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator animation) {
                // termExplain 높이 설정
                termExplain.getLayoutParams().height = (int)animation.getAnimatedValue();
                termExplain.requestLayout();
                // termExplain 가시성 여부 설정
                termExplain.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
            }
        });
        // Animation 시작
        va.start();
    }
    
    public void setOnViewHolderItemClickListener(SearchViewHolderItemClickListener searchViewHolderItemClickListener){
        this.searchViewHolderItemClickListener = searchViewHolderItemClickListener;
    }
    public void setRecordClickListener(SearchAdapter.recordButtonClickListener recordClickListener){
        this.recordClickListener = recordClickListener;
    }
    public void shutdownTTS() {
        ttsHelper.shutdown();
    }
}
