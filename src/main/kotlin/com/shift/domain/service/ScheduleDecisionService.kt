package com.shift.domain.service

import com.shift.common.Const
import com.shift.domain.model.bean.CmnScheduleCalendarBean
import com.shift.domain.model.bean.CmnScheduleUserNameBean
import com.shift.domain.model.bean.ScheduleDecisionBean
import com.shift.domain.model.bean.ScheduleDecisionModifyBean
import com.shift.domain.model.dto.ScheduleDayDto
import com.shift.domain.model.dto.SchedulePreDayDto
import com.shift.domain.model.entity.ScheduleTimeEntity
import com.shift.domain.model.entity.UserEntity
import com.shift.domain.repository.*
import com.shift.domain.service.common.CmnScheduleCalendarService
import com.shift.domain.service.common.CmnScheduleUserNameService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


/**
 * @author saito
 *
 */
@Service
@Transactional
class ScheduleDecisionService: BaseService() {

    @Autowired
    private lateinit var scheduleTimeRepository: ScheduleTimeRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var schedulePreDayRepository: SchedulePreDayRepository

    @Autowired
    private lateinit var scheduleDayRepository: ScheduleDayRepository

    @Autowired
    private lateinit var cmnScheduleCalendarService: CmnScheduleCalendarService

    @Autowired
    private lateinit var cmnScheduleUserNameService: CmnScheduleUserNameService


    /**
     * [Service] 確定スケジュール表示機能画面 (/schedule-decision)
     *
     * @param ym 年月
     * @return HomeBean
     */
    fun scheduleDecision(ym: String?): ScheduleDecisionBean {

        // CmnScheduleCalendarServiceからカレンダー, 年月, 最終日を取得
        val cmnScheduleCalendarBean: CmnScheduleCalendarBean = cmnScheduleCalendarService.generateCalendar(ym)
        // CmnScheduleUserNameServiceから2次元配列の確定スケジュール, スケジュール時間区分を取得
        val cmnScheduleUserNameBean: CmnScheduleUserNameBean = cmnScheduleUserNameService.generateScheduleAllUser(
            cmnScheduleCalendarBean.year,
            cmnScheduleCalendarBean.month,
        )

        // Beanにセット
        val scheduleDecisionBean = ScheduleDecisionBean()
        scheduleDecisionBean.year = cmnScheduleCalendarBean.year
        scheduleDecisionBean.month = cmnScheduleCalendarBean.month
        scheduleDecisionBean.nowYm = cmnScheduleCalendarBean.nowYm
        scheduleDecisionBean.calendarList = cmnScheduleCalendarBean.calendarList
        scheduleDecisionBean.afterYm = cmnScheduleCalendarBean.afterYm
        scheduleDecisionBean.beforeYm = cmnScheduleCalendarBean.beforeYm
        scheduleDecisionBean.scheduleUserNameArray = cmnScheduleUserNameBean.scheduleUserNameArray
        scheduleDecisionBean.scheduleTimeEntity = cmnScheduleUserNameBean.scheduleTimeEntity
        return scheduleDecisionBean
    }


