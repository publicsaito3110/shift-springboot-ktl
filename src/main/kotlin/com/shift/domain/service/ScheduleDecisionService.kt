package com.shift.domain.service

import com.shift.common.Const
import com.shift.domain.model.bean.*
import com.shift.domain.model.dto.ScheduleDayDto
import com.shift.domain.model.dto.SchedulePreDayDto
import com.shift.domain.model.entity.ScheduleEntity
import com.shift.domain.model.entity.ScheduleTimeEntity
import com.shift.domain.model.entity.UserEntity
import com.shift.domain.repository.*
import com.shift.domain.service.common.CmnScheduleCalendarService
import com.shift.domain.service.common.CmnScheduleUserNameService
import com.shift.form.ScheduleDecisionModifyForm
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
    private lateinit var scheduleRepository: ScheduleRepository

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
     * [Service] 確定スケジュール修正機能 (/schedule-decision/modify/modify)
     *
     * @param scheduleDecisionModifyForm 入力値を格納したForm
     * @return ScheduleDecisionModifyBean
     */
    fun scheduleDecisionModifyModify(scheduleDecisionModifyForm: ScheduleDecisionModifyForm): ScheduleDecisionModifyModifyBean {

        // 確定スケジュールを更新する
        val isUpdate: Boolean = updateSchedule(scheduleDecisionModifyForm)
        // CmnScheduleCalendarServiceからカレンダー, 年月, 最終日を取得
        val cmnScheduleCalendarBean: CmnScheduleCalendarBean = cmnScheduleCalendarService.generateCalendar(scheduleDecisionModifyForm.ym)
        // 年, 月, 日をそれぞれ配列で取得
        val ymdArray = calcYmdArray(scheduleDecisionModifyForm.ym!!, scheduleDecisionModifyForm.day!!)
        // 1日分の予定スケジュールをユーザ毎に取得
        val schedulePreDayList = selectSchedulePreDay(scheduleDecisionModifyForm.ym!!, scheduleDecisionModifyForm.day!!)
        // 1日分の確定スケジュールをユーザ毎に取得
        val scheduleUserList = selectScheduleDay(scheduleDecisionModifyForm.ym!!, scheduleDecisionModifyForm.day!!)
        // スケジュール時間区分を取得
        val scheduleTimeEntity = selectScheduleTime(cmnScheduleCalendarBean.lastDateYmd)
        // 未退職ユーザを全て取得
        val usersDbList: List<UserEntity> = selectUserNotDelFlg()
        // 確定スケジュールに新規登録可能ユーザのみに変換
        val userList: List<UserEntity> = calcUserRecordableSchedule(scheduleUserList, usersDbList)

        // Beanにセット
        val scheduleDecisionModifyBean = ScheduleDecisionModifyModifyBean()
        scheduleDecisionModifyBean.isUpdate = isUpdate
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


    /**
     * [Repository] 確定スケジュール更新処理
     *
     * scheduleDecisionModifyFormから確定スケジュールに新規でユーザとスケジュール及び更新するユーザとスケジュールを登録する<br>
     * ただし、確定スケジュールに新規登録するユーザがいない(ユーザが指定されていないまたはスケジュールを登録していない)ときは登録されない<br>
     * 既に確定スケジュールに登録済みのユーザはスケジュールが登録していないときでもその情報が登録される
     *
     * @param scheduleDecisionModifyForm Formクラス
     * @return boolean 更新判定<br>
     * true: 更新したとき<br>
     * false: 更新に失敗したとき
     */
    private fun updateSchedule(scheduleDecisionModifyForm: ScheduleDecisionModifyForm): Boolean {

        //--------------------------
        // 新規確定スケジュール追加
        //--------------------------

        // 登録する日付を取得
        val ym = scheduleDecisionModifyForm.ym
        val day = String.format("%02d", scheduleDecisionModifyForm.day!!.toInt())

        // 新規登録するユーザIDとスケジュールを取得
        val addUser: String = scheduleDecisionModifyForm.addScheduleArrayFormatString()
        val addScheduleArray: Array<String?> = scheduleDecisionModifyForm.addScheduleArray

        // 新規で登録するスケジュールがあるとき
        if (addScheduleArray.contains(Const.SCHEDULE_RECORDED) && addUser.isNotEmpty()) {

            // 対象の年月から新規で登録するユーザの確定スケジュールを検索する
            var addNewUserScheduleEntity: ScheduleEntity? = scheduleRepository.selectSchedule(ym, addUser)

            // 該当の年月に新規で登録するユーザが未登録のとき、値をインスタンス化して値をセットするする
            if (addNewUserScheduleEntity == null) {
                addNewUserScheduleEntity = ScheduleEntity()
                addNewUserScheduleEntity.ym = ym
                addNewUserScheduleEntity.user = addUser
            }

            // 新規で登録するユーザのスケジュール情報を取得
            val addSchedule = scheduleDecisionModifyForm.addScheduleArrayFormatString()

            // 登録する日付(day)に応じてスケジュール情報をセットする
            if ("01" == day) {
                addNewUserScheduleEntity.day1 = addSchedule
            } else if ("02" == day) {
                addNewUserScheduleEntity.day2 = addSchedule
            } else if ("03" == day) {
                addNewUserScheduleEntity.day3 = addSchedule
            } else if ("04" == day) {
                addNewUserScheduleEntity.day4 = addSchedule
            } else if ("05" == day) {
                addNewUserScheduleEntity.day5 = addSchedule
            } else if ("06" == day) {
                addNewUserScheduleEntity.day6 = addSchedule
            } else if ("07" == day) {
                addNewUserScheduleEntity.day7 = addSchedule
            } else if ("08" == day) {
                addNewUserScheduleEntity.day8 = addSchedule
            } else if ("09" == day) {
                addNewUserScheduleEntity.day9 = addSchedule
            } else if ("10" == day) {
                addNewUserScheduleEntity.day10 = addSchedule
            } else if ("11" == day) {
                addNewUserScheduleEntity.day11 = addSchedule
            } else if ("12" == day) {
                addNewUserScheduleEntity.day12 = addSchedule
            } else if ("13" == day) {
                addNewUserScheduleEntity.day13 = addSchedule
            } else if ("14" == day) {
                addNewUserScheduleEntity.day14 = addSchedule
            } else if ("15" == day) {
                addNewUserScheduleEntity.day15 = addSchedule
            } else if ("16" == day) {
                addNewUserScheduleEntity.day16 = addSchedule
            } else if ("17" == day) {
                addNewUserScheduleEntity.day17 = addSchedule
            } else if ("18" == day) {
                addNewUserScheduleEntity.day18 = addSchedule
            } else if ("19" == day) {
                addNewUserScheduleEntity.day19 = addSchedule
            } else if ("20" == day) {
                addNewUserScheduleEntity.day20 = addSchedule
            } else if ("21" == day) {
                addNewUserScheduleEntity.day21 = addSchedule
            } else if ("22" == day) {
                addNewUserScheduleEntity.day22 = addSchedule
            } else if ("23" == day) {
                addNewUserScheduleEntity.day23 = addSchedule
            } else if ("24" == day) {
                addNewUserScheduleEntity.day24 = addSchedule
            } else if ("25" == day) {
                addNewUserScheduleEntity.day25 = addSchedule
            } else if ("26" == day) {
                addNewUserScheduleEntity.day26 = addSchedule
            } else if ("27" == day) {
                addNewUserScheduleEntity.day27 = addSchedule
            } else if ("28" == day) {
                addNewUserScheduleEntity.day28 = addSchedule
            } else if ("29" == day) {
                addNewUserScheduleEntity.day29 = addSchedule
            } else if ("30" == day) {
                addNewUserScheduleEntity.day30 = addSchedule
            } else if ("31" == day) {
                addNewUserScheduleEntity.day31 = addSchedule
            }

            // 新規で追加したスケジュールをDB更新
            scheduleRepository.save(addNewUserScheduleEntity)
        }


        //-------------------------
        // 登録済みスケジュール更新
        //-------------------------

        // 既存で登録されたユーザと更新スケジュールを取得
        val userArray2 = scheduleDecisionModifyForm.userArray
        val scheduleArray2 = scheduleDecisionModifyForm.scheduleArray

        // 該当する年月の全てのユーザの確定スケジュールをすべて取得
        val scheduleList: MutableList<ScheduleEntity> = scheduleRepository.selectSchedule(ym).toMutableList()

        // 該当の年月に登録されているユーザだけループ
        for (i in scheduleList.indices) {
            val entity = scheduleList[i]

            // 既存で登録されているユーザだけループ
            for (j in userArray2!!.indices) {

                // 既に登録済みのユーザ取得
                val userArray = userArray2[j]

                // 更新するユーザが一致したとき
                if (userArray[0] == entity.user) {

                    // DBに更新するスケジュールを格納する
                    var schedule = ""

                    // 対象のユーザのスケジュール時間区分ごとの更新スケジュールを配列で所得
                    val scheduleArray = scheduleArray2!![j]

                    // スケジュール時間区分ごとの更新スケジュールだけループ
                    for (k in 0 until Const.SCHEDULE_RECORDABLE_MAX_DIVISION) {

                        if (scheduleArray.size <= k) {
                            // スケジュール時間区分が存在しないとき、未登録情報を格納
                            schedule += Const.SCHEDULE_NOT_RECORDED
                        } else if (Const.SCHEDULE_RECORDED != scheduleArray[k]) {
                            // スケジュールが登録されていないとき、未登録情報を格納
                            schedule += Const.SCHEDULE_NOT_RECORDED
                        } else {
                            // スケジュールが登録されているとき、登録情報を格納
                            schedule += Const.SCHEDULE_RECORDED
                        }
                    }

                    // 該当の日付のスケジュールを更新する
                    if ("01" == day) {
                        entity.day1 = schedule
                    } else if ("02" == day) {
                        entity.day2 = schedule
                    } else if ("03" == day) {
                        entity.day3 = schedule
                    } else if ("04" == day) {
                        entity.day4 = schedule
                    } else if ("05" == day) {
                        entity.day5 = schedule
                    } else if ("06" == day) {
                        entity.day6 = schedule
                    } else if ("07" == day) {
                        entity.day7 = schedule
                    } else if ("08" == day) {
                        entity.day8 = schedule
                    } else if ("09" == day) {
                        entity.day9 = schedule
                    } else if ("10" == day) {
                        entity.day10 = schedule
                    } else if ("11" == day) {
                        entity.day11 = schedule
                    } else if ("12" == day) {
                        entity.day12 = schedule
                    } else if ("13" == day) {
                        entity.day13 = schedule
                    } else if ("14" == day) {
                        entity.day14 = schedule
                    } else if ("15" == day) {
                        entity.day15 = schedule
                    } else if ("16" == day) {
                        entity.day16 = schedule
                    } else if ("17" == day) {
                        entity.day17 = schedule
                    } else if ("18" == day) {
                        entity.day18 = schedule
                    } else if ("19" == day) {
                        entity.day19 = schedule
                    } else if ("20" == day) {
                        entity.day20 = schedule
                    } else if ("21" == day) {
                        entity.day21 = schedule
                    } else if ("22" == day) {
                        entity.day22 = schedule
                    } else if ("23" == day) {
                        entity.day23 = schedule
                    } else if ("24" == day) {
                        entity.day24 = schedule
                    } else if ("25" == day) {
                        entity.day25 = schedule
                    } else if ("26" == day) {
                        entity.day26 = schedule
                    } else if ("27" == day) {
                        entity.day27 = schedule
                    } else if ("28" == day) {
                        entity.day28 = schedule
                    } else if ("29" == day) {
                        entity.day29 = schedule
                    } else if ("30" == day) {
                        entity.day30 = schedule
                    } else if ("31" == day) {
                        entity.day31 = schedule
                    }

                    // 更新後のスケジュールをセットする
                    scheduleList[i] = entity
                }
            }
        }

        // 更新した確定スケジュールを全てDB更新
        scheduleRepository.saveAll(scheduleList)
        return true
    }
}