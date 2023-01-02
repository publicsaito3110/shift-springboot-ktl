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
    private val id: Int? = null

    @Column(name = "end_ymd")
    private val endYmd: String? = null

    @Column(name = "name1")
    private val name1: String? = null

    @Column(name = "start_hm1")
    private val startHm1: String? = null

    @Column(name = "end_hm1")
    private val endHm1: String? = null

    @Column(name = "rest_hm1")
    private val restHm1: String? = null

    @Column(name = "name2")
    private val name2: String? = null

    @Column(name = "start_hm2")
    private val startHm2: String? = null

    @Column(name = "end_hm2")
    private val endHm2: String? = null

    @Column(name = "rest_hm2")
    private val restHm2: String? = null

    @Column(name = "name3")
    private val name3: String? = null

    @Column(name = "start_hm3")
    private val startHm3: String? = null

    @Column(name = "end_hm3")
    private val endHm3: String? = null

    @Column(name = "rest_hm3")
    private val restHm3: String? = null

    @Column(name = "name4")
    private val name4: String? = null

    @Column(name = "start_hm4")
    private val startHm4: String? = null

    @Column(name = "end_hm4")
    private val endHm4: String? = null

    @Column(name = "rest_hm4")
    private val restHm4: String? = null

    @Column(name = "name5")
    private val name5: String? = null

    @Column(name = "start_hm5")
    private val startHm5: String? = null

    @Column(name = "end_hm5")
    private val endHm5: String? = null

    @Column(name = "rest_hm5")
    private val restHm5: String? = null

    @Column(name = "name6")
    private val name6: String? = null

    @Column(name = "start_hm6")
    private val startHm6: String? = null

    @Column(name = "end_hm6")
    private val endHm6: String? = null

    @Column(name = "rest_hm6")
    private val restHm6: String? = null

    @Column(name = "name7")
    private val name7: String? = null

    @Column(name = "start_hm7")
    private val startHm7: String? = null

    @Column(name = "end_hm7")
    private val endHm7: String? = null

    @Column(name = "rest_hm7")
    private val restHm7: String? = null
}