    /**
     * [Service] 確定スケジュール修正画面 (/schedule-decision/modify)
     *
     * @param ym 年月
     * @param day 日付
     * @return ScheduleDecisionModifyBean
     */
    fun scheduleDecisionModify(ym: String, day: String): ScheduleDecisionModifyBean {

        // CmnScheduleCalendarServiceからカレンダー, 年月, 最終日を取得
        val cmnScheduleCalendarBean: CmnScheduleCalendarBean = cmnScheduleCalendarService.generateCalendar(ym)
        // 年, 月, 日をそれぞれ配列で取得
        val ymdArray: Array<String> = calcYmdArray(ym, day)
        // 1日分の予定スケジュールをユーザ毎に取得
        val schedulePreDayList: List<SchedulePreDayDto> = selectSchedulePreDay(ym, day)
        // 1日分の確定スケジュールをユーザ毎に取得
        val scheduleUserList: List<ScheduleDayDto> = selectScheduleDay(ym, day)
        // スケジュール時間区分を取得
        val scheduleTimeEntity: ScheduleTimeEntity? = selectScheduleTime(cmnScheduleCalendarBean.lastDateYmd)
        // 未退職ユーザを全て取得
        val userDbList: List<UserEntity> = selectUserNotDelFlg()
        // 確定スケジュールに新規登録可能ユーザのみに変換
        val userList: List<UserEntity> = calcUserRecordableSchedule(scheduleUserList, userDbList)

        // Beanにセット
        val scheduleDecisionModifyBean = ScheduleDecisionModifyBean()
        scheduleDecisionModifyBean.year = ymdArray[0]
        scheduleDecisionModifyBean.month = ymdArray[1]
        scheduleDecisionModifyBean.day = ymdArray[2]
        scheduleDecisionModifyBean.schedulePreDayList = schedulePreDayList
        scheduleDecisionModifyBean.scheduleDayList = scheduleUserList
        scheduleDecisionModifyBean.scheduleTimeEntity = scheduleTimeEntity
        scheduleDecisionModifyBean.userList = userList
        return scheduleDecisionModifyBean
    }


    /**
     * 年月日付取得処理
     *
     * 年月と日からをそれぞれ配列で取得する
     *
     * @param ym 年月
     * @param day 日
     * @return Array<String> 年月日が格納された配列<br>
     * (要素: [0] 年, [1] 月, [2] 日)
     */
    private fun calcYmdArray(ym: String, day: String): Array<String> {

        // 年, 月, 日をそれぞれ取得し、配列に格納
        val year = ym.substring(0, 4)
        val month = ym.substring(4, 6)
        val trimDay = String.format("%02d", day.toInt())
        return arrayOf(year, month, trimDay)
    }


    /**
     * 確定スケジュール登録可能ユーザ取得処理
     *
     * 確定スケジュールに登録されているユーザを除く、登録可能ユーザをすべて取得する<br>
     * ただし、確定スケジュールに登録済みユーザがいないときは何もユーザを返す
     *
     * @param scheduleUserList ユーザ毎の対象の日付のスケジュール
     * @param userDbList 全てのユーザ
     * @return List<UserEntity> スケジュールに新規登録可能ユーザ
     **/
    private fun calcUserRecordableSchedule(scheduleUserList: List<ScheduleDayDto>, userDbList: List<UserEntity>): List<UserEntity> {

        // 登録可能ユーザがいないとき、何もせず返す
        if (scheduleUserList.isEmpty()) {
            return userDbList
        }

        // 確定スケジュールに登録されているユーザIDを取得する
        val scheduleRecordedUserList: MutableList<String?> = ArrayList()

        // 登録されている確定スケジュール分だけループし、確定スケジュールに登録されているユーザIDを格納する
        for (scheduleDayDto in scheduleUserList) {
            scheduleRecordedUserList.add(scheduleDayDto.userId)
        }

        // 登録可能ユーザを取得するための変数
        val usersList: MutableList<UserEntity> = ArrayList()

        // 該当のユーザの総数だけループする
        for (userEntity in userDbList) {

            // スケジュールに登録されていないとき、ユーザを追加する
            if (!scheduleRecordedUserList.contains(userEntity.id)) {
                usersList.add(userEntity)
            }
        }
        return usersList
    }


