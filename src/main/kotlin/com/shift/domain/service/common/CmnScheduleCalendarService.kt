package com.shift.domain.service.common

import com.shift.common.CommonLogic
import com.shift.common.CommonUtil
import com.shift.domain.model.bean.CmnScheduleCalendarBean
import com.shift.domain.service.BaseService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate


/**
 * @author saito
 *
 */
@Service
@Transactional
class CmnScheduleCalendarService: BaseService() {


    /**
     * [共通 Service] カレンダー, YM作成処理
     *
     * 指定されたymからカレンダー及び月次情報を取得する<br>
     * ただし、指定された年月の値がnullまたは異常なときは現在の日付の年月となる
     *
     * @param ym 年月(YYYYMM)
     * @return CmnScheduleCalendarBean
     */
    fun generateCalendar(ym: String?): CmnScheduleCalendarBean {

        // 指定された年月をそれぞれintで取得
        val yearMonthArray: Array<Int> = changeYearMonthArray(ym)
        // 指定された日付から当月、翌月、前月をそれぞれ取得
        val nowNextBeforeYmArray: Array<String> = calcNowNextBeforeYmArray(yearMonthArray[0], yearMonthArray[1])
        // 指定された年月のカレンダーを取得
        val calendarList: List<Int?>  = calcCalendar(yearMonthArray[0], yearMonthArray[1])
        // 最終日の日付を取得
        val lastDateYmd: String? = CommonLogic().getLastDateYmd(yearMonthArray[0], yearMonthArray[1])

        // Beanにセット
        val cmnScheduleCalendarBean: CmnScheduleCalendarBean = CmnScheduleCalendarBean()
        cmnScheduleCalendarBean.year = yearMonthArray[0]
        cmnScheduleCalendarBean.month = yearMonthArray[1]
        cmnScheduleCalendarBean.lastDateYmd = lastDateYmd
        cmnScheduleCalendarBean.nowYm = nowNextBeforeYmArray[0]
        cmnScheduleCalendarBean.afterYm = nowNextBeforeYmArray[1]
        cmnScheduleCalendarBean.beforeYm = nowNextBeforeYmArray[2]
        cmnScheduleCalendarBean.calendarList = calendarList
        return cmnScheduleCalendarBean;
    }


    /**
     * 翌前月に取得処理
     *
     * 翌月と前月を計算して返す<br>
     * ym(YYYYMM)に変換した年月 0: 現在, 1: 翌月, 2: 前月となる
     *
     * @param year LocalDateから取得した年(int)
     * @param month LocalDateから取得した月(int)
     * @return Array<String> 0: 現在の年月, 1: 翌月の年月, 2: 前月の年月
     */
    private fun calcNowNextBeforeYmArray(year: Int, month: Int): Array<String> {

        // year, monthから現在のLocalDateを取得
        val nowLd: LocalDate = getLocalDateByYearMonth(year, month)
        val nowYear = nowLd.year
        val nowMonth = nowLd.monthValue

        //共通ロジックをインスタンス化
        val commonLogic: CommonLogic = CommonLogic()

        // 前月のLocalDateを取得
        val beforeMonthLd = nowLd.minusMonths(1)
        val beforeYear = beforeMonthLd.year
        val beforeMonth = beforeMonthLd.monthValue

        // 翌月のLocalDateを取得
        val afterMonthLd = nowLd.plusMonths(1)
        val afterYear = afterMonthLd.year
        val afterMonth = afterMonthLd.monthValue

        // それぞれ年月をym(YYYYMM)に変換
        val nowYm: String = commonLogic.toStringYm(nowYear, nowMonth)
        val beforeYm: String = commonLogic.toStringYm(beforeYear, beforeMonth)
        val afterYm: String = commonLogic.toStringYm(afterYear, afterMonth)

        //nowYm, beforeYm, afterYmを配列に格納し、返す
        return arrayOf(nowYm, afterYm, beforeYm)
    }


