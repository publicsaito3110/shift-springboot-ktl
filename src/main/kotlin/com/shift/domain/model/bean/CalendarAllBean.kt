package com.shift.domain.model.bean

import com.shift.domain.model.entity.ScheduleTimeEntity
import lombok.AllArgsConstructor
import lombok.NoArgsConstructor


/**
 * @author saito
 *
 */
@AllArgsConstructor
@NoArgsConstructor
class CalendarAllBean {

    var year = 0

    var month = 0

    var calendarList: List<Int?>? = null

    var scheduleUserNameArray: Array<Array<String?>>? = null

    var afterYm: String? = null

    var beforeYm: String? = null

    var scheduleTimeEntity: ScheduleTimeEntity? = null
}