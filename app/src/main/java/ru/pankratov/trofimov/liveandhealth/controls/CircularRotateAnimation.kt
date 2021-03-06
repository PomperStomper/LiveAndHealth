package ru.pankratov.trofimov.liveandhealth.controls

import android.content.Context
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation
import com.budiyev.android.circularprogressbar.CircularProgressBar
import ru.pankratov.trofimov.liveandhealth.BreathActivity
import ru.pankratov.trofimov.liveandhealth.MainActivity.MainObject.log
import kotlin.math.cos
import kotlin.math.sin

class CircularRotateAnimation(v: View, r: Float) : Animation() {
    private val view: View = v   // картинка
    private var cx = 0f
    private var cy = 0f             // центр
    private var prevX = 0f
    private var prevY = 0f          // предыдущее положение x,y изображения во время анимации
    private val radius: Float = r        // радиус круга

    private var prevDx = 0f
    private var prevDy = 0f

    var progress = 0F


    override fun willChangeBounds(): Boolean {
        return true
    }

    override fun initialize(width: Int, height: Int, parentWidth: Int, parentHeight: Int) {
        // рассчитываем положение центра изображения
        val cxImage = width / 2
        val cyImage = height / 2
        cx = (view.left + cxImage).toFloat()
        cy = (view.top + cyImage).toFloat()

        // устанавливаем предыдущее положение в центр
        prevX = cx
        prevY = cy
    }

    override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
        if (interpolatedTime == 0f) {
            t.matrix.setTranslate(prevDx, prevDy)
            return
        }
        val angleDeg = (interpolatedTime * 360f + 90) % 360
        val angleRad = Math.toRadians(angleDeg.toDouble()).toFloat()

        // r = радиус, cx и cy = центр точки, a = угол (радиана)
        val x = (cx + radius * cos(angleRad.toDouble())).toFloat()
        val y = (cy + radius * sin(angleRad.toDouble())).toFloat()
        val dx = prevX - x
        val dy = prevY - y
        prevX = x
        prevY = y
        prevDx = dx
        prevDy = dy

        // применяем анимацию
        t.matrix.setTranslate(dx, dy)

        progress = interpolatedTime * 100
    }

}