package com.shift.domain.model.bean

import com.shift.domain.model.dto.ScheduleDayDto
import com.shift.domain.model.dto.SchedulePreDayDto
import com.shift.domain.model.entity.ScheduleTimeEntity
import com.shift.domain.model.entity.UserEntity
import lombok.AllArgsConstructor
import lombok.NoArgsConstructor


/**
 * @author saito
 *
 */
@AllArgsConstructor
@NoArgsConstructor
class ScheduleDecisionModifyModifyBean {

    var isUpdate = false
    
    var year: String? = null

    var month: String? = null

    var day: String? = null

    var schedulePreDayList: List<SchedulePreDayDto>? = null

    var scheduleDayList: List<ScheduleDayDto>? = null

    var scheduleTimeEntity: ScheduleTimeEntity? = null

    var userList: List<UserEntity>? = null
}