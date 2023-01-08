package com.shift.domain.model.entity

import javax.persistence.*


/**
 * @author saito
 *
 */
@Entity
@Table(name = "schedule")
class ScheduleEntity: BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Int? = null

    @Column(name = "ym")
    var ym: String? = null

    @Column(name = "user")
    var user: String? = null

    @Column(name = "day1")
    var day1: String? = null

    @Column(name = "day2")
    var day2: String? = null

    @Column(name = "day3")
    var day3: String? = null

    @Column(name = "day4")
    var day4: String? = null

    @Column(name = "day5")
    var day5: String? = null

    @Column(name = "day6")
    var day6: String? = null

    @Column(name = "day7")
    var day7: String? = null

    @Column(name = "day8")
    var day8: String? = null

    @Column(name = "day9")
    var day9: String? = null

    @Column(name = "day10")
    var day10: String? = null

    @Column(name = "day11")
    var day11: String? = null

    @Column(name = "day12")
    var day12: String? = null

    @Column(name = "day13")
    var day13: String? = null

    @Column(name = "day14")
    var day14: String? = null

    @Column(name = "day15")
    var day15: String? = null

    @Column(name = "day16")
    var day16: String? = null

    @Column(name = "day17")
    var day17: String? = null

    @Column(name = "day18")
    var day18: String? = null

    @Column(name = "day19")
    var day19: String? = null

    @Column(name = "day20")
    var day20: String? = null

    @Column(name = "day21")
    var day21: String? = null

    @Column(name = "day22")
    var day22: String? = null

    @Column(name = "day23")
    var day23: String? = null

    @Column(name = "day24")
    var day24: String? = null

    @Column(name = "day25")
    var day25: String? = null

    @Column(name = "day26")
    var day26: String? = null

    @Column(name = "day27")
    var day27: String? = null

    @Column(name = "day28")
    var day28: String? = null

    @Column(name = "day29")
    var day29: String? = null

    @Column(name = "day30")
    var day30: String? = null

    @Column(name = "day31")
    var day31: String? = null


    fun getScheduleDayList(): List<String?> {

        // 日付のフィールドをListで全て取得
        val dayList: MutableList<String?> = ArrayList()
        dayList.add(day1)
        dayList.add(day2)
        dayList.add(day3)
        dayList.add(day4)
        dayList.add(day5)
        dayList.add(day6)
        dayList.add(day7)
        dayList.add(day8)
        dayList.add(day9)
        dayList.add(day10)
        dayList.add(day11)
        dayList.add(day12)
        dayList.add(day13)
        dayList.add(day14)
        dayList.add(day15)
        dayList.add(day16)
        dayList.add(day17)
        dayList.add(day18)
        dayList.add(day19)
        dayList.add(day20)
        dayList.add(day21)
        dayList.add(day22)
        dayList.add(day23)
        dayList.add(day24)
        dayList.add(day25)
        dayList.add(day26)
        dayList.add(day27)
        dayList.add(day28)
        dayList.add(day29)
        dayList.add(day30)
        dayList.add(day31)
        return dayList
    }
}