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

            // nullのとき
            if (value == null) {
                return ""
            }

            return value
        }


        /**
         * バリデーション判定処理
         *
         * 正規表現のパターンと一致しているか判定する<br>
         * ただし、valueがnullまたは正規表現が適切でないときは必ず失敗する
         *
         * @param value 判定させる値
         * @param regex 正規表現のパターン
         * @return Boolean 正規表現のパターンマッチの結果<br>
         * true: 正規表現のパターンと一致しているとき, false: 正規表現のパターンと一致していないとき
         */
        fun isSuccessValidation(value: String?, regex: String): Boolean {

            try {

                // 正規表現のパターンとマッチしているとき
                val regexRg: Regex = Regex(regex)
                if (value != null && value.matches(regexRg)) {
                    return true
                }

                return false
            } catch (e: Exception) {

                // 例外発生時、falseを返す
                return false
            }
        }
    }
}