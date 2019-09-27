package com.yang.dyvideo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yang.dyvideo.R;
import com.yang.dyvideo.data.Video;

import java.util.List;

/**
 * @author yangzc
 * @data 2019/9/27 11:17
 * @desc
 */
public class VideoPlayerAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<Video> mVideoList;
    private OnItemClickListener onItemClickListener;

    //点击事件的接口
    public interface OnItemClickListener {
        void onItemClick(View view, int position, Video mVideo);

    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public VideoPlayerAdapter(Context context, List<Video> videoList) {
        mContext = context;
        mVideoList = videoList;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new VideoPlayerViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_video_list, viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        Video mVideo = mVideoList.get(i);

        VideoPlayerViewHolder mVideoPlayerViewHolder = (VideoPlayerViewHolder) viewHolder;
        mVideoPlayerViewHolder.dy_iv.setImageResource(mVideo.getIamge());
        mVideoPlayerViewHolder.dy_tv.setText(mVideo.getTitle());

        setOnItemClick(mVideoPlayerViewHolder);
    }

    protected void setOnItemClick(final VideoPlayerViewHolder holder) {
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    {
                        int position = holder.getLayoutPosition();
                        onItemClickListener.onItemClick(holder.itemView, position, mVideoList.get(position));
                    }

                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return mVideoList.isEmpty() ? 0 : mVideoList.size();
    }


    private class VideoPlayerViewHolder extends RecyclerView.ViewHolder {
        private ImageView dy_iv;
        private TextView dy_tv;

        VideoPlayerViewHolder(@NonNull View itemView) {
            super(itemView);
            dy_iv = itemView.findViewById(R.id.dy_iv);
            dy_tv = itemView.findViewById(R.id.dy_tv);


        }
    }
}
