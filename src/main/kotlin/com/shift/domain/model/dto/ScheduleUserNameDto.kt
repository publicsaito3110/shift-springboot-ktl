package com.shift.domain.model.dto

import com.shift.domain.model.entity.BaseEntity
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id


/**
 * @author saito
 *
 */
@Entity
class ScheduleUserNameDto: BaseEntity() {

    @Id
    @Column(name = "id")
    var id: String? = null

    @Column(name = "user_name", unique = true)
    var userName: String? = null

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
}