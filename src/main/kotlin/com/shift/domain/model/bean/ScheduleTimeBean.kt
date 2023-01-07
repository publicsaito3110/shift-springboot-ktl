package com.shift.domain.model.bean

import lombok.AllArgsConstructor
import lombok.NoArgsConstructor

/**
 * @author saito
 *
 */
@AllArgsConstructor
@NoArgsConstructor
class ScheduleTimeBean {

    var name: String? = null

    var startHm: String? = null

    var endHm: String? = null

    var restHm: String? = null
}