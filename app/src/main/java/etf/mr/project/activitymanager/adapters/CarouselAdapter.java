package etf.mr.project.activitymanager.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.stream.Collectors;

import etf.mr.project.activitymanager.R;
import etf.mr.project.activitymanager.model.ActivityDTO;

public class CarouselAdapter extends RecyclerView.Adapter<CarouselAdapter.ViewHolder>{

    private List<String> data;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView img;
        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            img=v.findViewById(R.id.carousel_image_view);
        }
    }
    public CarouselAdapter(List<String> data){
        this.data=data;
    }

    // binding list item view and its layout
    @Override
    public CarouselAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.carousel_item, parent, false);
        CarouselAdapter.ViewHolder vh = new CarouselAdapter.ViewHolder(v);
        return vh;
    }
    // inserting data into view
    @Override
    public void onBindViewHolder(@NonNull CarouselAdapter.ViewHolder holder, int position) {
        final String path = data.get(position);
        showPhoto(holder.img,path);

    }
    public void addImg(String path) {
        data.add(path);
        notifyDataSetChanged(); // Notify the adapter of the data change
    }
    @Override
    public int getItemCount(){
        return data.size();
    }

    private void showPhoto(ImageView imageView, String path) {

        Bitmap bitmap = BitmapFactory.decodeFile(path);
        imageView.setImageBitmap(bitmap);
    }
}

