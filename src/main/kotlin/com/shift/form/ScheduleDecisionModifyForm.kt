package com.shift.form

import com.shift.common.Const
import com.shift.domain.model.dto.ScheduleDayDto
import org.hibernate.validator.constraints.Length
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern


/**
 * @author saito
 *
 */
class ScheduleDecisionModifyForm {

    @NotBlank(message = "入力値が不正です")
    @Pattern(regexp = Const.PATTERN_SCHEDULE_YM_INPUT, message = "入力値が不正です")
    @Length(
        min = Const.PATTERN_SCHEDULE_YM_LENGTH_MIN_INPUT,
        max = Const.PATTERN_SCHEDULE_YM_LENGTH_MAX_INPUT,
        message = "入力値が不正です"
    )
    var ym: String? = null

    @NotBlank(message = "入力値が不正です")
    @Pattern(regexp = Const.PATTERN_SCHEDULE_DAY_INPUT, message = "入力値が不正です")
    @Length(
        min = Const.PATTERN_SCHEDULE_DAY_LENGTH_MIN_INPUT,
        max = Const.PATTERN_SCHEDULE_DAY_LENGTH_MAX_INPUT,
        message = "入力値が不正です"
    )
    var day: String? = null

    var userArray: Array<Array<String?>>? = null

    var scheduleArray: Array<Array<String?>>? = null

    @Pattern(regexp = Const.PATTERN_SCHEDULE_USER_INPUT_OPTIONAL, message = "入力値が不正です")
    @Length(
        min = Const.PATTERN_SCHEDULE_USER_LENGTH_MIN_INPUT_OPTIONAL,
        max = Const.PATTERN_SCHEDULE_USER_LENGTH_MAX_INPUT_OPTIONAL,
        message = "入力値が不正です"
    )
    private val addUserId: String? = null

    private val addScheduleArray = arrayOfNulls<String>(Const.SCHEDULE_RECORDABLE_MAX_DIVISION)


    /**
     * [Constractor] ScheduleDecisionModifyForm
     *
     * 1日のスケジュール及びスケジュールに新規登録可能ユーザの値をセットする
     *
     * @param scheduleDayList 1日のスケジュール
     * @param year 登録する年
     * @param month 登録する月
     * @param day 登録する日
     * @return ScheduleDecisionModifyForm
     */
    constructor(scheduleDayList: List<ScheduleDayDto>?, year: String?, month: String?, day: String?) {

        // 登録するスケジュールの年月をセット
        ym = year + month
        this.day = day

        // スケジュールが存在していないとき
        if (scheduleDayList == null || scheduleDayList.isEmpty()) {
            return
        }

        // 登録ユーザと登録済みスケジュールの要素数を確定スケジュールの登録済みのユーザ数で指定
        userArray = Array(scheduleDayList.size) { arrayOfNulls(2) }
        scheduleArray = Array(scheduleDayList.size) { arrayOfNulls(Const.SCHEDULE_RECORDABLE_MAX_DIVISION) }

        //--------------------------------------------------------------
        //　登録したスケジュール通りになるようにuserScheduleArrayに値を代入する
        //--------------------------------------------------------------

        // 確定スケジュール登録済みユーザだけループする
        for (i in scheduleDayList.indices) {

            // 確定スケジュールに登録済みのユーザ名とIDをそれぞれ格納
            userArray!![i][0] = scheduleDayList[i].userId
            userArray!![i][1] = scheduleDayList[i].userName

            // スケジュールが登録済みか判定した配列を取得
            val isScheduleRecordedArray: Array<Boolean?> = scheduleDayList[i].scheduleFormatTFArray()

            // スケジュール登録済み判定したスケジュール情報だけループする
            for (j in isScheduleRecordedArray.indices) {
                val isScheduleRecorded = isScheduleRecordedArray[j]

                if (!isScheduleRecorded!!) {
                    // スケジュールが登録されていないとき、未登録の情報を格納する
                    scheduleArray!![i][j] = Const.SCHEDULE_NOT_RECORDED
                    continue
                } else {
                    //　スケジュールが登録されているとき、登録済みの情報を格納する
                    scheduleArray!![i][j] = Const.SCHEDULE_RECORDED
                }
            }
        }
    }


    /**
     * 新規スケジュール文字列変換処理
     *
     * 新規スケジュールを配列から文字列へ返還する<br>
     * ただし、スケジュール時間区分と同じ桁数となる
     *
     * @return String 配列から文字列に変換された新規スケジュール
     */
    fun addScheduleArrayFormatString(): String {

        // スケジュールを格納
        var schedule = ""

        // スケジュールの時間区分だけループ
        for (`val` in addScheduleArray) {
            if (Const.SCHEDULE_RECORDED.equals(`val`)) {
                // スケジュールが登録されているとき、登録情報を格納
                schedule += Const.SCHEDULE_RECORDED
            } else {
                // スケジュールが登録されていないとき、未登録情報を格納
                schedule += Const.SCHEDULE_NOT_RECORDED
            }
        }
        return schedule
    }
}