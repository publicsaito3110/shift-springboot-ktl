package com.shift.domain.repository

import com.shift.domain.model.entity.ScheduleTimeEntity
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository


/**
 * @author saito
 *
 */
@Repository
interface ScheduleTimeRepository: BaseRepository<ScheduleTimeEntity, Int> {


    /**
     * [Repository] スケジュール時間区分取得処理
     *
     * 取得したい日付(ymd)から該当するスケジュール時間区分を取得する<br></br>
     * また、現在日(ymd)に該当するスケジュール時間区分が複数登録されているときは最新のスケジュール時間区分が取得される<br></br>
     * ただし、スケジュール時間区分が何も登録されていないときはnullとなる
     *
     * @param ymd 取得したいスケジュール時間区分の日付(YYYYMMDD)
     * @return ScheduleTimeEntity
     */
    @Query(value = "SELECT s.* FROM schedule_time s WHERE s.end_ymd = (SELECT MIN(c.end_ymd) FROM schedule_time c WHERE :ymd <= c.end_ymd) AND s.id = (SELECT MAX(h.id) FROM schedule_time h WHERE h.end_ymd = (SELECT MIN(e.end_ymd) FROM schedule_time e WHERE :ymd <= e.end_ymd))", nativeQuery = true)
    fun selectScheduleTimeByYmd(ymd: String?): ScheduleTimeEntity?
}