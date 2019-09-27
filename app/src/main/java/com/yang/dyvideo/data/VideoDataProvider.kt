package com.yang.dyvideo.data

import android.content.Context
import android.content.res.TypedArray

import com.yang.dyvideo.R

/**
 * @author yangzc
 * @data 2019/9/27 9:51
 * @desc  数据
 */
object VideoDataProvider {


    fun getVideoListImg(context: Context): TypedArray {
        return context.resources.obtainTypedArray(R.array.data_image)
    }

    fun getVideoListTitle(context: Context): TypedArray {
        return context.resources.obtainTypedArray(R.array.data_title)
    }

    fun getVideoPlayer(context: Context): TypedArray {
        return context.resources.obtainTypedArray(R.array.data_player)
    }


}
