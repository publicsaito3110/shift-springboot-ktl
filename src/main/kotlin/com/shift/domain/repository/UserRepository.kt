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
    @Query(value = "SELECT u.* FROM user u WHERE u.id = :userId;", nativeQuery = true)
    fun selectUser(userId: String?): UserEntity?
}