package com.shift.domain.service

import com.shift.common.Const
import com.shift.domain.model.bean.CalendarBean
import com.shift.domain.model.bean.CmnScheduleCalendarBean
import com.shift.domain.model.entity.ScheduleEntity
import com.shift.domain.model.entity.ScheduleTimeEntity
import com.shift.domain.repository.ScheduleRepository
import com.shift.domain.repository.ScheduleTimeRepository
import com.shift.domain.service.common.CmnScheduleCalendarService
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
class CalendarService: BaseService() {

    @Autowired
    private lateinit var scheduleRepository: ScheduleRepository

    @Autowired
    private lateinit var scheduleTimeRepository: ScheduleTimeRepository

    @Autowired
    private lateinit var cmnScheduleCalendarService: CmnScheduleCalendarService


    /**
     * [Service] カレンダー表示機能 (/calendar)
     *
     * @param
     * @return CalendarBean
     */
    fun calendar(ym: String?, loginUser: String): CalendarBean {

        // 共通サービスより指定された年月のカレンダーと日付情報を取得
        val cmnScheduleCalendarBean: CmnScheduleCalendarBean = cmnScheduleCalendarService.generateCalendar(ym)
        // 指定された年月の確定スケジュールを取得
        val scheduleEntity: ScheduleEntity? = selectSchedule(cmnScheduleCalendarBean.nowYm, loginUser)
        // 指定された年月のスケジュール時間を取得
        val scheduleTimeEntity: ScheduleTimeEntity? = selectScheduleTime(cmnScheduleCalendarBean.lastDateYmd)

        // Beanにセット
        val calendarBean: CalendarBean = CalendarBean()
        calendarBean.year = cmnScheduleCalendarBean.year
        calendarBean.month = cmnScheduleCalendarBean.month
        calendarBean.calendarList = cmnScheduleCalendarBean.calendarList
        calendarBean.nowYm = cmnScheduleCalendarBean.nowYm
        calendarBean.beforeYm = cmnScheduleCalendarBean.beforeYm
        calendarBean.afterYm = cmnScheduleCalendarBean.afterYm
        calendarBean.scheduleEntity = scheduleEntity
        return calendarBean
    }


    /**
     * 確定スケジュール判定Array取得処理
     *
     * scheduleEntityとscheduleTimeListから登録済みのスケジュールとスケジュール時間区分を取得し、確定スケジュールが登録されているかを判別する<br>
     * Listのエレメント(Boolean[])には1日ごとのスケジュール時間区分で登録済みかを判別する
     *
     * @param scheduleEntity DBから取得したscheduleEntity
     * @param scheduleTime DBから取得したScheduleTimeEntity
     * @return Array<Array<Boolean?>>　<br>
     * 1: 日付(1～31日), 2: スケジュール時間(スケジュール登録可能数)<br>
     * true: スケジュール登録済み, false: スケジュール未登録
     */
    private fun calcIsScheduleRecordedArray(scheduleEntity: ScheduleEntity?, scheduleTime: ScheduleTimeEntity): Array<Array<Boolean?>> {

        //scheduleEntityがnullでないとき、scheduleEntityを代入
        var trimScheduleEntity: ScheduleEntity = ScheduleEntity()
        if (scheduleEntity != null) {
            trimScheduleEntity = scheduleEntity
        }

        //スケジュール登録を判別するBoolean[日付][スケジュール時間]の配列
        val isScheduleDisplayArray: Array<Array<Boolean?>> = Array(31) {
            arrayOfNulls(Const.SCHEDULE_RECORDABLE_MAX_DIVISION)
        }

        //trimSchedulePreEntityに登録されている値(1ヵ月分)をListで取得
        val scheduleList: List<String?> = trimScheduleEntity.getScheduleDayList()

        //スケジュール登録済みかを判定する共通Logicクラス
        val cmnScheduleLogic = CmnScheduleLogic()

        //scheduleListの要素数(1ヵ月の日付)の回数だけループする
        for (i in scheduleList.indices) {

            //スケジュールが登録されているかどうかを判別する配列(1日ごとのスケジュールにおいて要素0 -> scheduleTimeList(0), 要素1 -> scheduleTimeList(1)...)
            val isScheduleRecordedArray: Array<Boolean?> = cmnScheduleLogic.toIsScheduleRecordedArrayBySchedule(
                scheduleList[i], scheduleTime
            )

            //isScheduleDisplayArrayの1次元目i(日付)を指定し、2次元目(スケジュール時間区分)にセットする
            isScheduleDisplayArray[i] = isScheduleRecordedArray
        }
        return isScheduleDisplayArray
    }


    /**
     * [Repository] ユーザ確定スケジュール検索処理
     *
     * 指定された年月からユーザーの1ヵ月分のスケジュール予定を取得する<br>
     * ただし、登録済みのスケジュールがないときはnullとなる<br>
     * また、日付が存在しない日(2月 -> 30, 31日etc)は必ず登録されていない
     *
     * @param ym 取得したい確定スケジュールの年月(YYYYMM)
     * @param userId 取得したいユーザID
     * @return ScheduleEntity 指定されたユーザの1ヶ月の確定スケジュール
     */
    private fun selectSchedule(ym: String?, userId: String?): ScheduleEntity? {
        return scheduleRepository.findByYmAndUser(ym, userId)
    }


    /**
     * [Repository] スケジュール時間区分検索処理
     *
     * 取得したい日付(ymd)から該当するスケジュール時間区分を取得する<br>
     * また、現在日(ymd)に該当するスケジュール時間区分が複数登録されているときは最新のスケジュール時間区分が取得される<br>
     * ただし、スケジュール時間区分が何も登録されていないときはnullとなる
     *
     *
     * @param ymd 取得したいスケジュール時間区分の日付(YYYYMMDD)
     * @return ScheduleTimeEntity 指定した年月のスケジュール時間区分
     */
    private fun selectScheduleTime(ymd: String?): ScheduleTimeEntity? {
        return scheduleTimeRepository.selectScheduleTimeByYmd(ymd)
    }
}