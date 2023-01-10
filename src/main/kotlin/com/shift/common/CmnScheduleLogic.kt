package com.shift.common

import com.shift.domain.model.bean.collection.ScheduleTimeBean
import com.shift.domain.model.entity.ScheduleTimeEntity


/**
 * @author saito
 *
 */
class CmnScheduleLogic {


    /**
     * スケジュール時間区分ごとの登録済み判別Array処理
     *
     * scheduleからスケジュール時間区分ごとのスケジュールが登録されているかどうかをBooleanの配列に変換して返す<br>
     * ただし、スケジュール時間区分に登録可能な数だけ判定する<br>
     * また、scheduleは一文字ずつ取得し、スケジュール時間区分ごとに登録されているか判定される
     *
     * @param schedule スケジュール<br>
     * ただし、nullまたは文字数が登録可能なスケジュール時間区分の要素数と一致していないときはスケジュール未登録と判定される
     * @param scheduleTimeEntity 対象となるスケジュール時間区分<br>
     * ただし、nullまたはEmptyのときはBoolean[0]:falseのみが返される
     * @return Array<Boolean?> スケジュール登録済みか判定したBooleanの配列<br>
     * true: スケジュール登録済み<br>
     * false: スケジュール未登録
     */
    fun toIsScheduleArray(schedule: String?, scheduleTimeEntity: ScheduleTimeEntity?): Array<Boolean?> {

        // スケジュールが登録されているかどうかを判別する配列(1日ごとのスケジュールにおいて要素0 -> scheduleTimeList(0), 要素1 -> scheduleTimeList(1)...)
        val isScheduleRecordedArray: Array<Boolean?> = arrayOfNulls<Boolean>(Const.SCHEDULE_RECORDABLE_MAX_DIVISION)

        // scheduleTimeがnullのとき
        if (scheduleTimeEntity == null) {

            // 要素[0]にfalseをセットし、返す
            isScheduleRecordedArray[0] = false
            return isScheduleRecordedArray
        }

        // スケジュール時間区分をListで取得
        val scheduleTimeList: List<ScheduleTimeBean?> = scheduleTimeEntity.scheduleTimeFormatList()

        // 登録されているスケジュール時間区分だけループ
        for (i in scheduleTimeList.indices) {

            // scheduleがnullのとき
            if (schedule == null) {
                isScheduleRecordedArray[i] = false
                continue
            }

            // scheduleの文字数がiより小さい(1文字取得できない)とき
            if (schedule.length <= i) {
                isScheduleRecordedArray[i] = false
                continue
            }

            // ループの回数から1文字だけ取得
            val scheduleChara = schedule[i].toString()

            // スケジュールが登録されていないとき
            if (Const.SCHEDULE_DAY_RECORDED != scheduleChara) {
                isScheduleRecordedArray[i] = false
                continue
            }

            // スケジュールが登録されているときtrueを代入
            isScheduleRecordedArray[i] = true
        }
        return isScheduleRecordedArray
    }
}