    /**
     * [Repository] 指定日付予定スケジュール取得処理
     *
     * 指定した年月及び日付に該当する予定スケジュールをユーザごとに取得する<br>
     * ただし、登録済みのスケジュールが1つもないときはEmptyとなる<br>
     * その日付に登録されたユーザのみ取得される<br>
     * ex) ym=200001,day=1のとき2000年1月1日, ym=200001,day=2のとき2000年1月2日...
     *
     * @param ym 検索したい年月
     * @param day 検索したい日付
     * @return List<SchedulePreDayDto> ユーザ毎の対象の日付のスケジュール
     **/
    private fun selectSchedulePreDay(ym: String, day: String): List<SchedulePreDayDto> {

        // 検索したい文字と文字列目を指定するためにフォーマットを"%1______%"に整える
        val schedule: String = Const.CHARACTER_PERCENT + Const.SCHEDULE_DAY_RECORDED + Const.CHARACTER_PERCENT
        val schedule1: String = Const.CHARACTER_PERCENT + Const.SCHEDULE_DAY_RECORDED + "______" + Const.CHARACTER_PERCENT
        val schedule2: String = Const.CHARACTER_PERCENT + "_" + Const.SCHEDULE_DAY_RECORDED + "_____" + Const.CHARACTER_PERCENT
        val schedule3: String = Const.CHARACTER_PERCENT + "__" + Const.SCHEDULE_DAY_RECORDED + "____" + Const.CHARACTER_PERCENT
        val schedule4: String = Const.CHARACTER_PERCENT + "___" + Const.SCHEDULE_DAY_RECORDED + "___" + Const.CHARACTER_PERCENT
        val schedule5: String = Const.CHARACTER_PERCENT + "____" + Const.SCHEDULE_DAY_RECORDED + "__" + Const.CHARACTER_PERCENT
        val schedule6: String = Const.CHARACTER_PERCENT + "_____" + Const.SCHEDULE_DAY_RECORDED + "_" + Const.CHARACTER_PERCENT
        val schedule7: String = Const.CHARACTER_PERCENT + "______" + Const.SCHEDULE_DAY_RECORDED + Const.CHARACTER_PERCENT

        // 日付(DD)に変換する
        val trimDay = String.format("%02d", day.toInt())

        // 日付に応じて取得するスケジュールの日付を変える
        var schedulePreDayDtoList: List<SchedulePreDayDto> = ArrayList()
        if ("01" == trimDay) {
            schedulePreDayDtoList = schedulePreDayRepository.selectSchedulePreDay1(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_DAY_RECORDED, ym)
        } else if ("02" == trimDay) {
            schedulePreDayDtoList = schedulePreDayRepository.selectSchedulePreDay2(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_DAY_RECORDED, ym)
        } else if ("03" == trimDay) {
            schedulePreDayDtoList = schedulePreDayRepository.selectSchedulePreDay3(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_DAY_RECORDED, ym)
        } else if ("04" == trimDay) {
            schedulePreDayDtoList = schedulePreDayRepository.selectSchedulePreDay4(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_DAY_RECORDED, ym)
        } else if ("05" == trimDay) {
            schedulePreDayDtoList = schedulePreDayRepository.selectSchedulePreDay5(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_DAY_RECORDED, ym)
        } else if ("06" == trimDay) {
            schedulePreDayDtoList = schedulePreDayRepository.selectSchedulePreDay6(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_DAY_RECORDED, ym)
        } else if ("07" == trimDay) {
            schedulePreDayDtoList = schedulePreDayRepository.selectSchedulePreDay7(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_DAY_RECORDED, ym)
        } else if ("08" == trimDay) {
            schedulePreDayDtoList = schedulePreDayRepository.selectSchedulePreDay8(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_DAY_RECORDED, ym)
        } else if ("09" == trimDay) {
            schedulePreDayDtoList = schedulePreDayRepository.selectSchedulePreDay9(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_DAY_RECORDED, ym)
        } else if ("10" == trimDay) {
            schedulePreDayDtoList = schedulePreDayRepository.selectSchedulePreDay10(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_DAY_RECORDED, ym)
        } else if ("11" == trimDay) {
            schedulePreDayDtoList = schedulePreDayRepository.selectSchedulePreDay11(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_DAY_RECORDED, ym)
        } else if ("12" == trimDay) {
            schedulePreDayDtoList = schedulePreDayRepository.selectSchedulePreDay12(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_DAY_RECORDED, ym)
        } else if ("13" == trimDay) {
            schedulePreDayDtoList = schedulePreDayRepository.selectSchedulePreDay13(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_DAY_RECORDED, ym)
        } else if ("14" == trimDay) {
            schedulePreDayDtoList = schedulePreDayRepository.selectSchedulePreDay14(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_DAY_RECORDED, ym)
        } else if ("15" == trimDay) {
            schedulePreDayDtoList = schedulePreDayRepository.selectSchedulePreDay15(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_DAY_RECORDED, ym)
        } else if ("16" == trimDay) {
            schedulePreDayDtoList = schedulePreDayRepository.selectSchedulePreDay16(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_DAY_RECORDED, ym)
        } else if ("17" == trimDay) {
            schedulePreDayDtoList = schedulePreDayRepository.selectSchedulePreDay17(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_DAY_RECORDED, ym)
        } else if ("18" == trimDay) {
            schedulePreDayDtoList = schedulePreDayRepository.selectSchedulePreDay18(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_DAY_RECORDED, ym)
        } else if ("19" == trimDay) {
            schedulePreDayDtoList = schedulePreDayRepository.selectSchedulePreDay19(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_DAY_RECORDED, ym)
        } else if ("20" == trimDay) {
            schedulePreDayDtoList = schedulePreDayRepository.selectSchedulePreDay20(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_DAY_RECORDED, ym)
        } else if ("21" == trimDay) {
            schedulePreDayDtoList = schedulePreDayRepository.selectSchedulePreDay21(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_DAY_RECORDED, ym)
        } else if ("22" == trimDay) {
            schedulePreDayDtoList = schedulePreDayRepository.selectSchedulePreDay22(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_DAY_RECORDED, ym)
        } else if ("23" == trimDay) {
            schedulePreDayDtoList = schedulePreDayRepository.selectSchedulePreDay23(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_DAY_RECORDED, ym)
        } else if ("24" == trimDay) {
            schedulePreDayDtoList = schedulePreDayRepository.selectSchedulePreDay24(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_DAY_RECORDED, ym)
        } else if ("25" == trimDay) {
            schedulePreDayDtoList = schedulePreDayRepository.selectSchedulePreDay25(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_DAY_RECORDED, ym)
        } else if ("26" == trimDay) {
            schedulePreDayDtoList = schedulePreDayRepository.selectSchedulePreDay26(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_DAY_RECORDED, ym)
        } else if ("27" == trimDay) {
            schedulePreDayDtoList = schedulePreDayRepository.selectSchedulePreDay27(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_DAY_RECORDED, ym)
        } else if ("28" == trimDay) {
            schedulePreDayDtoList = schedulePreDayRepository.selectSchedulePreDay28(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_DAY_RECORDED, ym)
        } else if ("29" == trimDay) {
            schedulePreDayDtoList = schedulePreDayRepository.selectSchedulePreDay29(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_DAY_RECORDED, ym)
        } else if ("30" == trimDay) {
            schedulePreDayDtoList = schedulePreDayRepository.selectSchedulePreDay30(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_DAY_RECORDED, ym)
        } else if ("31" == trimDay) {
            schedulePreDayDtoList = schedulePreDayRepository.selectSchedulePreDay31(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_DAY_RECORDED, ym)
        }
        return schedulePreDayDtoList
    }


