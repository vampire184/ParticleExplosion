package com.yangzhenyu.particleexplosion

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import androidx.core.animation.addListener
import androidx.core.content.ContextCompat
import com.yangzhenyu.particleexplosion.model.Particle
import java.nio.channels.FileLock
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.pow
import kotlin.random.Random

/**
 * TODO: document your custom view class.
 */
class ParticleView : View {

    private lateinit var mPaint:Paint
    //像素球直径
    private val mDiameter = 15
    //像素球数组
    private var mBallList:ArrayList<Particle> = ArrayList()
    //图片宽度
    private var mBitmapWidth:Int = 0
    //图片高度
    private var mBitmapHeight:Int = 0
    private val mG:Float = 5f
    private lateinit var mAnimation:ValueAnimator
    private val mDuration:Long = 2000L
    private lateinit var mBitmap: Bitmap
    private var isAnimate:Boolean = false

    constructor(context: Context) : this(context,null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs,0)

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        init()
    }

    private fun init() {
        mPaint = Paint()
        mPaint.isAntiAlias = true

        mBitmap = BitmapFactory.decodeResource(resources,R.mipmap.a)
        mBitmapWidth = mBitmap.width
        mBitmapHeight = mBitmap.height

        //遍历图片像素，步长为一个像素球的直径
        createParticleList()

        mAnimation = ValueAnimator.ofFloat(0.0f,1.0f)
        mAnimation.interpolator = LinearInterpolator()
        mAnimation.duration = mDuration
        mAnimation.addUpdateListener {
            val value = it.animatedValue as Float
            updateParticleList(value)
        }
        mAnimation.addListener(onEnd = {
            isAnimate = false
            createParticleList()
            invalidate()
        })
    }

    private fun createParticleList(){
        mBallList.clear()
        //遍历图片像素，步长为一个像素球的直径
        for (i in 0 until mBitmapWidth step mDiameter){
            for (j in 0 until mBitmapHeight step mDiameter){
                val speedX = generateSpeedX()
                val speedY = generateSpeedY()
                val particle = Particle(
                        i+mDiameter/2.0f,
                        j+mDiameter/2.0f,
                        mDiameter/2.0f,
                        mBitmap.getPixel(i,j),
                        speedX,
                        speedY)
                mBallList.add(particle)
            }
        }
    }

    private fun generateSpeedX():Float{
        return ((-1.0).pow(ceil((Random.nextInt(2)+1).toDouble())) *10*Math.random()).toFloat()
    }

    private fun generateSpeedY():Float{
        return (Math.random()*30).toFloat()
    }

    private fun updateParticleList(value :Float){
        for (particle in mBallList){
            particle.x +=particle.speedX
            particle.y +=(particle.speedY+mG)
        }
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.translate((width-mBitmapWidth)/2.0f,(height-mBitmapHeight)/3.0f)
        if(isAnimate){
            for (particle in mBallList){
                mPaint.color = particle.color
                canvas.drawCircle(particle.x,particle.y,particle.radius,mPaint)
            }
        }else{
            canvas.drawBitmap(mBitmap,0.0f,0.0f,mPaint)
        }

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event!!.action){
            MotionEvent.ACTION_DOWN -> {
                isAnimate = true
                mAnimation.start()
            }
        }
        return true
    }
}
