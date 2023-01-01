package com.shift.common

import java.time.LocalDate


/**
 * @author saito
 *
 */
class CommonLogic {


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

        // ymdがnullまたは8桁でないとき
        if (ymd == null || ymd.length != 8) {
            return null
        }

        try {

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
     * <p>year(int), month(int)をString型ym(YYYYMM)に変換して返す</p>
     *
     * @param year 年(int)
     * @param month 月(int)
     * @return String ym(YYYYMM)
     */
    fun toStringYm(year: Int, month: Int): String {

        //ym(YYYYMM)に変換する
        return year.toString() + String.format("%02d", month)
    }
}