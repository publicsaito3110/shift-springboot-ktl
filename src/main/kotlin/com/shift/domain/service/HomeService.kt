package com.shift.domain.service

import com.shift.common.CmnScheduleLogic
import com.shift.common.CommonLogic
import com.shift.domain.model.entity.ScheduleEntity
import com.shift.domain.model.entity.ScheduleTimeEntity
import com.shift.domain.model.entity.UserEntity
import com.shift.domain.repository.ScheduleRepository
import com.shift.domain.repository.ScheduleTimeRepository
import com.shift.domain.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
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
    fun home(ymd: String, loginUser: String): HomeBean {

        //Service内の処理を実行
        val userEntity = selectUserByLoginUser(loginUser)
        //CmnNewsService(共通サービス)から表示可能なお知らせを取得
        val cmnNewsBean: CmnNewsBean = cmnNewsService.generateDisplayNowNews()
        //指定された日付とその日付の1週間前後の日付をymdで取得
        val nowBeforeAfterWeekYmdArray = calcNowBeforeAfterWeekYmdArray(ymd)
        //現在の日付から1日ごと(1週間分)に確定スケジュールを取得
        val oneweekScheduleList = selectScheduleForOnweek(nowBeforeAfterWeekYmdArray[0], loginUser)
        //現在の日付から1日ごと(1週間分)にスケジュール時間区分を取得
        val oneweekScheduleTimeList = selectScheduleTimeForOneweek(nowBeforeAfterWeekYmdArray[0])
        //取得したスケジュールから表示するスケジュールを1週間分のカレンダーで取得
        val dayScheduleList: List<HomeDayScheduleBean> = generateOneWeekCalendar(
            oneweekScheduleList, oneweekScheduleTimeList,
            nowBeforeAfterWeekYmdArray[0]
        )

        //Beanにセット
        val homeBean = HomeBean()
        homeBean.setBeforeWeekYmd(nowBeforeAfterWeekYmdArray[1])
        homeBean.setAfterWeekYmd(nowBeforeAfterWeekYmdArray[2])
        homeBean.setUserEntity(userEntity)
        homeBean.setNewsList(cmnNewsBean.getNewsList())
        homeBean.setDayScheduleList(dayScheduleList)
        return homeBean
    }


    /**
     * 前翌週日付計算処理
     *
     *
     * 指定された日付から指定された日付と1週間前の日付と1週間後の日付をymd(YYYYMMDD)で取得する<br></br>
     * ただし、指定された日付がないまたは指定された日付が異常な値だった場合は現在の日付となる<br></br>
     * String[0]が指定された日付, String[1]が指定された日付の1週間前の日付, String[2]が指定された日付の1週間後の日付
     *
     *
     * @param ymd RequestParameter 日付ymd
     * @return String[] ymdフォーマットに変換された日付<br></br>
     * String[0]が指定された日付, String[1]が指定された日付の1週間前の日付, String[2]が指定された日付の1週間後の日付
     */
    private fun calcNowBeforeAfterWeekYmdArray(ymd: String): Array<String> {

        //指定された日付のLocalDateを取得
        val commonLogic = CommonLogic()
        var ymdLd: LocalDate? = commonLogic.getLocalDateByYmd(ymd)

        //ymdが指定されていないまたは指定した日付が取得できなかったとき、現在の日付のLocalDateを取得
        if (ymdLd == null) {
            ymdLd = LocalDate.now()
        }

        //指定された日付の1週間後のLocalDateを取得
        val afterWeekLd = ymdLd!!.plusWeeks(1)
        val beforeWeekLd = ymdLd.minusWeeks(1)

        //指定された日付と1週間後の日付をymdで取得
        val nowYmd: String =
            commonLogic.toStringYmdByYearMonthDay(ymdLd.year, ymdLd.monthValue, ymdLd.dayOfMonth)
        val afterWeekYmd: String =
            commonLogic.toStringYmdByYearMonthDay(afterWeekLd.year, afterWeekLd.monthValue, afterWeekLd.dayOfMonth)
        val beforeWeekYmd: String =
            commonLogic.toStringYmdByYearMonthDay(beforeWeekLd.year, beforeWeekLd.monthValue, beforeWeekLd.dayOfMonth)

        //配列に格納し、返す
        return arrayOf(nowYmd, beforeWeekYmd, afterWeekYmd)
    }


    /**
     * 1週間カレンダー取得処理
     *
     *
     * 取得したスケジュールと指定された日付から1週間の確定スケジュールをカレンダー形式に変換する<br></br>
     * 1日ごとに日付, 確定スケジュール判定, スケジュール時間区分が格納される
     *
     *
     * @param oneweekScheduleList DBから取得したList
     * @param oneweekScheduleTimeList DBから取得したList
     * @param nowYmd ymdフォーマットで取得した日付
     * @return List<HomeDayScheduleBean> 1週間のスケジュール情報<br></br>
     * フィールド(List&lt;HomeDayScheduleBean&gt;)<br></br>
     * month, day, week, isScheduleRecordedArray, scheduleTimeEntity
    </HomeDayScheduleBean> */
    private fun generateOneWeekCalendar(
        oneweekScheduleList: List<ScheduleEntity?>,
        oneweekScheduleTimeList: List<ScheduleTimeEntity?>,
        nowYmd: String
    ): List<HomeDayScheduleBean> {

        //1週間のスケジュール情報を格納するための変数
        val dayScheduleList: MutableList<HomeDayScheduleBean> = ArrayList<HomeDayScheduleBean>()

        //スケジュールが登録されているか判定するクラス
        val cmnScheduleLogic = CmnScheduleLogic()

        //指定された日付をLocalDateで取得
        val commonLogic = CommonLogic()
        val nowYmdLd: LocalDate = commonLogic.getLocalDateByYmd(nowYmd)

        //1日ごと(1週間分)に取得した確定スケジュールだけループする
        for (i in 0 until Const.HOME_DISPLAY_SCHEDULE_DAY) {

            //現在のループ回数の要素目のスケジュールをScheduleEntityに格納
            var scheduleEntity = oneweekScheduleList[i]

            //スケジュールが登録されていなかったとき、インスタンス化
            if (scheduleEntity == null) {
                scheduleEntity = ScheduleEntity()
            }

            //スケジュールから日付ごとのスケジュールをListで取得
            val scheduleDayList: List<String> = scheduleEntity.getDayList()

            //ループ回数 + 指定された日付の日をintで取得
            val localDate = nowYmdLd.plusDays(i.toLong())
            val day = localDate.dayOfMonth

            //日付からスケジュールを取得
            val index = day - 1
            val schedule = scheduleDayList[index]

            //日付からスケジュールが登録されているかを判定した配列を取得
            val scheduleTimeEntity = oneweekScheduleTimeList[i]
            val isScheduleRecordedArray: Array<Boolean> = cmnScheduleLogic.toIsScheduleRecordedArrayBySchedule(
                schedule,
                oneweekScheduleTimeList[i]
            )

            //スケジュール情報をHomeDayScheduleBeanにセットし、Listへ格納
            val homeDayScheduleBean = HomeDayScheduleBean()
            homeDayScheduleBean.setIsScheduleRecordedArray(isScheduleRecordedArray)
            homeDayScheduleBean.setScheduleTimeEntity(scheduleTimeEntity)
            homeDayScheduleBean.setWeek(localDate.dayOfWeek.value)
            homeDayScheduleBean.setMonth(localDate.monthValue)
            homeDayScheduleBean.setDay(day)
            dayScheduleList.add(homeDayScheduleBean)
        }
        return dayScheduleList
    }


    /**
     * [DB]ユーザ検索処理
     *
     *
     * loginUserと一致するユーザを取得する<br></br>
     * ただし、一致するユーザーがいない場合はnullとなる
     *
     *
     * @param loginUser Authenticationから取得したユーザID
     * @return UserEntity<br></br>
     * フィールド(UserEntity)<br></br>
     * id, name, nameKana, gender, password, address, tel, email, note, admin_flg, del_flg
     */
    private fun selectUserByLoginUser(loginUser: String): UserEntity? {
        val userEntityOpt: Optional<UserEntity> = userRepository!!.findById(loginUser)

        //ユーザを取得できなかったとき
        return if (!userEntityOpt.isPresent()) {
            null
        } else userEntityOpt.get()

        //UserEntityを返す
    }


    /**
     * [DB]1日ごとユーザの確定スケジュール検索処理
     *
     *
     * 取得したい日付(ymd)から該当する確定スケジュールを1日ごとに(1週間分)取得する<br></br>
     * ただし、スケジュールが何も登録されていないときはエレメントはnullとなる
     *
     *
     * @param year LocalDateから取得した年(int)
     * @param month LocalDateから取得した月(int)
     * @param loginUser Authenticationから取得したユーザID
     * @return List<ScheduleEntity> <br></br>
     * フィールド(List&lt;ScheduleEntity&gt;)<br></br>
     * id, ym, user, 1, 2, 3, 4, 5... 30, 31
    </ScheduleEntity> */
    private fun selectScheduleForOnweek(nowYmd: String, loginUser: String): List<ScheduleEntity?> {

        //1日ごと(1週間分)のスケジュールを格納するための変数
        val ScheduleEntityList: MutableList<ScheduleEntity?> = ArrayList()

        //現在の日付をLocalDateで取得
        val commonLogic = CommonLogic()
        val nowYmdLd: LocalDate = commonLogic.getLocalDateByYmd(nowYmd)

        //7回(1週間分)ループする
        for (i in 0 until Const.HOME_DISPLAY_SCHEDULE_DAY) {

            //現在の日付 + ループ回数日の年月を取得する
            val localDate = nowYmdLd.plusDays(i.toLong())
            val ym: String = commonLogic.toStringYmFormatSixByYearMonth(localDate.year, localDate.monthValue)

            //取得した年月からスケジュールを取得し、ScheduleEntityListに格納
            val scheduleEntity = scheduleRepository!!.findByYmAndUser(ym, loginUser)
            ScheduleEntityList.add(scheduleEntity)
        }
        return ScheduleEntityList
    }


    /**
     * [DB]1日ごとスケジュール時間区分取得処理
     *
     *
     * 取得したい日付(ymd)から該当するスケジュール時間区分を1日ごとに(1週間分)取得する<br></br>
     * また、現在日(ymd)に該当するスケジュール時間区分が複数登録されているときは最新のスケジュール時間区分が取得される<br></br>
     * ただし、スケジュール時間区分が何も登録されていないときはエレメントはnullとなる
     *
     *
     * @param ymd 取得したいスケジュール時間区分の日付(YYYYMMDD)
     * @return List<ScheduleTimeEntity><br></br>
     * フィールド(List&lt;ScheduleTimeEntity&gt;)<br></br>
     * id, endYmd, name1, startHm1, endHM1, restHm1... startHm7, endHM7, restHm7
    </ScheduleTimeEntity> */
    private fun selectScheduleTimeForOneweek(nowYmd: String): List<ScheduleTimeEntity?> {

        //1日ごと(1週間分)のスケジュール時間区分を格納するための変数
        val ScheduleTimeEntityList: MutableList<ScheduleTimeEntity?> = ArrayList()

        //現在の日付をLocalDateで取得
        val commonLogic = CommonLogic()
        val nowYmdLd: LocalDate = commonLogic.getLocalDateByYmd(nowYmd)

        //7回(1週間分)ループする
        for (i in 0 until Const.HOME_DISPLAY_SCHEDULE_DAY) {

            //現在の日付 + ループ回数日の年月を取得する
            val localDate = nowYmdLd.plusDays(i.toLong())
            val ymd: String =
                commonLogic.toStringYmdByYearMonthDay(localDate.year, localDate.monthValue, localDate.dayOfMonth)

            //取得した年月からスケジュール時間区分を取得し、ScheduleTimeEntityListに格納
            val scheduleTimeEntity = scheduleTimeRepository!!.selectScheduleTimeByYmd(ymd)
            ScheduleTimeEntityList.add(scheduleTimeEntity)
        }
        return ScheduleTimeEntityList
    }
}