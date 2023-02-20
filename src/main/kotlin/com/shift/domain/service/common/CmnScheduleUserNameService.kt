package com.shift.domain.service.common

import com.shift.common.CmnScheduleLogic
import com.shift.common.CommonLogic
import com.shift.common.CommonUtil
import com.shift.common.Const
import com.shift.domain.model.bean.CmnScheduleUserNameBean
import com.shift.domain.model.dto.ScheduleUserNameDto
import com.shift.domain.model.entity.ScheduleTimeEntity
import com.shift.domain.repository.ScheduleTimeRepository
import com.shift.domain.repository.ScheduleUserNameRepository
import com.shift.domain.service.BaseService
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
class CmnScheduleUserNameService: BaseService() {


    @Autowired
    private lateinit var scheduleUserNameRepository: ScheduleUserNameRepository

    @Autowired
    private lateinit var scheduleTimeRepository: ScheduleTimeRepository


    /**
     * [共通 Service] 確定スケジュール登録済み全ユーザ取得処理
     *
     * @param year 年
     * @param month 月
     * @param lastDateYmd 年月の最終日の日付(YYYYMMDD)
     * @return CmnScheduleUserNameBean<br></br>
     * フィールド(CmnScheduleUserNameBean)<br></br>
     * scheduleEntityList, scheduleTimeList
     */
    fun generateScheduleAllUser(year: Int, month: Int, lastDateYmd: String): CmnScheduleUserNameBean {

        // ユーザ毎の1ヶ月の確定スケジュールを取得
        val scheduleUserNameDtoList: List<ScheduleUserNameDto> = selectScheduleUserNameMonth(year, month)
        // スケジュール時間区分を取得
        val scheduleTimeEntity: ScheduleTimeEntity? = selectScheduleTime(lastDateYmd)
        // 確定スケジュールを判定した配列に変換
        val scheduleUserNameArray: Array<Array<String?>> = calcScheduleUserNameArray(scheduleUserNameDtoList, scheduleTimeEntity)

        // Beanにセット
        val cmnScheduleUserNameBean = CmnScheduleUserNameBean()
        cmnScheduleUserNameBean.scheduleTimeEntity = scheduleTimeEntity
        cmnScheduleUserNameBean.scheduleUserNameArray = scheduleUserNameArray
        return cmnScheduleUserNameBean
    }


    /**
     * 確定スケジュール登録済みユーザArray処理
     *
     * 登録済みのスケジュールとスケジュール時間区分から該当の日付とスケジュール時間区分に登録されている全てのユーザを取得する<br>
     * ただし、登録可能なスケジュール時間区分だけ判別する<br>
     * 日付とスケジュール時間区分に登録しているユーザが存在するとき、ユーザー名が格納される (ユーザ毎に改行タグ&lt;br&gt;が追加される)
     *
     * @param scheduleUserNameDtoList ユーザ毎の確定スケジュール
     * @param scheduleTimeEntity スケジュール時間区分
     * @return Array<Array<String?>> 日付とスケジュール時間区分に登録されている全てのユーザ<br>
     * エレメント(要素 [日付(31固定)][スケジュール時間(スケジュール登録可能数)])
     **/
    private fun calcScheduleUserNameArray(scheduleUserNameDtoList: List<ScheduleUserNameDto>, scheduleTimeEntity: ScheduleTimeEntity?): Array<Array<String?>> {

        // スケジュールに登録されているユーザを格納するための変数 (要素[日付][スケジュール時間区分])
        val scheduleUserNameArray = Array(31) {
            arrayOfNulls<String>(
                Const.SCHEDULE_RECORDABLE_MAX_DIVISION
            )
        }

        // スケジュールの判定を行う共通ロジック
        val cmnScheduleLogic = CmnScheduleLogic()

        // スケジュールに登録されているユーザだけループ
        for (scheduleUserNameDto in scheduleUserNameDtoList) {

            // 登録されているユーザ名を取得
            val userName: String? = scheduleUserNameDto.userName

            // 1ヵ月分のスケジュールを日付ごとに取得
            val scheduleDayList: List<String?> = scheduleUserNameDto.getScheduleDayList()

            // スケジュールの日付だけループ
            for (i in scheduleDayList.indices) {

                // 日付ごとのスケジュールを取得し、スケジュール時間ごとにスケジュールが登録されているかどうかを判定したBooleanの配列を取得
                val scheduleDay = scheduleDayList[i]
                val isScheduleRecordedArray: Array<Boolean?> = cmnScheduleLogic.toIsScheduleArray(scheduleDay, scheduleTimeEntity)

                // 判定した日付のスケジュール時間の区分だけループ
                for (j in isScheduleRecordedArray.indices) {

                    // 対象のスケジュール時間区分[j]がnullのとき、ループに戻る
                    if (isScheduleRecordedArray[j] == null) {
                        continue
                    }

                    // スケジュールが登録されているとき
                    if (isScheduleRecordedArray[j]!!) {

                        // スケジュールに登録されているユーザに現在のユーザを改行タグと共に追加する
                        scheduleUserNameArray[i][j] = CommonUtil.changeEmptyByNull(scheduleUserNameArray[i][j]) + userName + Const.HTML_BR
                    }
                }
            }
        }
        return scheduleUserNameArray
    }


    /**
     *  [Repository] 月次確定スケジュール登録済みユーザ検索処理
     *
     * 該当年月に一致するスケジュールに登録済みの全ユーザのスケジュールをユーザ毎に取得する<br>
     * ただし、該当年月のスケジュールに登録済みのユーザがいないときはEmptyとなる
     *
     * @param year 検索したい年
     * @param month 検索したい月
     * @return List<ScheduleUserNameDto> ユーザ毎の確定スケジュール
     **/
    private fun selectScheduleUserNameMonth(year: Int, month: Int): List<ScheduleUserNameDto> {

        // ymに変換
        val ym = CommonLogic().toStringYm(year, month)
        return scheduleUserNameRepository.selectScheduleUserNameMonth(ym)
    }


    /**
     * [Repository] スケジュール時間区分検索処理
     *
     * 取得したい日付(ymd)から該当するスケジュール時間区分を取得する<br>
     * また、現在日(ymd)に該当するスケジュール時間区分が複数登録されているときは最新のスケジュール時間区分が取得される<br>
     * ただし、スケジュール時間区分が何も登録されていないときはnullとなる
     *
     * @param ymd 取得したいスケジュール時間区分の日付(YYYYMMDD)
     * @return ScheduleTimeEntity スケジュール時間区分
     **/
    private fun selectScheduleTime(ymd: String): ScheduleTimeEntity? {
        return scheduleTimeRepository.selectScheduleTime(ymd)
    }
}