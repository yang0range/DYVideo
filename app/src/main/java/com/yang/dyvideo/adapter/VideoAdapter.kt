package com.yang.dyvideo.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.yang.dyvideo.R
import com.yang.dyvideo.data.Video

/**
 * @author yangzc
 * @data 2019/9/27 11:17
 * @desc
 */
 class VideoAdapter(private val mContext: Context, private val mVideoList: List<Video>) : RecyclerView.Adapter<VideoAdapter.VideoPlayerViewHolder>() {


    private var onItemClickListener: OnItemClickListener? = null

    //点击事件的接口
    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int, mVideo: Video)

    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): VideoPlayerViewHolder {
        return VideoPlayerViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_video_list, viewGroup, false))
    }

    override fun onBindViewHolder(ViewHolder: VideoPlayerViewHolder, position: Int) {
        val mVideo = mVideoList[position]
        ViewHolder.dy_iv.setImageResource(mVideo.iamge)
        ViewHolder.dy_tv.text = mVideo.title
        setOnItemClick(ViewHolder)

    }


    private fun setOnItemClick(holder: VideoPlayerViewHolder) {
        if (this.onItemClickListener != null) {
            holder.itemView.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View) {
                    run {
                        val position = holder.layoutPosition
                        this@VideoAdapter.onItemClickListener!!.onItemClick(holder.itemView, position, mVideoList[position])
                    }

                }
            })
        }
    }


    override fun getItemCount(): Int {
        return if (mVideoList.isEmpty()) 0 else mVideoList.size
    }


    inner class VideoPlayerViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val dy_iv: ImageView = itemView.findViewById(R.id.dy_iv)
        internal val dy_tv: TextView = itemView.findViewById(R.id.dy_tv)

    }
}
