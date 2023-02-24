package com.shift.common

import java.time.LocalDate


/**
 * @author saito
 *
 */
class CommonLogic {


    /**
     * 改行スペース変換処理
     *
     * HTMLの改行とスペースに対応させた文字列を返す<br>
     * ただし、対象の文字列がnullのときは何もしない
     *
     * @param value 対象の文字列
     * @return String HTML対応した文字列
     */
    fun changeAfterBreakLine(value: String?): String? {

        if (value == null) {
            return null
        }
        // 改行及びスペース対応
        val afterBreakLine: String = value.replaceAfter(Const.CHARACTER_CODE_BREAK_LINE_ALL, Const.HTML_BR)
        return afterBreakLine.replaceAfter(Const.CHARACTER_SPACE, Const.HTML_SPACE)
    }


    /**
     * LocalDate変換処理
     *
     * ymd(YYYYMMDD)をLocalDateで返す<br>
     * ただし、ymdがYYYYMMDDでない又は存在しない日付のときはnullを返す
     *
     * @param ymd (YYYYMMDD)
     * @return LocalDate ymdから変換されたLocalDate<br>
     * ただし、ymdがフォーマット通りでないときはnullとなる
     */
    fun getLocalDate(ymd: String?): LocalDate? {

        try {

            // ymdがnullまたは8桁でないとき
            if (ymd == null || ymd.length != 8) {
                return null
            }

            // ymdをLocalDateに変換する
            val ymdDate = ymd.substring(0, 4) + "-" + ymd.substring(4, 6) + "-" + ymd.substring(6, 8)
            return LocalDate.parse(ymdDate)
        } catch (e: Exception) {

            // 例外発生時、nullを返す
            return null
        }
    }


    /**
     * 最終日の日付取得処理
     *
     * year, monthからその年月の最終日の日付をString(YYYYMMDD)の8桁で返す
     *
     * @param year 年
     * @param month 月
     * @return String 最終日の日付(YYYYMMDD)
     */
    fun getLastDateYmd(year: Int, month: Int): String? {

        try {

            //現在の日付をLocalDateで取得し、ymd(YYYYMMDD)に変換する
            val localDate = LocalDate.of(year, month, 1)
            val lastDay: Int = localDate.lengthOfMonth()
            return year.toString() + String.format("%02d", month) + String.format("%02d", lastDay)
        } catch (e: Exception) {
            //例外発生時、nullを返す
            return null
        }
    }


    /**
     * 年月変換処理
     *
     * year, monthから年月(YYYYMM)に変換して返す
     *
     * @param year 年
     * @param month 月
     * @return String 年月ym(YYYYMM)
     */
    fun toStringYm(year: Int, month: Int): String {
        return year.toString() + String.format("%02d", month)
    }


    /**
     * 日付変換処理
     *
     * year, month, dayから日付(YYYYMMDD)に変換して返す
     *
     * @param year 年
     * @param month 月
     * @param day 日
     * @return String 日付(YYYYMMDD)
     */
    fun toStringYmd(year: Int, month: Int, day: Int): String {
        return year.toString() + String.format("%02d", month) + String.format("%02d", day)
    }
}