package com.shift.domain.service.common

import com.shift.common.CommonLogic
import com.shift.common.CommonUtil
import com.shift.domain.model.bean.CmnScheduleCalendarBean
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate


/**
 * @author saito
 *
 */
@Service
@Transactional
class CmnScheduleCalendarService {


    /**
     * [共通Service] カレンダー, YM作成処理
     *
     * @param ym 年月(YYYYMM)
     * @return CmnScheduleCalendarBean
     */
    fun generateCalendar(ym: String?): CmnScheduleCalendarBean {

        //Beanにセット
        var cmnScheduleCalendarBean: CmnScheduleCalendarBean = CmnScheduleCalendarBean()
        val yearMonthArray: Array<Int> = changeYearMonthArray(ym)
        val nowNextBeforeYmArray: Array<String> = calcNowNextBeforeYmArray(yearMonthArray[0], yearMonthArray[1])
        return cmnScheduleCalendarBean;
    }


    /**
     * 翌前月に取得処理
     *
     *翌月と前月を計算して返す<br>
     * ym(YYYYMM)に変換した現在の月[0], 翌月[1]と前月[2]
     *
     * @param year LocalDateから取得した年(int)
     * @param month LocalDateから取得した月(int)
     * @return String[] 現在の月[0], 翌月のym[1]と前月のym[2]<br></br>
     * String[0]が現在の月, String[1]が翌月, String[2]が前月
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
     * 年と月をint型に変換し、int[]で返す<br></br>
     * ただし、パラメーターがない(null)場合またはymがフォーマット通りでないときは現在の年月になる<br></br>
     * int[0]が年, int[1]が月
     *
     * @param ym 年月(YYYYMM)
     * @return int[] intに変換した年[0]と月[1]<br>
     * int[0]が年, int[1]が月
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
     * [privateメソッド共通処理] LocalDate取得処理
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