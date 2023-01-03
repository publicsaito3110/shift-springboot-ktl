package com.shift.domain.model.entity

import javax.persistence.*


/**
 * @author saito
 *
 */
@Entity
@Table(name = "schedule")
class ScheduleTimeEntity: BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Int? = null

    @Column(name = "end_ymd")
    var endYmd: String? = null

    @Column(name = "name1")
    var name1: String? = null

    @Column(name = "start_hm1")
    var startHm1: String? = null

    @Column(name = "end_hm1")
    var endHm1: String? = null

    @Column(name = "rest_hm1")
    var restHm1: String? = null

    @Column(name = "name2")
    var name2: String? = null

    @Column(name = "start_hm2")
    var startHm2: String? = null

    @Column(name = "end_hm2")
    var endHm2: String? = null

    @Column(name = "rest_hm2")
    var restHm2: String? = null

    @Column(name = "name3")
    var name3: String? = null

    @Column(name = "start_hm3")
    var startHm3: String? = null

    @Column(name = "end_hm3")
    var endHm3: String? = null

    @Column(name = "rest_hm3")
    var restHm3: String? = null

    @Column(name = "name4")
    var name4: String? = null

    @Column(name = "start_hm4")
    var startHm4: String? = null

    @Column(name = "end_hm4")
    var endHm4: String? = null

    @Column(name = "rest_hm4")
    var restHm4: String? = null

    @Column(name = "name5")
    var name5: String? = null

    @Column(name = "start_hm5")
    var startHm5: String? = null

    @Column(name = "end_hm5")
    var endHm5: String? = null

    @Column(name = "rest_hm5")
    var restHm5: String? = null

    @Column(name = "name6")
    var name6: String? = null

    @Column(name = "start_hm6")
    var startHm6: String? = null

    @Column(name = "end_hm6")
    var endHm6: String? = null

    @Column(name = "rest_hm6")
    var restHm6: String? = null

    @Column(name = "name7")
    var name7: String? = null

    @Column(name = "start_hm7")
    var startHm7: String? = null

    @Column(name = "end_hm7")
    var endHm7: String? = null

    @Column(name = "rest_hm7")
    var restHm7: String? = null
}