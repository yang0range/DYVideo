package com.yang.dyvideo.adapter

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.yang.dyvideo.R
import com.yang.dyvideo.data.Video
import com.yang.dyvideo.widget.FullWindowVideoView
import com.yang.dyvideo.widget.VideoLikeView

/**
 * @author yangzc
 * @data 2019/9/29 15:25
 * @desc
 */
class VideoPlayAdapter (private val mContext: Context, private val mVideoList: List<Video>): RecyclerView.Adapter<VideoPlayAdapter.VideoPlayAdapterViewHolder>() {
    private var onItemClickListener: VideoPlayAdapter.OnItemClickListener? = null

//    //点击事件的接口
//    interface OnItemClickListener {
//        fun onItemClick(view: View, position: Int, mVideo: Video)
//
//    }
//
//    fun setOnItemClickListener(listener: VideoAdapter.OnItemClickListener) {
//        this.onItemClickListener = listener
//    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)

    }

    override fun onBindViewHolder(mViewHolder: VideoPlayAdapterViewHolder, position: Int) {
        val mVideo = mVideoList[position]
        mViewHolder.iv_video_cover.setImageResource(mVideo.iamge)
        mViewHolder.surface_view.setVideoURI(Uri.parse(mVideo.videoplayer))
//        mViewHolder.surface_view.setVideoURI(Uri.parse("http://jzvd.nathen.cn/b201be3093814908bf987320361c5a73/2f6d913ea25941ffa78cc53a59025383-5287d2089db37e62345123a1be272f8b.mp4"))
        if (!mViewHolder.surface_view.isPlaying()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                mViewHolder.surface_view.setOnPreparedListener { mp ->
                    mp.setOnInfoListener(MediaPlayer.OnInfoListener { mp, what, extra ->
                        if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                            mp.isLooping = true
                            mViewHolder.iv_video_cover.animate().alpha(0f).setDuration(0).start()
                            mp.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT)

                            return@OnInfoListener true

                        }
                        false
                    })
                }
            }
            mViewHolder.surface_view.start()

        }


        setOnItemClick(mViewHolder)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int):VideoPlayAdapterViewHolder {
        return VideoPlayAdapterViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_video_play, viewGroup, false))
    }


    override fun getItemCount(): Int {
        return if (mVideoList.isEmpty()) 0 else mVideoList.size
    }

    protected fun setOnItemClick(holder: VideoPlayAdapterViewHolder) {
        if (onItemClickListener != null) {
            //为holder增加点击事件
            //为了保持插入和删除的position正确 不采用getview的position
            holder.itemView.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View) {
                    run {
                        val position = holder.getLayoutPosition()
                        onItemClickListener!!.onItemClick(holder.itemView, position)
                    }

                }
            })
        }
    }

    inner class VideoPlayAdapterViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
              internal  val  root_view : ConstraintLayout = itemView.findViewById(R.id.root_view)
              internal  val  surface_view : FullWindowVideoView = itemView.findViewById(R.id.surface_view)
              internal  val  iv_video_cover : ImageView = itemView.findViewById(R.id.iv_video_cover)
              internal  val  video_likelayout : VideoLikeView = itemView.findViewById(R.id.video_likelayout)
    }
}
