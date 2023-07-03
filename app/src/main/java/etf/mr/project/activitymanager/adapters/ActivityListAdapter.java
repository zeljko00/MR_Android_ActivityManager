package etf.mr.project.activitymanager.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import etf.mr.project.activitymanager.R;
import etf.mr.project.activitymanager.model.ActivityDTO;

public class ActivityListAdapter extends RecyclerView.Adapter<ActivityListAdapter.ViewHolder>{

    // list data
    private List<ActivityDTO> data;
    // handler for list item click
    public interface ItemClickHandler{
        void handleItemClick(ActivityDTO item);
    }
    public interface ItemLongClickHandler{
        void handleItemLongClick(ActivityDTO item);
    }
    // list item view
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView type;
        public TextView date;
        public TextView day;
        public TextView month;
        public ImageView icon;
        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            title = v.findViewById(R.id.title_textView);
            type = v.findViewById(R.id.type_textView);
            date=v.findViewById(R.id.date_textView);
            day=v.findViewById(R.id.day_textView);
            month=v.findViewById(R.id.month_textView);
            icon=v.findViewById(R.id.type_icon);
        }
    }
    private ItemClickHandler itemClickHandler;
    private ItemLongClickHandler itemLongClickHandler;
    public ActivityListAdapter(List<ActivityDTO> data, ItemClickHandler itemClickHandler, ItemLongClickHandler itemLongClickHandler){
        this.data=new ArrayList<>(data);
        this.itemClickHandler=itemClickHandler;
        this.itemLongClickHandler=itemLongClickHandler;
    }

    // binding list item view and its layout
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.activity_list_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // inserting data into view
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,int position) {
        final ActivityDTO item = data.get(position);
        holder.title.setText(item.getTitle());
        holder.type.setText(item.getType());
        holder.icon.setImageDrawable(item.getIcon());
        holder.date.setText(item.getDate());
        holder.day.setText(item.getDay());
        holder.month.setText(item.getMonth());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                itemClickHandler.handleItemClick(item);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener(){
            @Override public boolean onLongClick(View v) {
                itemLongClickHandler.handleItemLongClick(item);
                return true;
            }
        });
    }
    public void changeData(List<ActivityDTO> newData){
        data=newData;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount(){
        return data.size();
    }
}
