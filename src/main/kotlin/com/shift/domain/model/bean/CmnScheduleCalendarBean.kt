package com.shift.domain.model.bean

import lombok.AllArgsConstructor
import lombok.NoArgsConstructor

/**
 * @author saito
 *
 */
@AllArgsConstructor
@NoArgsConstructor
class CmnScheduleCalendarBean {

    var year: Int = 0

    var month: Int = 0

    var lastDateYmd: String? = null

    var calendarList: List<Int?>? = null

    var nowYm: String? = null

    var afterYm: String? = null

    var beforeYm: String? = null
}