package cn.inrhor.imipetcore.util

object PositionUtil {

    /**
     * @return 旋转结果
     */
    fun Float.rotate(): Float {
        return this * 256.0F / 360.0F
    }

}