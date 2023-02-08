package com.shift.domain.service

import com.shift.common.CmnScheduleLogic
import com.shift.common.CommonLogic
import com.shift.common.Const
import com.shift.domain.model.bean.HomeBean
import com.shift.domain.model.bean.collection.ScheduleDayBean
import com.shift.domain.model.entity.ScheduleEntity
import com.shift.domain.model.entity.ScheduleTimeEntity
import com.shift.domain.model.entity.UserEntity
import com.shift.domain.repository.ScheduleRepository
import com.shift.domain.repository.ScheduleTimeRepository
import com.shift.domain.repository.UserRepository
import com.shift.domain.service.common.CmnNewsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate


/**
 * @author saito
 *
 */
@Service
@Transactional
class HomeService: BaseService() {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var scheduleRepository: ScheduleRepository

    @Autowired
    private lateinit var scheduleTimeRepository: ScheduleTimeRepository

    @Autowired
    private lateinit var cmnNewsService: CmnNewsService


    /**
     * [Service] ホーム画面表示機能 (/home)
     *
     * @param ymd 1週間カレンダーに表示したい日付 (Not required)
     * @param loginUser Authenticationから取得したログインユーザID
     * @return HomeBean
     */
    fun home(ymd: String?, loginUser: String?): HomeBean {

        // 共通サービスから表示可能なお知らせを取得
        val cmnNewsBean = cmnNewsService.generateDisplayNews()
        // ユーザ情報を取得
        val userEntity: UserEntity? = selectUser(loginUser)
        // 指定された日付とその日付の1週間前後の日付をymdで取得
        val nowBeforeAfterWeekYmdArray: Array<String> = calcNowBeforeAfterWeekYmdArray(ymd)
        // 現在の日付から1日ごと(1週間分)に確定スケジュールを取得
        val scheduleList: List<ScheduleEntity?> = selectScheduleForHome(nowBeforeAfterWeekYmdArray[0], loginUser)
        // 現在の日付から1日ごと(1週間分)にスケジュール時間区分を取得
        val scheduleTimeList: List<ScheduleTimeEntity?> = selectScheduleTimeForHome(nowBeforeAfterWeekYmdArray[0])
        // 取得したスケジュールから表示するスケジュールを1週間分のカレンダーで取得
        val scheduleDayList: List<ScheduleDayBean> = generateCalendarForHome(scheduleList, scheduleTimeList, nowBeforeAfterWeekYmdArray[0])

        // Beanにセット
        val homeBean = HomeBean()
        homeBean.beforeWeekYmd = nowBeforeAfterWeekYmdArray[1]
        homeBean.afterWeekYmd = nowBeforeAfterWeekYmdArray[2]
        homeBean.userEntity = userEntity
        homeBean.newsList = cmnNewsBean.newsList
        homeBean.scheduleDayList = scheduleDayList
        return homeBean
    }


    /**
     * 前翌週日付計算処理
     *
     * 指定された日付から指定された日付, 1週間前の日付, 1週間後の日付をymd(YYYYMMDD)で取得する<br>
     * ただし、指定された日付がないまたは指定された日付が異常な値だった場合は現在の日付となる
     *
     * @param ymd RequestParameter 日付ymd
     * @return Array<String> ymdフォーマットに変換された日付<br>
     * 0: 指定された日付, 1: 指定された日付の1週間前の日付, 2: 指定された日付の1週間後の日付
     */
    private fun calcNowBeforeAfterWeekYmdArray(ymd: String?): Array<String> {

        // 指定された日付のLocalDateを取得
        val commonLogic = CommonLogic()
        var ymdLd: LocalDate? = commonLogic.getLocalDate(ymd)

        // ymdが指定されていないまたは指定した日付が取得できなかったとき、現在の日付のLocalDateを取得
        if (ymdLd == null) {
            ymdLd = LocalDate.now()
        }

        // 指定された日付の1週間前後の日付をLocalDateで取得
        val afterWeekLd = ymdLd!!.plusWeeks(1)
        val beforeWeekLd = ymdLd.minusWeeks(1)

        // 指定された日付と1週間後の日付をymdで取得
        val nowYmd = commonLogic.toStringYmd(ymdLd.year, ymdLd.monthValue, ymdLd.dayOfMonth)
        val afterWeekYmd = commonLogic.toStringYmd(afterWeekLd.year, afterWeekLd.monthValue, afterWeekLd.dayOfMonth)
        val beforeWeekYmd = commonLogic.toStringYmd(beforeWeekLd.year, beforeWeekLd.monthValue, beforeWeekLd.dayOfMonth)

        // 配列に格納し、返す
        return arrayOf(nowYmd, beforeWeekYmd, afterWeekYmd)
    }