    /**
     * 年月変換処理
     *
     * 年と月をIntに変換し、返す<br>
     * ただし、パラメーターがない(null)場合またはymがフォーマット通りでないときは現在の年月になる<br>
     * 0: 年, 1: 月
     *
     * @param ym 年月(YYYYMM)
     * @return Array<Int> intに変換した年と月 0: 年, 1: 月
     */
    private fun changeYearMonthArray(ym: String?): Array<Int> {

        // ymをymd(YYMM01)に変換し、LocalDateを取得する
        val ymd: String = CommonUtil.changeEmptyByNull(ym) + "01"
        val ymdLd: LocalDate? = CommonLogic().getLocalDate(ymd)

        // ym(年月)が指定されていないまたはymが異常なとき
        if (ymdLd == null) {

            // 現在の日付を取得し、年月に変換
            val nowLd: LocalDate = LocalDate.now()
            val year: Int = nowLd.year
            val month: Int = nowLd.monthValue

            // 年月を配列に格納して返す
            return arrayOf(year, month)
        }

        // ymdLdから年月に変換
        val year: Int = ymdLd.year
        val month: Int = ymdLd.monthValue

        // 年月を配列に格納して返す
        return arrayOf(year, month)
    }


    /**
     * カレンダー作成処理
     *
     * year, monthから1ヵ月分のカレンダーを作成する<br>
     * ただし、カレンダーのフォーマット(7×4 or 7×5 or 7×6)にするため、前月, 翌月も含む(前月, 翌月の日付は含まれない)<br>
     * また、前月, 翌月分の日付はnullが格納される
     *
     * @param year 年(int)
     * @param month 月(int)
     * @return List<Int?> 1ヵ月分のカレンダー<br>
     * 要素数は必ず7の倍数となり、前月または翌月の日付はnull, 当月の日付はIntが格納される
     */
    private fun calcCalendar(year: Int, month: Int): List<Int?> {

        //------------------------------
        // 第1週目の日曜日～初日までを設定
        //------------------------------

        // 指定された年月の1日目の情報をLocalDateで取得
        val localDate: LocalDate = getLocalDateByYearMonth(year, month)

        // 第1週目の初日の曜日を取得（月:1, 火:2, ....日:7）
        val firstWeek: Int = localDate.dayOfWeek.value

        // 日付けとスケジュールを格納
        val calendarList: MutableList<Int?> = ArrayList()

        // firstWeekが日曜日でないとき
        if (firstWeek != 7) {

            // 初日が日曜を除く取得した曜日の回数分nullを代入して第1週目のカレンダーのフォーマットに揃える
            for (i in 1..firstWeek) {
                calendarList.add(null)
            }
        }

        //-------------
        // 日付を設定
        //-------------

        // 最終日をLocalDateから取得
        val lastDay: Int = localDate.lengthOfMonth()

        // lastDayの回数だけループして日付を格納
        for (i in 1..lastDay) {
            calendarList.add(i)
        }

        //------------------------------
        // 最終週の終了日～土曜日までを設定
        //------------------------------

        // calendarListに登録した要素数から残りの最終週の土曜日までの日数を取得
        val remainderWeek: Int = 7 - calendarList.size % 7

        // remainderWeekが7(最終日が土曜日)以外のとき
        if (remainderWeek != 7) {

            // remainderWeekの回数分nullを代入して最終週目のカレンダーのフォーマットに揃える
            for (i in 1..remainderWeek) {
                calendarList.add(null)
            }
        }
        return calendarList
    }


    /**
     * [private メソッド共通処理] LocalDate取得処理
     *
     * year(int), month(int)からLocalDateを返す<br>
     * ただし、正確な日付は指定した年月の初日となる
     *
     * @param year 年
     * @param month 月
     * @return LocalDate 指定した年月と一致したLocalDate
     */
    private fun getLocalDateByYearMonth(year: Int, month: Int): LocalDate {

        // year, monthから初日のLocalDateで取得し、返す
        return LocalDate.of(year, month, 1)
    }
}