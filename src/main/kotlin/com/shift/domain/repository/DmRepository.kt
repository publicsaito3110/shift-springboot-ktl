package com.shift.domain.repository

import com.shift.domain.model.entity.DmEntity
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository


/**
 * @author saito
 *
 */
@Repository
interface DmRepository: BaseRepository<DmEntity, Int> {

    /**
     * [Repository] 二者間トークのログインユーザの未読チャット検索処理
     *
     * 送信ユーザーと受信ユーザとのチャットで送信ユーザが未読のチャットを全て取得する<br>
     * ただし、未読チャットがないときはEmptyとなる
     *
     * @param sendUser 未読チャットを取得したい送信ユーザのID
     * @param receiveUser チャット相手のユーザのID
     * @param readFlg 既読済みの値
     * @return 二者間の全ての未読チャット
     **/
    @Query(value = "SELECT d.* FROM dm d WHERE d.send_user = :sendUser AND d.receive_user = :receiveUser AND (d.read_flg != :readFlg OR d.read_flg IS NULL)", nativeQuery = true)
    fun selectDmNotReadFlg(sendUser: String?, receiveUser: String?, readFlg: String?): List<DmEntity>
}