    /**
     * ホームカレンダー取得処理
     *
     * 取得したスケジュール情報と指定された日付からホーム画面用のカレンダーに変換する<br>
     * ただし、カレンダー生成時に年月をまたぐ場合は、スケジュール情報は要素数は2でないとならない<br>
     * 各エレメントは1日ごとに日付, 確定スケジュール判定, スケジュール時間区分が格納される
     *
     * @param scheduleEntityList 確定スケジュール(要素数は2以下)
     * @param scheduleTimeList スケジュール時間区分(要素数は2以下)
     * @param nowYmd カレンダー表示開始日付
     * @return List<ScheduleDayBean> ホーム画面用のカレンダー<br>
     * エレメント: 一日ごとのスケジュール情報
     **/
    private fun generateCalendarForHome(scheduleEntityList: List<ScheduleEntity?>, scheduleTimeList: List<ScheduleTimeEntity?>, nowYmd: String?): List<ScheduleDayBean> {

        // 1日ごとのスケジュール情報を格納するための変数
        val scheduleDayList: MutableList<ScheduleDayBean> = ArrayList<ScheduleDayBean>()

        // 指定された日付をLocalDateで取得
        val cmnScheduleLogic = CmnScheduleLogic()
        val commonLogic = CommonLogic()
        val nowYmdLd: LocalDate? = commonLogic.getLocalDate(nowYmd)

        // 現在位からホーム画面に表示する日付までループ
        for (i in 0 until Const.SCHEDULE_HOME_CALENDAR_DISPLAY_LIMIT_DAY) {

            // ループ中の日付をLocalDateで取得
            val localDate: LocalDate? = nowYmdLd!!.plusDays(i)

            // 開始日の年月とループ中の年月が同じとき
            if (nowYmdLd.dayOfMonth == localDate!!.dayOfMonth) {

               // カレンダーの日付に該当する確定スケジュールを取得
                val scheduleList = scheduleEntityList[0]!!.getScheduleDayList()
                val day = localDate.dayOfMonth
                val schedule = scheduleList[day - 1]

                // 確定スケジュールを登録済みか判定
                val scheduleTimeEntity = scheduleTimeList[0]
                val isScheduleRecordedArray: Array<Boolean?> = cmnScheduleLogic.toIsScheduleArray(schedule, scheduleTimeEntity)

                // スケジュール情報をBeanにセットし、追加する
                val scheduleDayBean = ScheduleDayBean()
                scheduleDayBean.isScheduleRecordedArray = isScheduleRecordedArray
                scheduleDayBean.scheduleTimeEntity = scheduleTimeEntity
                scheduleDayBean.week = localDate.dayOfWeek.value
                scheduleDayBean.month = localDate.monthValue
                scheduleDayBean.day = day
                scheduleDayList.add(scheduleDayBean)
                continue
            } else {
                // 開始日の年月とループ中の年月が異なるとき

                // カレンダーの日付に該当する確定スケジュールを取得
                val scheduleList = scheduleEntityList[1]!!.getScheduleDayList()
                val day = localDate.dayOfMonth
                val schedule = scheduleList[day - 1]

                // 確定スケジュールを登録済みか判定
                val scheduleTimeEntity = scheduleTimeList[1]
                val isScheduleRecordedArray: Array<Boolean?> = cmnScheduleLogic.toIsScheduleArray(schedule, scheduleTimeEntity)

                // スケジュール情報をBeanにセットし、追加する
                val scheduleDayBean = ScheduleDayBean()
                scheduleDayBean.isScheduleRecordedArray = isScheduleRecordedArray
                scheduleDayBean.scheduleTimeEntity = scheduleTimeEntity
                scheduleDayBean.week = localDate.dayOfWeek.value
                scheduleDayBean.month = localDate.monthValue
                scheduleDayBean.day = day
                scheduleDayList.add(scheduleDayBean)
                continue
            }
        }
        return scheduleDayList
    }


    /**
     * [Repository] ユーザ検索処理
     *
     * userIdと一致するユーザを取得する<br>
     * ただし、一致するユーザーがいない場合はnullとなる
     *
     * @param userId 取得したいユーザのユーザID
     * @return UserEntity ユーザ情報
     */
    private fun selectUser(userId: String?): UserEntity? {
        return userRepository.selectUser(userId)
    }


