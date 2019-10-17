package com.yang.dyvideo.activity

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.view.MotionEvent
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


        mAdapter!!.setOnItemClickListener(object : VideoPlayAdapter.OnItemClickListener {
             var timeout = 500//双击间吾百毫秒延时
             var clickCount = 0//记录连续点击次数
             var handler = Handler()

            override fun onItemClick(view: View, position: Int) {
                clickCount++
                val mini_surface_view = view.findViewById<FullWindowVideoView>(R.id.surface_view)
                view.setOnTouchListener { v, event ->
                    if (event.action == MotionEvent.ACTION_DOWN) {
                        handler.postDelayed({
                            if (clickCount == 1) {
                                if (mini_surface_view.isPlaying()) {
                                    mini_surface_view.pause()
                                    iv_video_play.setVisibility(View.VISIBLE)
                                } else {
                                    iv_video_play.setVisibility(View.GONE)
                                    val mediaPlayer = arrayOfNulls<MediaPlayer>(1)
                                    mini_surface_view.setOnPreparedListener(MediaPlayer.OnPreparedListener { })
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                        mini_surface_view.setOnInfoListener(MediaPlayer.OnInfoListener { mp, what, extra ->
                                            mediaPlayer[0] = mp
                                            mp.isLooping = true
                                            mp.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT)
                                            false
                                        })
                                    }
                                    mini_surface_view.start()
                                }
                            } else if (clickCount >= 2) {
                                if (!videolist?.get(position)!!.like) {
                                    praiseMethod(position)
                                }
                            }
                            handler.removeCallbacksAndMessages(null)
                            //清空handler延时，并防内存泄漏
                            clickCount = 0//计数清零
                        }, timeout.toLong())//延时timeout后执行run方法中的代码
                    }
                    false
                }
            }
        })




    }

    private fun praiseMethod(position: Int) {
        iv_user_follow.setImageResource(R.mipmap.ic_love_red)

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
        videoView.setOnPreparedListener({ })
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            videoView.setOnInfoListener { mp, what, extra ->
                mediaPlayer[0] = mp
                mp.isLooping = true
                imgThumb.animate().alpha(0f).start()
                mp.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT)
                false
            }
        }
        videoView.start()

        if (iv_video_play.visibility == View.VISIBLE) {
            iv_video_play.visibility = View.GONE
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
