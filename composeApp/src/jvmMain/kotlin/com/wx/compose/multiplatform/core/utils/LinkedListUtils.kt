package com.wx.compose.multiplatform.core.utils

import java.util.LinkedList
import kotlin.random.Random

object LinkedListUtils {


    //该扩展函数会原地修改LinkedList，删除连续出现超过15次的相同元素
    fun <T> LinkedList<T>.removeConsecutiveDuplicates(maxAllowed: Int = 15) {
        if (size <= maxAllowed) return

        val iterator = listIterator()
        var current: T? = null
        var count = 1

        while (iterator.hasNext()) {
            val next = iterator.next()
            if (next == current) {
                count++
                if (count > maxAllowed) {
                    iterator.remove()
                }
            } else {
                current = next
                count = 1
            }
        }
    }

    //通过洗牌算法实现更均匀的随机分布，适合需要完全随机化的场景
    fun <T> LinkedList<T>.randomShufflePick(count: Int): List<T> {
        require(count in 0..size) { "请求数量超出范围" }
        return shuffled().take(count)
    }

    //通过生成随机索引来选取元素，适合需要保持原集合顺序的场景
    fun <T> LinkedList<T>.randomPick(count: Int): List<T> {
        require(count in 0..size) { "请求数量超出范围" }
        return List(count) { this[Random.nextInt(size)] }
    }

}