    /**
     * [Repository] ホーム画面の確定スケジュール検索処理
     *
     * 日付から該当する年月の確定スケジュールを取得する<br>
     * ただし、ホーム画面に表示する上限の日付が年月をまたいでいるときは2種類の確定スケジュールが取得される<br>
     * また、確定スケジュールが何も登録されていないときはエレメントは必ずnullとなる
     *
     * @param nowYmd ホーム画面に表示させる確定スケジュールの下限の日付(YYYYMMDD)
     * @param userId 取得したい確定スケジュールのユーザID
     * @return List<ScheduleEntity?> 確定スケジュール<br>
     * 同じ年月: 要素数1, 年月をまたいでいるとき: 要素数2<br>
     * 該当する年月の確定スケジュールが存在しているとき: エレメント not null, 存在していないとき: エレメント null
     **/
    private fun selectScheduleForHome(nowYmd: String?, userId: String?): List<ScheduleEntity?> {

        // 確定スケジュールを格納するための変数
        val scheduleEntityList: MutableList<ScheduleEntity?> = ArrayList()

        // 現在の日付とホーム画面に表示する上限の日付をLocalDateで取得
        val commonLogic = CommonLogic()
        val nowLd = commonLogic.getLocalDate(nowYmd)
        val afterWeekLd = nowLd!!.plusDays(Const.SCHEDULE_HOME_CALENDAR_DISPLAY_LIMIT_DAY)

        // ホーム画面に表示する上限の日付が月をまたいでいるとき
        if (nowLd.dayOfMonth != afterWeekLd.dayOfMonth) {

            // 現在の日付とホーム画面に表示する上限の日付の年月の確定スケジュールを取得
            scheduleEntityList.add(scheduleRepository.selectSchedule(nowYmd, userId))
            val afterWeekYmd = commonLogic.toStringYmd(afterWeekLd.year, afterWeekLd.monthValue, afterWeekLd.dayOfMonth)
            scheduleEntityList.add(scheduleRepository.selectSchedule(afterWeekYmd, userId))
            return scheduleEntityList
        }

        // 現在の日付の確定スケジュールを取得
        scheduleEntityList.add(scheduleRepository.selectSchedule(nowYmd, userId))
        return scheduleEntityList
    }


    /**
     * [Repository] ホーム画面のスケジュール時間区分検索処理
     *
     *
     * 日付から該当する年月のスケジュール時間区分を取得する<br>
     * ただし、ホーム画面に表示する上限の日付が年月をまたいでいるときは2種類のスケジュール時間区分が取得される<br>
     * また、スケジュール時間区分が何も登録されていないときはエレメントは必ずnullとなる
     *
     *
     * @param nowYmd ホーム画面に表示させるスケジュール時間区分の下限の日付(YYYYMMDD)
     * @return List<ScheduleTimeEntity?> スケジュール時間区分<br>
     * 同じ年月: 要素数1, 年月をまたいでいるとき: 要素数2<br>
     * 該当する年月のスケジュール時間区分が存在しているとき: エレメント not null, 存在していないとき: エレメント null
     **/
    private fun selectScheduleTimeForHome(nowYmd: String): List<ScheduleTimeEntity?> {

        // スケジュール時間区分を格納するための変数
        val scheduleTimeEntityList: MutableList<ScheduleTimeEntity?> = ArrayList()

        // 現在の日付とホーム画面に表示する上限の日付をLocalDateで取得
        val commonLogic = CommonLogic()
        val nowLd = commonLogic.getLocalDate(nowYmd)
        val afterWeekLd = nowLd!!.plusDays(Const.SCHEDULE_HOME_CALENDAR_DISPLAY_LIMIT_DAY)

        // ホーム画面に表示する上限の日付が月をまたいでいるとき
        if (nowLd.dayOfMonth != afterWeekLd.dayOfMonth) {

            // 現在の日付とホーム画面に表示する上限の日付の年月の確定スケジュールを取得
            scheduleTimeEntityList.add(scheduleTimeRepository!!.selectScheduleTime(nowYmd))
            val afterWeekYmd = commonLogic.toStringYmd(afterWeekLd.year, afterWeekLd.monthValue, afterWeekLd.dayOfMonth)
            scheduleTimeEntityList.add(scheduleTimeRepository.selectScheduleTime(afterWeekYmd))
            return scheduleTimeEntityList
        }

        // 現在の日付の確定スケジュールを取得
        scheduleTimeEntityList.add(scheduleTimeRepository!!.selectScheduleTime(nowYmd))
        return scheduleTimeEntityList
    }
}