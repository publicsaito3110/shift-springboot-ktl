package com.shift.domain.repository

import com.shift.domain.model.entity.ScheduleEntity
import org.springframework.stereotype.Repository


/**
 * @author saito
 *
 */
@Repository
interface ScheduleRepository: BaseRepository<Int, ScheduleEntity> {

    /**
     * [Repository] ユーザ確定スケジュール検索処理
     *
     * 指定された年月からユーザーの1ヵ月分のスケジュール予定を取得する<br>
     * ただし、登録済みのスケジュールがないときはnullとなる<br>
     * また、日付が存在しない日(2月 -> 30, 31日etc)は必ず登録されていない
     *
     * @param ym 取得したい確定スケジュールの年月(YYYYMM)
     * @param userId 取得したいユーザID
     * @return ScheduleEntity
     */
    fun findByYmAndUser(ym: String?, userId: String?): ScheduleEntity?
}