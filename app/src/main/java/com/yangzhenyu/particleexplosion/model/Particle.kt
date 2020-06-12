package com.yangzhenyu.particleexplosion.model

/**
 * x:x坐标
 * y:y坐标
 * radius:半径
 * color:颜色
 * speedX:x方向的速度
 */
data class Particle(var x:Float,var y:Float, val radius:Float, val color:Int, val speedX:Float,val speedY:Float)