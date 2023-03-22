package com.shift.domain.model.dto

import com.shift.common.Const
import com.shift.domain.model.entity.BaseEntity
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id


/**
 * @author saito
 *
 */
@Entity
class ScheduleDayDto: BaseEntity() {

    @Id
    @Column(name = "id")
    var id: Int? = null

    @Column(name = "user_id", unique = true)
    var userId: String? = null

    @Column(name = "user_name", unique = true)
    var userName: String? = null

    @Column(name = "schedule1", unique = true)
    var schedule1: String? = null

    @Column(name = "schedule2", unique = true)
    var schedule2: String? = null

    @Column(name = "schedule3", unique = true)
    var schedule3: String? = null

    @Column(name = "schedule4", unique = true)
    var schedule4: String? = null

    @Column(name = "schedule5", unique = true)
    var schedule5: String? = null

    @Column(name = "schedule6", unique = true)
    var schedule6: String? = null

    @Column(name = "schedule7", unique = true)
    var schedule7: String? = null


    /**
     * スケジュール登録判定処理
     *
     * スケジュールが登録済みか判定し、Booleanの配列に格納する
     *
     * @return Array<Boolean> スケジュールが登録済みか判定した配列
     */
    fun scheduleFormatTFArray(): Array<Boolean?> {

        // スケジュールが登録済みか判定
        val isScheduleArray = arrayOfNulls<Boolean>(Const.SCHEDULE_RECORDABLE_MAX_DIVISION)
        isScheduleArray[0] = Const.SCHEDULE_DAY_RECORDED == schedule1
        isScheduleArray[1] = Const.SCHEDULE_DAY_RECORDED == schedule2
        isScheduleArray[2] = Const.SCHEDULE_DAY_RECORDED == schedule3
        isScheduleArray[3] = Const.SCHEDULE_DAY_RECORDED == schedule4
        isScheduleArray[4] = Const.SCHEDULE_DAY_RECORDED == schedule5
        isScheduleArray[5] = Const.SCHEDULE_DAY_RECORDED == schedule6
        isScheduleArray[6] = Const.SCHEDULE_DAY_RECORDED == schedule7
        return isScheduleArray
    }
}