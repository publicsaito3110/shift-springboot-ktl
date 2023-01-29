package com.shift.domain.model.bean.collection

import com.shift.domain.model.entity.ScheduleTimeEntity
import lombok.AllArgsConstructor
import lombok.NoArgsConstructor


/**
 * @author saito
 *
 */
@AllArgsConstructor
@NoArgsConstructor
class HomeDayScheduleBean {

    var month: Int? = 0

    var day: Int? = 0

    var week: Int? = 0

    var isScheduleRecordedArray: Array<Boolean?>? = null

    var scheduleTimeEntity: ScheduleTimeEntity? = null


    /**
     * 曜日取得処理
     *
     * weekの値に応じて曜日を返す<br>
     * 1: 月, 2: 火, 3: 水... 7: 日 となる<br>
     * ただし、一致する曜日がない場合はEmptyとなる
     *
     * @return String 曜日の名称
     */
    fun weekFormatWeekDay(): String {
        if (week == 1) {
            return "月"
        } else if (week == 2) {
            return "火"
        } else if (week == 3) {
            return "水"
        } else if (week == 4) {
            return "木"
        } else if (week == 5) {
            return "金"
        } else if (week == 6) {
            return "土"
        } else if (week == 7) {
            return "日"
        } else {
            return ""
        }
    }
}