package com.shift.config

import org.springframework.boot.autoconfigure.security.servlet.PathRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain


/**
 * @author saito
 *
 */
@Configuration
class SecurityConfig {


    /**
     * [Configuration] パスワード設定(Spring Security)
     *
     * ログイン認証及びパスワードのハッシュ化で必要なハッシュアルゴリズムを実装したクラスを設定する<br>
     * 設定されるクラスはBCryptPasswordEncoderであり、PasswordEncoderインターフェースにセットされる
     *
     * @param
     * @return PasswordEncoder 設定済みのログイン認証情報
     */
    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}