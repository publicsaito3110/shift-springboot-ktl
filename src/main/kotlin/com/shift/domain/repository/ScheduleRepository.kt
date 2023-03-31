package com.shift.domain.repository

import com.shift.domain.model.entity.ScheduleEntity
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository


/**
 * @author saito
 *
 */
@Repository
interface ScheduleRepository: BaseRepository<ScheduleEntity, Int> {

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
    @Query(value = "SELECT s.* FROM schedule s WHERE s.ym = :ym AND s.user = :userId", nativeQuery = true)
    fun selectSchedule(ym: String?, userId: String?): ScheduleEntity?


    /**
     * [Repository] 確定スケジュール検索処理
     *
     * 年月と一致する全てのユーザの確定スケジュールを取得する<br></br>
     * ただし、該当のスケジュールがない場合はEmptyとなる
     *
     * @param ym 取得したいスケジュールの年月
     * @return List<ScheduleEntity> 全てのユーザの確定スケジュール
     **/
    @Query(value = "SELECT s.* FROM schedule s WHERE s.ym = :ym", nativeQuery = true)
    fun selectSchedule(ym: String?): List<ScheduleEntity>
}