package com.yang.dyvideo.activity

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import com.yang.dyvideo.R
import com.yang.dyvideo.adapter.VideoPlayAdapter
import com.yang.dyvideo.data.Video
import com.yang.dyvideo.presenter.OnViewPagerListener
import com.yang.dyvideo.utils.MyLayoutManager
import com.yang.dyvideo.widget.FullWindowVideoView
import kotlinx.android.synthetic.main.activity_video_play.*
import java.util.*

class VideoPlayActivity : BaseAcivity() {
    private var videolist: MutableList<Video>? = null
    private var mAdapter: VideoPlayAdapter? = null
    private var pos: Int = 0
    private var mVideo: Video? = null

    private var myLayoutManager: MyLayoutManager? = null

    override fun initListener() {
        rlv_play_video.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lastItem = (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                mVideo = videolist?.get(lastItem)
            }
        })

        rlv_play_video.scrollToPosition(pos)
        this.myLayoutManager?.setOnViewPagerListener(object : OnViewPagerListener {
            override fun onInitComplete() {

            }

            override fun onPageRelease(isNext: Boolean, position: Int) {
                var index: Int
                index = if (isNext) {
                    0
                } else {
                    1
                }
                releaseVideo(index)
            }

            override fun onPageSelected(position: Int, bottom: Boolean) {
                playVideo(0)
            }
        })
    }


    private fun releaseVideo(index: Int) {
        val itemView = rlv_play_video.getChildAt(index)
        val videoView = itemView.findViewById<FullWindowVideoView>(R.id.surface_view)
        val imgThumb = itemView.findViewById<ImageView>(R.id.iv_video_cover)
        videoView.stopPlayback()
        imgThumb.animate().alpha(1f).start()

    }


    private fun playVideo(position: Int) {
        val itemView = rlv_play_video.getChildAt(position)
        val videoView = itemView.findViewById<FullWindowVideoView>(R.id.surface_view)
        val imgThumb = itemView.findViewById<ImageView>(R.id.iv_video_cover)
        val mediaPlayer = arrayOfNulls<MediaPlayer>(1)
        videoView.setOnPreparedListener(MediaPlayer.OnPreparedListener { })
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            videoView.setOnInfoListener(MediaPlayer.OnInfoListener { mp, what, extra ->
                mediaPlayer[0] = mp
                mp.isLooping = true
                imgThumb.animate().alpha(0f).start()
                mp.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT)
                false
            })
        }
        videoView.start()

        if (iv_video_play.getVisibility() == View.VISIBLE) {
            iv_video_play.setVisibility(View.GONE)
        }
    }


    override fun initData() {
        intent?.let {
            videolist = it.getSerializableExtra(INTENT_EXTAL_VIDEO_LIST_KEY) as MutableList<Video>
            pos = it.getIntExtra(INTENT_EXTAL_VIDEO_LIST_POS, 0)
        }

        myLayoutManager = MyLayoutManager(this, OrientationHelper.VERTICAL, false);
        rlv_play_video.layoutManager = myLayoutManager
        mAdapter = videolist?.let { VideoPlayAdapter(this, it) }
        rlv_play_video.adapter = mAdapter
    }

    override fun initView() {
    }

    override fun start() {


    }


    override fun layoutId(): Int {
        return R.layout.activity_video_play
    }

    companion object {
        private const val INTENT_EXTAL_VIDEO_LIST_KEY = "intent_extal_video_list_key"
        private const val INTENT_EXTAL_VIDEO_LIST_POS = "intent_extal_video_list_pos"

        fun buildIntent(context: Context, postion: Int, list: ArrayList<Video>?): Intent {
            val intent = Intent(context, VideoPlayActivity::class.java)
            val bundle = Bundle()
            bundle.putSerializable(INTENT_EXTAL_VIDEO_LIST_KEY, list)
            bundle.putInt(INTENT_EXTAL_VIDEO_LIST_POS, postion)
            intent.putExtras(bundle)
            return intent
        }
    }


}
