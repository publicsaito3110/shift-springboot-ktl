package com.shift.domain.model.entity

import com.shift.common.CommonUtil
import com.shift.domain.model.bean.collection.ScheduleTimeBean
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


    /**
     * スケジュール時間区分List取得処理
     *
     * 登録済みのそれぞれのスケジュール時間区分の値をScheduleTimeBeanに格納し、Listで取得する<br>
     * ただし、スケジュール時間区分のそれそれのname, startHm, endHm, restHmの全て値があるとき登録済みのスケジュール時間区分として扱われる<br>
     * また、スケジュール時間区分が1つも登録されていない場合はEmptyとなる
     *
     * @param
     * @return List<ScheduleTimeBean?> 登録済みのスケジュール時間区分を格納したList
     */
    fun scheduleTimeFormatList(): List<ScheduleTimeBean?> {

        // スケジュール時間区分を格納するためのList
        val scheduleTimeList: MutableList<ScheduleTimeBean> = ArrayList()

        // スケジュール時間区分1が登録されているとき
        if (CommonUtil.changeEmptyByNull(name1) != "" && CommonUtil.changeEmptyByNull(startHm1) != "" && CommonUtil.changeEmptyByNull(endHm1) != "" && CommonUtil.changeEmptyByNull(restHm1) != "") {
            val scheduleTimeBean: ScheduleTimeBean = ScheduleTimeBean(name1, startHm1, endHm1, restHm1)
            scheduleTimeList.add(scheduleTimeBean)
        }

        // スケジュール時間区分2が登録されているとき
        if (CommonUtil.changeEmptyByNull(name2) != "" && CommonUtil.changeEmptyByNull(startHm2) != "" && CommonUtil.changeEmptyByNull(endHm2) != "" && CommonUtil.changeEmptyByNull(restHm2) != "") {
            val scheduleTimeBean: ScheduleTimeBean = ScheduleTimeBean(name2, startHm2, endHm2, restHm2)
            scheduleTimeList.add(scheduleTimeBean)
        }

        // スケジュール時間区分3が登録されているとき
        if (CommonUtil.changeEmptyByNull(name3) != "" && CommonUtil.changeEmptyByNull(startHm3) != "" && CommonUtil.changeEmptyByNull(endHm3) != "" && CommonUtil.changeEmptyByNull(restHm3) != "") {
            val scheduleTimeBean: ScheduleTimeBean = ScheduleTimeBean(name3, startHm3, endHm3, restHm3)
            scheduleTimeList.add(scheduleTimeBean)
        }

        // スケジュール時間区分4が登録されているとき
        if (CommonUtil.changeEmptyByNull(name4) != "" && CommonUtil.changeEmptyByNull(startHm4) != "" && CommonUtil.changeEmptyByNull(endHm4) != "" && CommonUtil.changeEmptyByNull(restHm4) != "") {
            val scheduleTimeBean: ScheduleTimeBean = ScheduleTimeBean(name4, startHm4, endHm4, restHm4)
            scheduleTimeList.add(scheduleTimeBean)
        }

        // スケジュール時間区分5が登録されているとき
        if (CommonUtil.changeEmptyByNull(name5) != "" && CommonUtil.changeEmptyByNull(startHm5) != "" && CommonUtil.changeEmptyByNull(endHm5) != "" && CommonUtil.changeEmptyByNull(restHm5) != "") {
            val scheduleTimeBean: ScheduleTimeBean = ScheduleTimeBean(name5, startHm5, endHm5, restHm5)
            scheduleTimeList.add(scheduleTimeBean)
        }

        // スケジュール時間区分6が登録されているとき
        if (CommonUtil.changeEmptyByNull(name6) != "" && CommonUtil.changeEmptyByNull(startHm6) != "" && CommonUtil.changeEmptyByNull(endHm6) != "" && CommonUtil.changeEmptyByNull(restHm6) != "") {
            val scheduleTimeBean: ScheduleTimeBean = ScheduleTimeBean(name6, startHm6, endHm6, restHm6)
            scheduleTimeList.add(scheduleTimeBean)
        }

        // スケジュール時間区分7が登録されているとき
        if (CommonUtil.changeEmptyByNull(name7) != "" && CommonUtil.changeEmptyByNull(startHm7) != "" && CommonUtil.changeEmptyByNull(endHm7) != "" && CommonUtil.changeEmptyByNull(restHm7) != "") {
            val scheduleTimeBean: ScheduleTimeBean = ScheduleTimeBean(name7, startHm7, endHm7, restHm7)
            scheduleTimeList.add(scheduleTimeBean)
        }
        return scheduleTimeList
    }
}