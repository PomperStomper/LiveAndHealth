package ru.pankratov.trofimov.liveandhealth.controls

import android.content.Context
import android.util.AttributeSet
import android.widget.VideoView


class VideoViewCustom : VideoView {
    private var mForceHeight = 0
    private var mForceWidth = 0

    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
    }

    fun setDimensions(w: Int, h: Int) {
        mForceHeight = h
        mForceWidth = w
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        Log.i("@@@@", "onMeasure")
        setMeasuredDimension(mForceWidth, mForceHeight)
    }
}