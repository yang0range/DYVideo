package com.yang.dyvideo.activity

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.yang.dyvideo.R
import com.yang.dyvideo.adapter.VideoAdapter
import com.yang.dyvideo.data.Video
import com.yang.dyvideo.data.VideoDataProvider
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : BaseAcivity() {
    private var videolist: MutableList<Video>? = null
    private var mAdapter: VideoAdapter? = null


    override fun initListener() {
        mAdapter?.let {
            it.setOnItemClickListener(object : VideoAdapter.OnItemClickListener {
                override fun onItemClick(view: View, position: Int, mVideo: Video) {
                    startActivity(VideoPlayActivity.buildIntent(this@MainActivity, position, videolist as ArrayList<Video>?))
                }

            })

        }

    }
        override fun initData() {
            videolist = ArrayList()

            for (i in 0 until 15) {
                val mVideo = Video()
                mVideo.iamge = VideoDataProvider.getVideoListImg(this).getResourceId(i, R.mipmap.one)
                mVideo.title = VideoDataProvider.getVideoListTitle(this).getString(i)
                mVideo.videoplayer = VideoDataProvider.getVideoPlayer(this).getString(i)
                (videolist as ArrayList<Video>).add(mVideo)
            }


        }

        override fun initView() {

        }

        override fun start() {
            mAdapter = videolist?.let { VideoAdapter(this, it) }
            rlv.adapter = mAdapter
            val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            rlv.layoutManager = linearLayoutManager
        }


        override fun layoutId(): Int {
            return R.layout.activity_main
        }
    }
