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
class CmnScheduleUserNameBean {

    var scheduleTimeEntity: ScheduleTimeEntity? = null

    var scheduleUserAllArray: Array<Array<String>>? = null
}