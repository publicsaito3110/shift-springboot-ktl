package com.shift.domain.repository

import com.shift.domain.model.dto.ScheduleUserNameDto
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository


/**
 * @author saito
 *
 */
@Repository
interface ScheduleUserNameRepository: BaseRepository<ScheduleUserNameDto, String> {


    /**
     * [Repository] 月次確定スケジュール登録済みユーザ検索処理
     *
     * 該当年月に一致するスケジュールに登録済みの全ユーザのスケジュールをユーザ毎に取得する
     * ただし、該当年月のスケジュールに登録済みのユーザがいないときはEmptyとなる
     *
     * @param ym 取得したい年月(YYYYMM)
     * @return List<ScheduleUserNameDto> ユーザ毎の確定スケジュール
     **/
    @Query(value = "SELECT u.id, u.name AS user_name, s.day1, s.day2, s.day3, s.day4, s.day5, s.day6, s.day7, s.day8, s.day9, s.day10, s.day11, s.day12, s.day13, s.day14, s.day15, s.day16, s.day17, s.day18, s.day19, s.day20, s.day21, s.day22, s.day23, s.day24, s.day25, s.day26, s.day27, s.day28, s.day29, s.day30, s.day31 FROM schedule s INNER JOIN user u ON u.id = s.user WHERE s.ym = :ym ORDER BY u.id", nativeQuery = true)
    fun selectScheduleUserNameMonth(ym: String?): List<ScheduleUserNameDto>
}