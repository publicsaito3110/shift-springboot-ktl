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

    var scheduleUserNameArray: Array<Array<String?>>? = null
}