    /**
     * [Repository] 指定日付予定スケジュール取得処理
     *
     * 指定した年月及び日付に該当する予定スケジュールをユーザごとに取得する<br>
     * ただし、登録済みのスケジュールが1つもないときはEmptyとなる<br>
     * その日付に登録されたユーザのみ取得される<br>
     * ex) ym=200001,day=1のとき2000年1月1日, ym=200001,day=2のとき2000年1月2日...
     *
     * @param ym 検索したい年月
     * @param day 検索したい日付
     * @return List<ScheduleDayDto> ユーザ毎の対象の日付のスケジュール
     **/
    private fun selectScheduleDay(ym: String, day: String): List<ScheduleDayDto> {

        // 検索したい文字と文字列目を指定するためにフォーマットを"%1______%"に整える
        val schedule: String = Const.CHARACTER_PERCENT + Const.SCHEDULE_DAY_RECORDED + Const.CHARACTER_PERCENT
        val schedule1: String = Const.CHARACTER_PERCENT + Const.SCHEDULE_DAY_RECORDED + "______" + Const.CHARACTER_PERCENT
        val schedule2: String = Const.CHARACTER_PERCENT + "_" + Const.SCHEDULE_DAY_RECORDED + "_____" + Const.CHARACTER_PERCENT
        val schedule3: String = Const.CHARACTER_PERCENT + "__" + Const.SCHEDULE_DAY_RECORDED + "____" + Const.CHARACTER_PERCENT
        val schedule4: String = Const.CHARACTER_PERCENT + "___" + Const.SCHEDULE_DAY_RECORDED + "___" + Const.CHARACTER_PERCENT
        val schedule5: String = Const.CHARACTER_PERCENT + "____" + Const.SCHEDULE_DAY_RECORDED + "__" + Const.CHARACTER_PERCENT
        val schedule6: String = Const.CHARACTER_PERCENT + "_____" + Const.SCHEDULE_DAY_RECORDED + "_" + Const.CHARACTER_PERCENT
        val schedule7: String = Const.CHARACTER_PERCENT + "______" + Const.SCHEDULE_DAY_RECORDED + Const.CHARACTER_PERCENT

        // 日付(DD)に変換する
        val trimDay = String.format("%02d", day.toInt())

        // 日付に応じて取得するスケジュールの日付を変える
        var scheduleDayDtoList: List<ScheduleDayDto> = ArrayList()
        if ("01" == trimDay) {
            scheduleDayDtoList = scheduleDayRepository.selectScheduleDay1(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_DAY_RECORDED, ym)
        } else if ("02" == trimDay) {
            scheduleDayDtoList = scheduleDayRepository.selectScheduleDay2(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_DAY_RECORDED, ym)
        } else if ("03" == trimDay) {
            scheduleDayDtoList = scheduleDayRepository.selectScheduleDay3(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_DAY_RECORDED, ym)
        } else if ("04" == trimDay) {
            scheduleDayDtoList = scheduleDayRepository.selectScheduleDay4(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_DAY_RECORDED, ym)
        } else if ("05" == trimDay) {
            scheduleDayDtoList = scheduleDayRepository.selectScheduleDay5(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_DAY_RECORDED, ym)
        } else if ("06" == trimDay) {
            scheduleDayDtoList = scheduleDayRepository.selectScheduleDay6(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_DAY_RECORDED, ym)
        } else if ("07" == trimDay) {
            scheduleDayDtoList = scheduleDayRepository.selectScheduleDay7(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_DAY_RECORDED, ym)
        } else if ("08" == trimDay) {
            scheduleDayDtoList = scheduleDayRepository.selectScheduleDay8(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_DAY_RECORDED, ym)
        } else if ("09" == trimDay) {
            scheduleDayDtoList = scheduleDayRepository.selectScheduleDay9(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_DAY_RECORDED, ym)
        } else if ("10" == trimDay) {
            scheduleDayDtoList = scheduleDayRepository.selectScheduleDay10(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_DAY_RECORDED, ym)
        } else if ("11" == trimDay) {
            scheduleDayDtoList = scheduleDayRepository.selectScheduleDay11(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_DAY_RECORDED, ym)
        } else if ("12" == trimDay) {
            scheduleDayDtoList = scheduleDayRepository.selectScheduleDay12(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_DAY_RECORDED, ym)
        } else if ("13" == trimDay) {
            scheduleDayDtoList = scheduleDayRepository.selectScheduleDay13(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_DAY_RECORDED, ym)
        } else if ("14" == trimDay) {
            scheduleDayDtoList = scheduleDayRepository.selectScheduleDay14(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_DAY_RECORDED, ym)
        } else if ("15" == trimDay) {
            scheduleDayDtoList = scheduleDayRepository.selectScheduleDay15(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_DAY_RECORDED, ym)
        } else if ("16" == trimDay) {
            scheduleDayDtoList = scheduleDayRepository.selectScheduleDay16(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_DAY_RECORDED, ym)
        } else if ("17" == trimDay) {
            scheduleDayDtoList = scheduleDayRepository.selectScheduleDay17(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_DAY_RECORDED, ym)
        } else if ("18" == trimDay) {
            scheduleDayDtoList = scheduleDayRepository.selectScheduleDay18(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_DAY_RECORDED, ym)
        } else if ("19" == trimDay) {
            scheduleDayDtoList = scheduleDayRepository.selectScheduleDay19(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_DAY_RECORDED, ym)
        } else if ("20" == trimDay) {
            scheduleDayDtoList = scheduleDayRepository.selectScheduleDay20(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_DAY_RECORDED, ym)
        } else if ("21" == trimDay) {
            scheduleDayDtoList = scheduleDayRepository.selectScheduleDay21(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_DAY_RECORDED, ym)
        } else if ("22" == trimDay) {
            scheduleDayDtoList = scheduleDayRepository.selectScheduleDay22(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_DAY_RECORDED, ym)
        } else if ("23" == trimDay) {
            scheduleDayDtoList = scheduleDayRepository.selectScheduleDay23(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_DAY_RECORDED, ym)
        } else if ("24" == trimDay) {
            scheduleDayDtoList = scheduleDayRepository.selectScheduleDay24(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_DAY_RECORDED, ym)
        } else if ("25" == trimDay) {
            scheduleDayDtoList = scheduleDayRepository.selectScheduleDay25(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_DAY_RECORDED, ym)
        } else if ("26" == trimDay) {
            scheduleDayDtoList = scheduleDayRepository.selectScheduleDay26(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_DAY_RECORDED, ym)
        } else if ("27" == trimDay) {
            scheduleDayDtoList = scheduleDayRepository.selectScheduleDay27(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_DAY_RECORDED, ym)
        } else if ("28" == trimDay) {
            scheduleDayDtoList = scheduleDayRepository.selectScheduleDay28(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_DAY_RECORDED, ym)
        } else if ("29" == trimDay) {
            scheduleDayDtoList = scheduleDayRepository.selectScheduleDay29(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_DAY_RECORDED, ym)
        } else if ("30" == trimDay) {
            scheduleDayDtoList = scheduleDayRepository.selectScheduleDay30(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_DAY_RECORDED, ym)
        } else if ("31" == trimDay) {
            scheduleDayDtoList = scheduleDayRepository.selectScheduleDay31(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_DAY_RECORDED, ym)
        }
        return scheduleDayDtoList
    }


    /**
     * [Repository] スケジュール時間区分検索処理
     *
     * 取得したい日付(ymd)から該当するスケジュール時間区分を取得する<br>
     * また、現在日(ymd)に該当するスケジュール時間区分が複数登録されているときは最新のスケジュール時間区分が取得される<br>
     * ただし、スケジュール時間区分が何も登録されていないときはnullとなる
     *
     * @param ymd 取得したいスケジュール時間区分の日付(YYYYMMDD)
     * @return ScheduleTimeEntity 指定した年月のスケジュール時間区分
     */
    private fun selectScheduleTime(ymd: String?): ScheduleTimeEntity? {
        return scheduleTimeRepository.selectScheduleTime(ymd)
    }


    /**
     * [Repository] ユーザ検索処理
     *
     * 未退職ユーザを全て取得する<br>
     * ただし、該当するユーザーがいない場合はEmptyとなる
     *
     * @return UsersEntity 全ての未退職ユーザ
     */
    private fun selectUserNotDelFlg(): List<UserEntity> {
        return userRepository.selectUsersNotDelFlg(Const.USER_DEL_FLG)
    }
}