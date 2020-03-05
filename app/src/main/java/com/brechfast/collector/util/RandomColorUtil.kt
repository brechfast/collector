package com.brechfast.collector.util

import com.brechfast.collector.R

/**
 * This class is used to create a random color pair for each Category background.
 */
class RandomColorUtil {

    companion object {

        private val colorMap = mapOf(
            Pair(R.color.materialGreen, R.color.materialGreenDark),
            Pair(R.color.materialOrange, R.color.materialOrangeDark),
            Pair(R.color.materialPink, R.color.materialPinkDark),
            Pair(R.color.materialPurple, R.color.materialPurpleDark),
            Pair(R.color.materialSalmon, R.color.materialSalmonDark),
            Pair(R.color.materialTeal, R.color.materialTealDark),
            Pair(R.color.materialViolet, R.color.materialVioletDark)
        )

        fun getRandomColor(): Int = colorMap.keys.random()

        fun getDarkColor(colorKey: Int): Int = colorMap.getValue(colorKey)
    }
}