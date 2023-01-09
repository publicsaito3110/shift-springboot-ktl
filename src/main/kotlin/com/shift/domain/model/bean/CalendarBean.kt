package com.shift.domain.model.bean

import com.shift.domain.model.entity.ScheduleEntity
import com.shift.domain.model.entity.ScheduleTimeEntity
import lombok.AllArgsConstructor
import lombok.NoArgsConstructor

/**
 * @author saito
 *
 */
@AllArgsConstructor
@NoArgsConstructor
class CalendarBean {

    var year: Int = 0

    var month: Int = 0

    var nowYm: String? = null

    var calendarList: List<Int?>? = null

    var afterYm: String? = null

    var beforeYm: String? = null

    var scheduleEntity: ScheduleEntity? = null

    var scheduleTimeEntity: ScheduleTimeEntity? = null

    var isScheduleAllArray: Array<Array<Boolean?>>? = null
}