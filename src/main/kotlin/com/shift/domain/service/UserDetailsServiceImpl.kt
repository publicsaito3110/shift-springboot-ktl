package com.shift.domain.service

import com.shift.domain.model.entity.UserEntity
import com.shift.domain.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


/**
 * @author saito
 *
 */
@Service
@Transactional
class UserDetailsServiceImpl: BaseService(), UserDetailsService {

    @Autowired
    private lateinit var userRepository: UserRepository


    /**
     * [Spring Security] ログイン認証機能 (/login)
     *
     * 入力されたログイン情報と登録されているユーザ情報を認証する<br>
     * ログイン情報と一致したユーザ情報(ユーザID, ユーザ権限)を格納し、ログイン認証を行う<br>
     * ただし、ログイン認証に失敗したときログイン画面に強制的に遷移される
     *
     * @param username ログイン時に入力されたユーザID
     * @return UserDetails ユーザ情報(ユーザID, パスワード, ユーザ権限)
     */
    override fun loadUserByUsername(username: String): UserDetails {

        // ログイン時に入力されたユーザIDから検索
        val userEntity: UserEntity? = userRepository.selectUser(username)

        // ユーザIDと一致するユーザが存在しなかったとき
        if (userEntity == null) {
            throw UsernameNotFoundException("$username is not found")
        }

        // User(UserDetailsインターフェースの実装クラス)に登録されているユーザ権限、ユーザID, パスワードをセット
        val grantedAuthorities: HashSet<GrantedAuthority> = HashSet<GrantedAuthority>()
        grantedAuthorities.add(SimpleGrantedAuthority(userEntity.adminFlgFormatRole()))
        return User(userEntity.id, userEntity.password, grantedAuthorities)
    }
}