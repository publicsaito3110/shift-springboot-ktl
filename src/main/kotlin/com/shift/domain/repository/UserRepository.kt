package com.shift.domain.repository

import com.shift.domain.model.entity.UserEntity
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository


/**
 * @author saito
 *
 */
//@Repository
interface UserRepository: BaseRepository<UserEntity, String> {


    /**
     * [Repository] ユーザ検索処理
     *
     * userIdと一致するユーザを取得する<br>
     * ただし、一致するユーザーがいない場合はnullとなる
     *
     * @param userId 取得したいユーザのユーザID
     * @return UserEntity ユーザ情報
     */
    @Query(value = "SELECT u.* FROM user u WHERE u.id = :userId", nativeQuery = true)
    fun selectUser(userId: String?): UserEntity?


    /**
     * [Repository] ユーザ検索処理
     *
     * 未退職ユーザを全て取得する<br>
     * ただし、該当するユーザーがいない場合はEmptyとなる
     *
     * @param delFlg 退職フラグ
     * @return UsersEntity 全ての未退職ユーザ
     */
    @Query(value = "SELECT u.* FROM users u WHERE u.del_flg != :delFlg OR u.del_flg IS NULL", nativeQuery = true)
    fun selectUsersNotDelFlg(delFlg: String?): List<UserEntity>
}