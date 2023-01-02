package com.shift.domain.repository

import com.shift.domain.model.entity.ScheduleTimeEntity
import org.springframework.stereotype.Repository


/**
 * @author saito
 *
 */
@Repository
interface ScheduleTimeRepository: BaseRepository<Int, ScheduleTimeEntity> {
}