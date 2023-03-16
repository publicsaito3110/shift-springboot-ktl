package com.shift.domain.service

import com.shift.domain.model.bean.CmnScheduleCalendarBean
import com.shift.domain.model.bean.CmnScheduleUserNameBean
import com.shift.domain.model.bean.ScheduleDecisionBean
import com.shift.domain.repository.ScheduleRepository
import com.shift.domain.repository.ScheduleTimeRepository
import com.shift.domain.repository.UserRepository
import com.shift.domain.service.common.CmnScheduleCalendarService
import com.shift.domain.service.common.CmnScheduleUserNameService
import org.springframework.beans.factory.annotation.Autowired
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
    private lateinit var scheduleRepository: ScheduleRepository

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
}