package com.shift.domain.service.common

import com.shift.common.CmnScheduleLogic
import com.shift.common.CommonLogic
import com.shift.common.CommonUtil
import com.shift.domain.model.dto.ScheduleUserNameDto
import com.shift.domain.model.entity.ScheduleTimeEntity
import com.shift.domain.repository.ScheduleTimeRepository
import com.shift.domain.repository.ScheduleUserNameRepository
import com.shift.domain.service.BaseService
import org.springframework.beans.factory.annotation.Autowired
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
     * [共通 Service] 確定スケジュール登録済み全ユーザ処理
     *
     * @param year 年
     * @param month 月
     * @param lastDateYmd 年月の最終日の日付(YYYYMMDD)
     * @return CmnScheduleUserNameBean<br></br>
     * フィールド(CmnScheduleUserNameBean)<br></br>
     * scheduleEntityList, scheduleTimeList
     */
    fun generateScheduleAllUser(year: Int, month: Int, lastDateYmd: String): CmnScheduleUserNameBean {

        // ユーザ毎の確定スケジュールをユーザ名で取得
        val scheduleUserNameDtoList: List<ScheduleUserNameDto> = selectScheduleAll(year, month)
        // スケジュール時間区分を取得
        val scheduleTimeEntity = selectScheduleTime(lastDateYmd)
        // 確定スケジュールを判定した配列に変換
        val userScheduleAllArray =
            calcUserALLScheduleArrayBySchedule(scheduleUserNameDtoList, scheduleTimeEntity)

        //Beanにセット
        return CmnScheduleUserNameBean(scheduleTimeEntity, userScheduleAllArray)
    }


    /**
     * 確定スケジュール登録済みユーザArray処理
     *
     * scheduleEntityListとscheduleTimeListから登録済みのスケジュールとスケジュール時間区分を取得し、登録されているユーザをArrayで取得する<br></br>
     * ただし、scheduleTimeListの要素数だけ判別する(最大7まで)<br></br>
     * 日付とスケジュール時間区分に登録しているユーザが存在するとき、ユーザー名を格納する(ユーザ毎に改行タグ&lt;br&gt;が追加される)
     *
     *
     * @param scheduleUserNameDtoList DBから取得したList<ScheduleUserNameDto> (List&lt;ScheduleUserNameDto&gt;)
     * @param scheduleTimeList DBから取得したList<ScheduleTimeEntity> (List&lt;ScheduleTimeEntity&gt;)
     * @return String[][]<br></br>
     * エレメント(String[日付(31固定)][スケジュール時間(スケジュール登録可能数)])<br></br>
     * 日付とスケジュール時間区分に登録しているユーザ名が順次格納される
    </ScheduleTimeEntity></ScheduleUserNameDto> */
    private fun calcUserALLScheduleArrayBySchedule(
        scheduleUserNameDtoList: List<ScheduleUserNameDto>,
        scheduleTimeEntity: ScheduleTimeEntity
    ): Array<Array<String?>> {

        //スケジュールに登録されているユーザを格納するための変数(要素[日付][スケジュール時間区分])
        val userScheduleAllArray = Array(31) {
            arrayOfNulls<String>(
                Const.SCHEDULE_RECORDABLE_MAX_DIVISION
            )
        }

        //スケジュールの判定を行うための共通クラス
        val cmnScheduleLogic = CmnScheduleLogic()

        //scheduleUserNameDtoの要素数(ユーザ数)だけループする
        for (scheduleUserNameDto in scheduleUserNameDtoList) {

            //登録されているユーザ名を取得
            val userName: String = scheduleUserNameDto.getUserName()

            //ユーザの1ヵ月分のスケジュールを日付ごとに取得
            val scheduleDayList: List<String> = scheduleUserNameDto.getDayList()

            //scheduleDayList(日付の回数)だけループする
            for (i in scheduleDayList.indices) {

                //日付ごとのスケジュールを取得し、スケジュール時間ごとにスケジュールが登録されているかどうかを判定したBooleanの配列を取得
                val scheduleDay = scheduleDayList[i]
                val isScheduleRecordedArray: Array<Boolean?> =
                    cmnScheduleLogic.toIsScheduleRecordedArrayBySchedule(scheduleDay, scheduleTimeEntity)

                //isScheduleRecordedArray(スケジュール時間の区分)だけループする
                for (j in isScheduleRecordedArray.indices) {

                    //isScheduleRecordedArray[j]がnull(スケジュール時間区分がない)とき、何もせずisScheduleRecordedArrayのループに戻る
                    if (isScheduleRecordedArray[j] == null) {
                        continue
                    }

                    //スケジュールが登録されているとき
                    if (isScheduleRecordedArray[j]!!) {

                        //スケジュールに登録されているユーザに現在のユーザを改行タグ(<br>)と共に追加する
                        val userSchedule =
                            CommonUtil.changeEmptyByNull(userScheduleAllArray[i][j]) + userName + Const.HTML_TAG_BR
                        userScheduleAllArray[i][j] = userSchedule
                    }
                }
            }
        }
        return userScheduleAllArray
    }


    /**
     * [DB]確定スケジュール登録済みユーザ検索処理
     *
     *
     * year, monthから1ヵ月分のスケジュールを取得する
     *
     * @param year LocalDateから取得した年(int)
     * @param month LocalDateから取得した月(int)
     * @return List<ScheduleUserNameDto> <br></br>
     * フィールド(List&lt;ScheduleUserNameDto&gt;)<br></br>
     * id, ymd, userName, day1, day2, day3... day30, day31
    </ScheduleUserNameDto> */
    private fun selectScheduleAll(year: Int, month: Int): List<ScheduleUserNameDto> {

        //year, monthをym(YYYYMM)に変換
        val ym: String = CommonLogic().toStringYmFormatSixByYearMonth(year, month)

        //DBから取得し、返す
        return scheduleUserNameRepository.selectScheduleUserNameByYm(ym)
    }


    /**
     * [DB]スケジュール時間区分取得処理
     *
     *
     * 取得したい日付(ymd)から該当するスケジュール時間区分を取得する<br></br>
     * また、現在日(ymd)に該当するスケジュール時間区分が複数登録されているときは最新のスケジュール時間区分が取得される<br></br>
     * ただし、スケジュール時間区分が何も登録されていないときはnullとなる
     *
     *
     * @param ymd 取得したいスケジュール時間区分の日付(YYYYMMDD)
     * @return ScheduleTimeEntity<br></br>
     * フィールド(ScheduleTimeEntity)<br></br>
     * id, endYmd, name1, startHm1, endHM1, restHm1... startHm7, endHM7, restHm7
     */
    private fun selectScheduleTime(ymd: String): ScheduleTimeEntity {
        return scheduleTimeRepository.selectScheduleTimeByYmd(ymd)
    }
}