package com.shift.domain.model.entity

import javax.persistence.*


/**
 * @author saito
 *
 */
@Entity
@Table(name = "schedule")
class ScheduleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private val id: Int? = null

    @Column(name = "ym")
    private val ym: String? = null

    @Column(name = "user")
    private val user: String? = null

    @Column(name = "day1")
    private val day1: String? = null

    @Column(name = "day2")
    private val day2: String? = null

    @Column(name = "day3")
    private val day3: String? = null

    @Column(name = "day4")
    private val day4: String? = null

    @Column(name = "day5")
    private val day5: String? = null

    @Column(name = "day6")
    private val day6: String? = null

    @Column(name = "day7")
    private val day7: String? = null

    @Column(name = "day8")
    private val day8: String? = null

    @Column(name = "day9")
    private val day9: String? = null

    @Column(name = "day10")
    private val day10: String? = null

    @Column(name = "day11")
    private val day11: String? = null

    @Column(name = "day12")
    private val day12: String? = null

    @Column(name = "day13")
    private val day13: String? = null

    @Column(name = "day14")
    private val day14: String? = null

    @Column(name = "day15")
    private val day15: String? = null

    @Column(name = "day16")
    private val day16: String? = null

    @Column(name = "day17")
    private val day17: String? = null

    @Column(name = "day18")
    private val day18: String? = null

    @Column(name = "day19")
    private val day19: String? = null

    @Column(name = "day20")
    private val day20: String? = null

    @Column(name = "day21")
    private val day21: String? = null

    @Column(name = "day22")
    private val day22: String? = null

    @Column(name = "day23")
    private val day23: String? = null

    @Column(name = "day24")
    private val day24: String? = null

    @Column(name = "day25")
    private val day25: String? = null

    @Column(name = "day26")
    private val day26: String? = null

    @Column(name = "day27")
    private val day27: String? = null

    @Column(name = "day28")
    private val day28: String? = null

    @Column(name = "day29")
    private val day29: String? = null

    @Column(name = "day30")
    private val day30: String? = null

    @Column(name = "day31")
    private val day31: String? = null


    fun getDayList(): List<String>? {

        // 日付のフィールドをListで全て取得
        val dayList: MutableList<String> = ArrayList()
        dayList.add(day1!!)
        dayList.add(day2!!)
        dayList.add(day3!!)
        dayList.add(day4!!)
        dayList.add(day5!!)
        dayList.add(day6!!)
        dayList.add(day7!!)
        dayList.add(day8!!)
        dayList.add(day9!!)
        dayList.add(day10!!)
        dayList.add(day11!!)
        dayList.add(day12!!)
        dayList.add(day13!!)
        dayList.add(day14!!)
        dayList.add(day15!!)
        dayList.add(day16!!)
        dayList.add(day17!!)
        dayList.add(day18!!)
        dayList.add(day19!!)
        dayList.add(day20!!)
        dayList.add(day21!!)
        dayList.add(day22!!)
        dayList.add(day23!!)
        dayList.add(day24!!)
        dayList.add(day25!!)
        dayList.add(day26!!)
        dayList.add(day27!!)
        dayList.add(day28!!)
        dayList.add(day29!!)
        dayList.add(day30!!)
        dayList.add(day31!!)
        return dayList
    }
}