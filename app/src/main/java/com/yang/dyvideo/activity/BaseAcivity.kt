package com.yang.dyvideo.activity

import android.support.v7.app.AppCompatActivity

/**
 * @author yangzc
 *	@data 2019/9/26 18:35
 *	@desc BaseAcivity
 *
 */
abstract class BaseAcivity : AppCompatActivity() {
    abstract fun initListener()

    /**
     * 初始化数据
     */
    abstract fun initData()

    /**
     * 初始化 View
     */
    abstract fun initView()

    /**
     * 开始请求
     */
    abstract fun start()

    /**
     *  加载布局
     */
    open fun layoutId(): Int {
        return 0
    }


}