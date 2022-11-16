package ru.pankratov.trofimov.liveandhealth.controls

import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.*
import android.widget.SeekBar.OnSeekBarChangeListener
import android.content.Context
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.SeekBar
import android.widget.TextView
import ru.pankratov.trofimov.liveandhealth.R
import java.lang.ref.WeakReference
import java.util.Formatter
import java.util.Locale


class VideoControllerView : FrameLayout {
    private var mPlayer: MediaPlayerControl? = null
    private var mContext: Context
    private var mAnchor: ViewGroup? = null
    private var mRoot: View? = null
    private var mProgress: ProgressBar? = null
    private var mEndTime: TextView? = null
    private var mCurrentTime: TextView? = null
    var isShowing = false
        private set
    private var mDragging = false
    private var mUseFastForward: Boolean
    private var mFromXml = false
    private var mListenersSet = false
    private var mNextListener: OnClickListener? = null
    private var mPrevListener: OnClickListener? = null
    var mFormatBuilder: StringBuilder? = null
    var mFormatter: Formatter? = null
    private var mPauseButton: ImageButton? = null
    private var mFfwdButton: ImageButton? = null
    private var mRewButton: ImageButton? = null
    private var mNextButton: ImageButton? = null
    private var mPrevButton: ImageButton? = null
    private var mFullscreenButton: ImageButton? = null
    private val mHandler: Handler = MessageHandler(this)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        mRoot = null
        mContext = context
        mUseFastForward = true
        mFromXml = true
        Log.i(TAG, TAG)
    }

    constructor(context: Context?, useFastForward: Boolean) : super(context!!) {
        mContext = context
        mUseFastForward = useFastForward
        Log.i(TAG, TAG)
    }

    constructor(context: Context?) : this(context, true) {
        Log.i(TAG, TAG)
    }

    public override fun onFinishInflate() {
        super.onFinishInflate()
        if (mRoot != null) initControllerView(mRoot)
    }

    fun setMediaPlayer(player: MediaPlayerControl?) {
        mPlayer = player
        updatePausePlay()
        updateFullScreen()
    }

    /**
     * Устанавливает якорь на ту вьюху на которую вы хотите разместить контролы
     * Это может быть например VideoView или SurfaceView
     */
    fun setAnchorView(view: ViewGroup?) {
        mAnchor = view
        val frameParams = LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        removeAllViews()
        val v: View? = makeControllerView()
        addView(v, frameParams)
    }

    /**
     * Создает вьюху которая будет находится поверх вашего VideoView или другого контролла
     */
    protected fun makeControllerView(): View? {
        val inflate = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        mRoot = inflate.inflate(R.layout.media_controller, null)
        initControllerView(mRoot)
        return mRoot
    }

    private fun initControllerView(v: View?) {
        mPauseButton = v?.findViewById(R.id.pause)
        if (mPauseButton != null) {
            mPauseButton!!.requestFocus()
            mPauseButton!!.setOnClickListener(mPauseListener)
        }
        mFullscreenButton = v?.findViewById(R.id.fullscreen)
        if (mFullscreenButton != null) {
            mFullscreenButton!!.requestFocus()
            mFullscreenButton!!.setOnClickListener(mFullscreenListener)
        }
        mFfwdButton = v?.findViewById(R.id.ffwd)
        if (mFfwdButton != null) {
            mFfwdButton!!.setOnClickListener(mFfwdListener)
            if (!mFromXml) {
                mFfwdButton!!.visibility = if (mUseFastForward) View.VISIBLE else View.GONE
            }
        }
        mRewButton = v?.findViewById(R.id.rew)
        if (mRewButton != null) {
            mRewButton!!.setOnClickListener(mRewListener)
            if (!mFromXml) {
                mRewButton!!.visibility = if (mUseFastForward) View.VISIBLE else View.GONE
            }
        }

        // По дефолту вьюха будет спрятана, и показываться будет после события onTouch()
        mNextButton = v?.findViewById(R.id.next)
        if (mNextButton != null && !mFromXml && !mListenersSet) {
            mNextButton!!.visibility = View.GONE
        }
        mPrevButton = v?.findViewById(R.id.prev)
        if (mPrevButton != null && !mFromXml && !mListenersSet) {
            mPrevButton!!.visibility = View.GONE
        }
        mProgress = v?.findViewById(R.id.mediacontroller_progress)
        if (mProgress != null) {
            if (mProgress is SeekBar) {
                val seeker = mProgress as SeekBar
                seeker.setOnSeekBarChangeListener(mSeekListener)
            }
            mProgress!!.max = 1000
        }
        mEndTime = v?.findViewById(R.id.time)
        mCurrentTime = v?.findViewById(R.id.time_current)
        mFormatBuilder = StringBuilder()
        mFormatter = Formatter(mFormatBuilder, Locale.getDefault())
        installPrevNextListeners()
    }

    /**
     * Отключить паузу или seek button, если поток не может быть приостановлена
     * Это требует интерфейс управления MediaPlayerControlExt
     */
    private fun disableUnsupportedButtons() {
        if (mPlayer == null) {
            return
        }
        try {
            if (mPauseButton != null && !mPlayer!!.canPause()) {
                mPauseButton!!.isEnabled = false
            }
            if (mRewButton != null && !mPlayer!!.canSeekBackward()) {
                mRewButton!!.isEnabled = false
            }
            if (mFfwdButton != null && !mPlayer!!.canSeekForward()) {
                mFfwdButton!!.isEnabled = false
            }
        } catch (ex: IncompatibleClassChangeError) {
            //выводите в лог что хотите из ex
        }
    }
    /**
     * Показывает контроллы на экране
     * Он исчезнет автоматически после своего таймаута
     */
    /**
     * Показывает контроллер на экране
     * Он будет убран через 3 секуунды
     */
    @JvmOverloads
    fun show(timeout: Int = sDefaultTimeout) {
        if (!isShowing && mAnchor != null) {
            setProgress()
            if (mPauseButton != null) {
                mPauseButton!!.requestFocus()
            }
            disableUnsupportedButtons()
            val tlp = LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.BOTTOM
            )
            mAnchor!!.addView(this, tlp)
            isShowing = true
        }
        updatePausePlay()
        updateFullScreen()
        mHandler.sendEmptyMessage(SHOW_PROGRESS)
        val msg: Message = mHandler.obtainMessage(FADE_OUT)
        if (timeout != 0) {
            mHandler.removeMessages(FADE_OUT)
            mHandler.sendMessageDelayed(msg, timeout.toLong())
        }
    }

    /**
     * Удаляем контроллы с экрана.
     */
    fun hide() {
        if (mAnchor == null) {
            return
        }
        try {
            mAnchor!!.removeView(this)
            mHandler.removeMessages(SHOW_PROGRESS)
        } catch (ex: IllegalArgumentException) {
            Log.w("MediaController", "already removed")
        }
        isShowing = false
    }

    private fun stringForTime(timeMs: Int): String {
        val totalSeconds = timeMs / 1000
        val seconds = totalSeconds % 60
        val minutes = totalSeconds / 60 % 60
        val hours = totalSeconds / 3600
        mFormatBuilder!!.setLength(0)
        return if (hours > 0) {
            mFormatter?.format("%d:%02d:%02d", hours, minutes, seconds).toString()
        } else {
            mFormatter?.format("%02d:%02d", minutes, seconds).toString()
        }
    }

    private fun setProgress(): Int {
        if (mPlayer == null || mDragging) {
            return 0
        }
        val position = mPlayer!!.currentPosition
        val duration = mPlayer!!.duration
        if (mProgress != null) {
            if (duration > 0) {
                // use long to avoid overflow
                val pos = 1000L * position / duration
                mProgress!!.progress = pos.toInt()
            }
            val percent = mPlayer!!.bufferPercentage
            mProgress!!.secondaryProgress = percent * 10
        }
        if (mEndTime != null) mEndTime!!.text = stringForTime(duration)
        if (mCurrentTime != null) mCurrentTime!!.text = stringForTime(position)
        return position
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        show(sDefaultTimeout)
        return true
    }

    override fun onTrackballEvent(ev: MotionEvent): Boolean {
        show(sDefaultTimeout)
        return false
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if (mPlayer == null) {
            return true
        }
        val keyCode: Int = event.keyCode
        val uniqueDown = (event.getRepeatCount() === 0
                && event.getAction() === KeyEvent.ACTION_DOWN)
        if (keyCode == KeyEvent.KEYCODE_HEADSETHOOK || keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE || keyCode == KeyEvent.KEYCODE_SPACE) {
            if (uniqueDown) {
                doPauseResume()
                show(sDefaultTimeout)
                if (mPauseButton != null) {
                    mPauseButton!!.requestFocus()
                }
            }
            return true
        } else if (keyCode == KeyEvent.KEYCODE_MEDIA_PLAY) {
            if (uniqueDown && !mPlayer!!.isPlaying) {
                mPlayer!!.start()
                updatePausePlay()
                show(sDefaultTimeout)
            }
            return true
        } else if (keyCode == KeyEvent.KEYCODE_MEDIA_STOP
            || keyCode == KeyEvent.KEYCODE_MEDIA_PAUSE
        ) {
            if (uniqueDown && mPlayer!!.isPlaying) {
                mPlayer!!.pause()
                updatePausePlay()
                show(sDefaultTimeout)
            }
            return true
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN || keyCode == KeyEvent.KEYCODE_VOLUME_UP || keyCode == KeyEvent.KEYCODE_VOLUME_MUTE) {
            return super.dispatchKeyEvent(event)
        } else if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_MENU) {
            if (uniqueDown) {
                hide()
            }
            return true
        }
        show(sDefaultTimeout)
        return super.dispatchKeyEvent(event)
    }

    private val mPauseListener: OnClickListener = OnClickListener {
        doPauseResume()
        show(sDefaultTimeout)
    }
    private val mFullscreenListener: OnClickListener = OnClickListener {
        doToggleFullscreen()
        show(sDefaultTimeout)
    }

    fun updatePausePlay() {
        if (mRoot == null || mPauseButton == null || mPlayer == null) {
            return
        }
        if (mPlayer!!.isPlaying) {
            mPauseButton!!.setImageResource(R.drawable.vidoepause)
        } else {
            mPauseButton!!.setImageResource(R.drawable.videoplay)
        }
    }

    fun updateFullScreen() {
        if (mRoot == null || mFullscreenButton == null || mPlayer == null) {
            return
        }
        if (mPlayer!!.isFullScreen) {
            mFullscreenButton!!.setImageResource(R.drawable.videofullscreen)
        } else {
            mFullscreenButton!!.setImageResource(R.drawable.videofullscreen)
        }
    }

    private fun doPauseResume() {
        if (mPlayer == null) {
            return
        }
        if (mPlayer!!.isPlaying) {
            mPlayer!!.pause()
        } else {
            mPlayer!!.start()
        }
        updatePausePlay()
    }

    private fun doToggleFullscreen() {
        if (mPlayer == null) {
            return
        }
        mPlayer!!.toggleFullScreen()
    }

    private val mSeekListener: OnSeekBarChangeListener = object : OnSeekBarChangeListener {
        override fun onStartTrackingTouch(bar: SeekBar) {
            show(3600000)
            mDragging = true
            mHandler.removeMessages(SHOW_PROGRESS)
        }

        override fun onProgressChanged(bar: SeekBar, progress: Int, fromuser: Boolean) {
            if (mPlayer == null) {
                return
            }
            if (!fromuser) {
                return
            }
            val duration = mPlayer!!.duration.toLong()
            val newposition = duration * progress / 1000L
            mPlayer!!.seekTo(newposition.toInt())
            if (mCurrentTime != null) mCurrentTime!!.text = stringForTime(newposition.toInt())
        }

        override fun onStopTrackingTouch(bar: SeekBar) {
            mDragging = false
            setProgress()
            updatePausePlay()
            show(sDefaultTimeout)
            mHandler.sendEmptyMessage(SHOW_PROGRESS)
        }
    }

    override fun setEnabled(enabled: Boolean) {
        if (mPauseButton != null) {
            mPauseButton!!.isEnabled = enabled
        }
        if (mFfwdButton != null) {
            mFfwdButton!!.isEnabled = enabled
        }
        if (mRewButton != null) {
            mRewButton!!.isEnabled = enabled
        }
        if (mNextButton != null) {
            mNextButton!!.isEnabled = enabled && mNextListener != null
        }
        if (mPrevButton != null) {
            mPrevButton!!.isEnabled = enabled && mPrevListener != null
        }
        if (mProgress != null) {
            mProgress!!.isEnabled = enabled
        }
        disableUnsupportedButtons()
        super.setEnabled(enabled)
    }

    private val mRewListener: View.OnClickListener = OnClickListener {
        if (mPlayer == null) {
            return@OnClickListener
        }
        var pos = mPlayer!!.currentPosition
        pos -= 5000 // милисекунд
        mPlayer!!.seekTo(pos)
        setProgress()
        show(sDefaultTimeout)
    }
    private val mFfwdListener: View.OnClickListener = OnClickListener {
        if (mPlayer == null) {
            return@OnClickListener
        }
        var pos = mPlayer!!.currentPosition
        pos += 15000 // милисекунд
        mPlayer!!.seekTo(pos)
        setProgress()
        show(sDefaultTimeout)
    }

    private fun installPrevNextListeners() {
        if (mNextButton != null) {
            mNextButton!!.setOnClickListener(mNextListener)
            mNextButton!!.isEnabled = mNextListener != null
        }
        if (mPrevButton != null) {
            mPrevButton!!.setOnClickListener(mPrevListener)
            mPrevButton!!.isEnabled = mPrevListener != null
        }
    }

    fun setPrevNextListeners(next: View.OnClickListener?, prev: View.OnClickListener?) {
        mNextListener = next
        mPrevListener = prev
        mListenersSet = true
        if (mRoot != null) {
            installPrevNextListeners()
            if (mNextButton != null && !mFromXml) {
                mNextButton!!.visibility = View.VISIBLE
            }
            if (mPrevButton != null && !mFromXml) {
                mPrevButton!!.visibility = View.VISIBLE
            }
        }
    }

    interface MediaPlayerControl {
        fun start()
        fun pause()
        val duration: Int
        val currentPosition: Int

        fun seekTo(pos: Int)
        val isPlaying: Boolean
        val bufferPercentage: Int

        fun canPause(): Boolean
        fun canSeekBackward(): Boolean
        fun canSeekForward(): Boolean
        val isFullScreen: Boolean

        fun toggleFullScreen()
    }

    private class MessageHandler internal constructor(view: VideoControllerView?) : Handler() {
        private val mView: WeakReference<VideoControllerView> = WeakReference<VideoControllerView>(view)
        override fun handleMessage(msg: Message) {
            var msg: Message = msg
            val view: VideoControllerView? = mView.get()
            if (view?.mPlayer == null) {
                return
            }
            val pos: Int
            when (msg.what) {
                FADE_OUT -> view.hide()
                SHOW_PROGRESS -> {
                    pos = view.setProgress()
                    if (!view.mDragging && view.isShowing && view.mPlayer!!.isPlaying) {
                        msg = obtainMessage(SHOW_PROGRESS)
                        sendMessageDelayed(msg, (1000 - pos % 1000).toLong())
                    }
                }
            }
        }

    }

    companion object {
        private const val TAG = "VideoControllerView"
        private const val sDefaultTimeout = 3000
        private const val FADE_OUT = 1
        private const val SHOW_PROGRESS = 2
    }
}