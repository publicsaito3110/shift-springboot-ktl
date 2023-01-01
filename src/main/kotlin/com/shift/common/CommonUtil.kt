package com.shift.common


/**
 * @author saito
 *
 */
class CommonUtil {
    private constructor() {
        //インスタンス化を禁止
    }

    companion object {

        /**
         * null空文字変換処理
         *
         * nullのとき空文字に変換する<br>
         * ただし、null以外のときは何もしない
         *
         * @param value 全てのStringの値
         * @return String nullのとき空文字に変換し、null以外のときは何もしない
         */
        fun changeEmptyByNull(value: String?): String {

            //nullのとき
            if (value == null) {
                return ""
            }

            return value
        }
    }
}