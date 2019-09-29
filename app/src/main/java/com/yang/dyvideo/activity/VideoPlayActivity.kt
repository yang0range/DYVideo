package com.yang.dyvideo.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.GestureDetector
import com.yang.dyvideo.R
import com.yang.dyvideo.data.Video
import java.util.*

class VideoPlayActivity : BaseAcivity() {
    private var mGestureDetector: GestureDetector? = null
    //    private var mAdapter: PalyVideoAdapter? = null
    private var videolist: MutableList<Video>? = null
    private var pos: Int = 0

    override fun initListener() {

    }

    override fun initData() {
        intent?.let {
            videolist = it.getSerializableExtra(INTENT_EXTAL_VIDEO_LIST_KEY) as MutableList<Video>
            pos = it.getIntExtra(INTENT_EXTAL_VIDEO_LIST_POS, 0)
        }
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
