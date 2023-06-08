package kr.ac.yuhan.croffle.medicaldictionary;
import android.annotation.SuppressLint;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;



import java.util.ArrayList;

// 데이터와 아이템에 대한 뷰 생성
public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<SearchData> listData = new ArrayList<>();       // adapter에 들어갈 list
    private SparseBooleanArray selectedItems = new SparseBooleanArray(); // Item의 클릭 상태를 저장할 array
    private int prePosition = -1;               // 직전에 클릭됐던 Item의 position
    private RecyclerView reView;
    private recordButtonClickListener recordClickListener;

    public interface recordButtonClickListener {
        void onRecordClick(ViewHolder holder, View view, int position);
    }

    public SearchAdapter(RecyclerView reView){
        this.reView = reView;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item, parent, false);
        return new ViewHolder(view, reView);
    }

    // 각 뷰 홀더에 데이터를 연결해주는 함수
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.onBind(listData.get(position), position, selectedItems);
        // ViewHolder에 RecordButtonClickListener 인터페이스
        viewHolder.setRecordClickListener(recordClickListener);

        // ViewHolder에 SearchViewHolderItemClickListener 인터페이스 설정
        viewHolder.setOnViewHolderItemClickListener(new SearchViewHolderItemClickListener() {
            @Override
            public void searchViewHolderItemClick() {
                if (selectedItems.get(position)) {
                    // 펼친 Item을 클릭
                    selectedItems.delete(position);
                } else {
                    // 앞서 클릭된 Item의 클릭상태 지움
                    selectedItems.delete(prePosition);
                    // 클릭한 Item의 position 저장
                    selectedItems.put(position, true);
                }
                // 해당 position의 변화를 알림
                if (prePosition != -1) notifyItemChanged(prePosition);
                notifyItemChanged(position);
                // 클릭된 position 저장
                prePosition = position;
            }
        });
    }

    @Override
    public int getItemCount() {
        return listData.size();         // adapter에서 관리할 item 개수
    }

    void addItem(SearchData itemData) {
        // 외부에서 item을 추가
        listData.add(itemData);
    }

    // 해당 위치의 item 삭제
    void removeItem(int position){
        listData.remove(position);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    // item 가져오기
    public SearchData getData(int position){
        return listData.get(position);
    }

    public void setRecordClickListener(recordButtonClickListener recordClickListener){
        this.recordClickListener = recordClickListener;
    }
}
