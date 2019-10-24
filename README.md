#### 欢迎关注公共号
##### 关注公共号会有更多收获！
![](https://upload-images.jianshu.io/upload_images/3258163-82da30438838140c.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
# 高仿抖音播放（一）——需求详解
## 背景
**抖音**，可以说目前最火的短视频APP！作为一名 Android 开发，是时候研究一下功能是如何实现的了！  
目前，也有一些其他的小伙伴实现都有播放的效果，只要是用 **ViewPager** 来实现的上下滑动。 今天，我们换个思路用我们最常用的控件，**RecyclerView** 来实现，下面先看看需要实现的功能。



## 背景
**抖音**，可以说目前最火的短视频APP！作为一名 Android 开发，是时候研究一下功能是如何实现的了！  
目前，也有一些其他的小伙伴实现都有播放的效果，只要是用 **ViewPager** 来实现的上下滑动。 今天，我们换个思路用我们最常用的控件，**RecyclerView** 来实现，下面先看看需要实现的功能。

## 需求
打开抖音看一下，大致都有什么功能。

![image](https://github.com/yang0range/DYVideo/blob/master/one.gif)


这是抖音的播放页面，可以看到大致有以下几个功能。

**1. 上下滑动播放详情页**  
**2. 播放之后自动播放**  
**3. 双击点赞效果，并且点赞**  
**4. 单击暂停，再单击播放**

下面对比一下，我实现的效果
![image](https://github.com/yang0range/DYVideo/blob/master/three.gif)


基本上完成了我们我们预先想实现的需求。

**GitHub地址：https://github.com/yang0range/DYVideo**   

**欢迎star**！

如果需要想看看实现原理那就请继续看下一篇。


# **高仿抖音播放（二）——代码的实现**

**GitHub地址：https://github.com/yang0range/DYVideo**   
**欢迎star！**

## 需求的实现
上一篇，文章展示了实现的效果，并且陈列了我们要实现的功能。接下来通过两篇文章，详细的实现以下整个的过程。

### 代码详解
#### 准备工作
创建项目，因为播放视频要在网络播放，所以需要添加网络权限。

```
    <uses-permission android:name="android.permission.INTERNET" />
```
*该项目的代码主要是采用Kotlin写的，不太熟悉的同学自行转成Java*

**创建BaseAcivity**

```
package com.yang.dyvideo.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

/**
 * @author yangzc
 *	@data 2019/9/26 18:35
 *	@desc BaseAcivity
 *
 */
abstract class BaseAcivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId())
        initData()
        initView()
        start()
        initListener()
    }

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
```
**首页的列表页**

```
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

```
**首页的Adapter**
```
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

```

**实现需求——上下滑动播放详情页**


这几部分没有什么可说的，都是常规的实现方式。接下来，我们来看看最主要的播放的详情页的实现。


接下来，看看我们的详情页的布局.
```
<?xml version="1.0" encoding="utf-8"?>
<android.support.constraint.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="#2D2D33"
    tools:ignore="MissingConstraints,ContentDescription">

    <android.support.v7.widget.RecyclerView
        android:id="@+id/rlv_play_video"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:descendantFocusability="beforeDescendants" />


    <LinearLayout
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        app:layout_constraintBottom_toTopOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintTop_toBottomOf="parent">

        <ImageView
            android:id="@+id/iv_user_avatar"
            android:layout_width="50dp"
            android:layout_height="50dp"
            android:src="@mipmap/default_avater" />

        <ImageView
            android:id="@+id/iv_user_follow"
            android:layout_width="36dp"
            android:layout_height="36dp"
            android:layout_gravity="center_horizontal"
            android:layout_marginTop="20dp"
            android:src="@mipmap/ic_love_white" />

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_gravity="center_horizontal"
            android:layout_marginTop="10dp"
            android:text="@string/num"
            android:textColor="@color/colorWhite"
            android:textSize="14sp" />

        <ImageView
            android:layout_width="36dp"
            android:layout_height="36dp"
            android:layout_gravity="center_horizontal"
            android:layout_marginTop="15dp"
            android:src="@mipmap/ic_comment" />

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_gravity="center_horizontal"
            android:layout_marginTop="10dp"
            android:text="@string/num"
            android:textColor="@color/colorWhite"
            android:textSize="14sp" />

        <ImageView
            android:layout_width="36dp"
            android:layout_height="36dp"
            android:layout_gravity="center_horizontal"
            android:layout_marginTop="20dp"
            android:src="@mipmap/ic_share" />

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_gravity="center_horizontal"
            android:layout_marginTop="10dp"
            android:text="@string/num"
            android:textColor="@color/colorWhite"
            android:textSize="14sp" />
    </LinearLayout>


    <ImageView
        android:id="@+id/iv_video_play"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:background="@mipmap/ic_video_play"
        android:visibility="visible"
        app:layout_constraintBottom_toTopOf="parent"
        app:layout_constraintLeft_toRightOf="parent"
        app:layout_constraintRight_toLeftOf="parent"
        app:layout_constraintTop_toBottomOf="parent" />

</android.support.constraint.ConstraintLayout>
```
之前，介绍过我们这次采用的是 **RecyclerView**来实现的效果。  

详情的播放页，才用的是Android自带的VideoView,为了实现我们想要的全屏的效果，我们需要简单的自定义一下。
```
package com.yang.dyvideo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

public class FullWindowVideoView extends VideoView {

    public FullWindowVideoView(Context context) {
        super(context);
    }

    public FullWindowVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FullWindowVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


}

```
那接下来看一下Adapter的布局的实现
```
<?xml version="1.0" encoding="utf-8"?>
<android.support.constraint.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/root_view"
    android:layout_width="match_parent"
    android:layout_height="match_parent">


    <com.yang.dyvideo.widget.FullWindowVideoView
        android:id="@+id/surface_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />


    <ImageView
        android:id="@+id/iv_video_cover"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:clickable="false"
        android:focusable="false"
        android:scaleType="centerInside"
        android:visibility="visible" />




</android.support.constraint.ConstraintLayout>
```
在播放页面，我们添加了一个播放的按钮。  
再看看Adapter当中的代码
```
package com.yang.dyvideo.adapter

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.yang.dyvideo.R
import com.yang.dyvideo.data.Video
import com.yang.dyvideo.widget.FullWindowVideoView

/**
 * @author yangzc
 * @data 2019/9/29 15:25
 * @desc
 */
class VideoPlayAdapter (private val mContext: Context, private val mVideoList: List<Video>): RecyclerView.Adapter<VideoPlayAdapter.VideoPlayAdapterViewHolder>() {
    private var onItemClickListener: OnItemClickListener? = null


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
              internal  val  surface_view : FullWindowVideoView = itemView.findViewById(R.id.surface_view)
              internal  val  iv_video_cover : ImageView = itemView.findViewById(R.id.iv_video_cover)
    }
}


```
Adapter已经完成，接下来再看看如何自动实现上下滑动的时候  
我们需要自定一下**LayoutManager**，通过自定义 **LayoutManager** 来实现我们对于 **RecyclerView**的滑动监听
```
package com.yang.dyvideo.utils

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PagerSnapHelper
import android.support.v7.widget.RecyclerView
import android.view.View
import com.yang.dyvideo.presenter.OnViewPagerListener

/**
 * @author yangzc
 *	@data 2019/5/29 14:14
 *	@desc
 *
 */
class MyLayoutManager : LinearLayoutManager, RecyclerView.OnChildAttachStateChangeListener {


    constructor(context: Context) : super(context)

    constructor(context: Context, @RecyclerView.Orientation orientation: Int,
                reverseLayout: Boolean) : super(context, orientation, reverseLayout) {
        pagerSpaner = PagerSnapHelper()
    }

    var pagerSpaner: PagerSnapHelper? = null
    var viewPagerListener: OnViewPagerListener? = null
    var diffY = 0

    override fun onAttachedToWindow(view: RecyclerView) {
        super.onAttachedToWindow(view)
        view.addOnChildAttachStateChangeListener(this)
        pagerSpaner!!.attachToRecyclerView(view)
    }


    override fun onChildViewDetachedFromWindow(p0: View) {
        val position = getPosition(p0)
        if (0 < diffY) {
            viewPagerListener?.onPageRelease(true, position)
        } else {
            viewPagerListener?.onPageRelease(false, position)
        }
    }

    override fun onChildViewAttachedToWindow(p0: View) {

        val position = getPosition(p0)
        if (0 == position) {
            viewPagerListener?.onPageSelected(position, false)
        }


    }

    override fun onScrollStateChanged(state: Int) {
        if (RecyclerView.SCROLL_STATE_IDLE == state) {
            val view = pagerSpaner!!.findSnapView(this)
            val position = getPosition(view!!)
            viewPagerListener?.onPageSelected(position, position == itemCount - 1)
        }
        super.onScrollStateChanged(state)
    }

    fun setOnViewPagerListener(listener: OnViewPagerListener) {
        viewPagerListener = listener
    }

    override fun scrollVerticallyBy(dy: Int, recycler: RecyclerView.Recycler?, state: RecyclerView.State?): Int {
        diffY = dy
        return super.scrollVerticallyBy(dy, recycler, state)
    }
}
```
定义完成之后，需要实现播放和停止播放的两个方法，这两个方法相对就比较简单了，分别是

**releaseVideo**
```
   private fun releaseVideo(index: Int) {
        val itemView = rlv_play_video.getChildAt(index)
        val videoView = itemView.findViewById<FullWindowVideoView>(R.id.surface_view)
        val imgThumb = itemView.findViewById<ImageView>(R.id.iv_video_cover)
        videoView.stopPlayback()
        imgThumb.animate().alpha(1f).start()

    }
```
**playVideo**
```
    private fun playVideo(position: Int) {
        val itemView = rlv_play_video.getChildAt(position)
        val videoView = itemView.findViewById<FullWindowVideoView>(R.id.surface_view)
        val imgThumb = itemView.findViewById<ImageView>(R.id.iv_video_cover)
        val mediaPlayer = arrayOfNulls<MediaPlayer>(1)

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
```

结合我们之前自定义的滑动监听，就可以实现 **上下滑动播放详情页**的效果了
```
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

```

截止目前为止，我们需要实现的第一个需求 **上下滑动播放详情页**已经实现了。

# **高仿抖音播放（三）——细节的优化**
上一篇文章，我们着重实现了 **上下滑动播放详情页**， **播放之后自动播放**
接下来，我们要实现的就是 **双击点赞效果，并且点赞**， **单击暂停，再单击播放**这两个功能。

**GitHub地址：https://github.com/yang0range/DYVideo**   

**欢迎star**！

首先，实现以下双击点击的动画效果。  
这个动画，可以看到我们要实现的效果。

![image](https://github.com/yang0range/DYVideo/blob/master/two.gif)


可以看到，主要有以下几个功能点。  

**1. 缩放的动画**  
**2. 位移的动画**   
**3. 透明度的动画**  
**4. 还有一个点击的动作的监听**

梳理完功能点之后，就比较好实现对应的效果了。

三个动画效果，我们可以直接用**ObjectAnimator**这个类来实现。

点击效果，自然就涉及到了 **dispatchTouchEvent**

梳理完要实现的效果和方法之后，详细就看代码。

我们自定义了一个点赞的动画。


```
package com.yang.dyvideo.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.yang.dyvideo.R;
import com.yang.dyvideo.presenter.MyClickListener;

import java.util.Random;



/**
 * @author yangzc
 * @data 2019/5/31 13:13
 * @desc
 */
public class VideoLikeView extends RelativeLayout {
    private Context mContext;
    float[] num = {-30, -20, 0, 20, 30};//随机心形图片角度
    //记录上一次的点击时间
    private long  lastClickTime = 0;
    //点击的时间间隔
    private long INTERVAL = 200;
    private MyClickListener.MyClickCallBack onClickListener;

    public VideoLikeView(Context context) {
        super(context);
        initView(context);
    }

    public VideoLikeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public VideoLikeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                //获取点击时间
                long currTime = System.currentTimeMillis();
                //判断点击之间的时间差
                long  interval = currTime - lastClickTime;
                lastClickTime = currTime;
                if(interval <INTERVAL ){
                    final ImageView imageView = new ImageView(mContext);
                    //设置展示的位置，需要在手指触摸的位置上方，即触摸点是心形的右下角的位置
                    LayoutParams params = new LayoutParams(300, 300);
                    params.leftMargin = (int) event.getX() - 150;
                    params.topMargin = (int) event.getY() - 300;
                    //设置图片资源
                    imageView.setImageDrawable(getResources().getDrawable(R.mipmap.ic_heart));
                    imageView.setLayoutParams(params);
                    //把IV添加到父布局当中
                    addView(imageView);
                    //设置控件的动画
                    AnimatorSet animatorSet = new AnimatorSet();
                    //缩放动画，X轴2倍缩小至0.9倍
                    animatorSet.play(scale(imageView, "scaleX", 2f, 0.9f, 100, 0))
                            //缩放动画，Y轴2倍缩放至0.9倍
                            .with(scale(imageView, "scaleY", 2f, 0.9f, 100, 0))
                            //旋转动画，随机旋转角
                            .with(rotation(imageView, 0, 0, num[new Random().nextInt(4)]))
                            //渐变透明动画，透明度从0-1
                            .with(alpha(imageView, 0, 1, 100, 0))
                            //缩放动画，X轴0.9倍缩小至
                            .with(scale(imageView, "scaleX", 0.9f, 1, 50, 150))
                            //缩放动画，Y轴0.9倍缩放至
                            .with(scale(imageView, "scaleY", 0.9f, 1, 50, 150))
                            //位移动画，Y轴从0上移至600
                            .with(translationY(imageView, 0, -600, 800, 400))
                            //透明动画，从1-0
                            .with(alpha(imageView, 1, 0, 300, 400))
                            //缩放动画，X轴1至3倍
                            .with(scale(imageView, "scaleX", 1, 3f, 700, 400))
                            //缩放动画，Y轴1至3倍
                            .with(scale(imageView, "scaleY", 1, 3f, 700, 400));
                    //开始动画
                    animatorSet.start();
                    //设置动画结束监听
                    animatorSet.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            //当动画结束以后，需要把控件从父布局移除
                            removeViewInLayout(imageView);
                        }
                    });
                }
                break;
        }
        return super.dispatchTouchEvent(event);
    }
    /**
     * 缩放动画
     * @param view
     * @param propertyName
     * @param from
     * @param to
     * @param time
     * @param delayTime
     * @return
     */
    public static ObjectAnimator scale(View view, String propertyName, float from, float to, long time, long delayTime) {
        ObjectAnimator translation = ObjectAnimator.ofFloat(view
                , propertyName
                , from, to);
        translation.setInterpolator(new LinearInterpolator());
        translation.setStartDelay(delayTime);
        translation.setDuration(time);
        return translation;
    }
    /**
     * 位移动画
     * @param view
     * @param from
     * @param to
     * @param time
     * @param delayTime
     * @return
     */
    public static ObjectAnimator translationX(View view, float from, float to, long time, long delayTime) {
        ObjectAnimator translation = ObjectAnimator.ofFloat(view
                , "translationX"
                , from, to);
        translation.setInterpolator(new LinearInterpolator());
        translation.setStartDelay(delayTime);
        translation.setDuration(time);
        return translation;
    }
    public static ObjectAnimator translationY(View view, float from, float to, long time, long delayTime) {
        ObjectAnimator translation = ObjectAnimator.ofFloat(view
                , "translationY"
                , from, to);
        translation.setInterpolator(new LinearInterpolator());
        translation.setStartDelay(delayTime);
        translation.setDuration(time);
        return translation;
    }
    /**
     * 透明度动画
     * @param view
     * @param from
     * @param to
     * @param time
     * @param delayTime
     * @return
     */
    public static ObjectAnimator alpha(View view, float from, float to, long time, long delayTime) {
        ObjectAnimator translation = ObjectAnimator.ofFloat(view
                , "alpha"
                , from, to);
        translation.setInterpolator(new LinearInterpolator());
        translation.setStartDelay(delayTime);
        translation.setDuration(time);
        return translation;
    }
    public static ObjectAnimator rotation(View view, long time, long delayTime, float... values) {
        ObjectAnimator rotation = ObjectAnimator.ofFloat(view, "rotation", values);
        rotation.setDuration(time);
        rotation.setStartDelay(delayTime);
        rotation.setInterpolator(new TimeInterpolator() {
            @Override
            public float getInterpolation(float input) {
                return input;
            }
        });
        return rotation;
    }
    public void setOnClickListener(MyClickListener.MyClickCallBack onClickListener) {
        this.onClickListener = onClickListener;
    }
    public MyClickListener.MyClickCallBack getOnClickListener() {
        return onClickListener;
    }
}

```

接下来，我们就要实现**单击暂停、播放的功能**

因为这个功能也涉及到了的点击，所以，我们就需要区分一下在屏幕上是双击，还是单击。

双击就是点赞的动画，单击就是播放、暂停的功能。

在这里，采用的**Handler**的**postDelayed**方法，来区分两次点击之间的延时点击时间**timeout**，同时设计一个连续点击的计数器**clickCount**来记录在**timeout**的时间里的点击次数。**clickCount**为1则是播放、暂停的方法，如果 **clickCount**为2 则为点赞的效果。

理清了思路，接下来我们看一下实现的代码。

```
  mAdapter!!.setOnItemClickListener(object : VideoPlayAdapter.OnItemClickListener {
             var timeout = 500//双击间百毫秒延时
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
```
至此，我们要实现的整个功能都完成了。

欢迎 *“点赞”、“关注”*。

如果要有任何问题、BUG欢迎给我留言。








