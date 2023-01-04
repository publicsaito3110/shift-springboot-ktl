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


    /**
     * [Configuration] ログイン認証設定(Spring Security)
     *
     * ログイン認証で必要な情報を設定する
     *
     * @param httpSecurity ログイン認証の設定を格納
     * @return SecurityFilterChain 設定済みのログイン認証情報
     */
    @Bean
    @Throws(Exception::class)
    fun securityFilterChain(httpSecurity: HttpSecurity): SecurityFilterChain {
        httpSecurity.formLogin { login: FormLoginConfigurer<HttpSecurity> ->
            login
                // ログイン画面のURL
                .loginProcessingUrl("/login")
                .loginPage("/login")
                // ログイン成功時のURL
                .defaultSuccessUrl("/login/auth")
                // ログイン失敗時のURL
                .failureUrl("/login?error")
                .permitAll()
        }.logout { logout: LogoutConfigurer<HttpSecurity> ->
            logout
                // ログアウト時のURL
                .logoutSuccessUrl("/logout")
        }.authorizeHttpRequests(
            Customizer { auth ->
                auth
                    // static内のアクセスを許容するパス
                    .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                    .permitAll()
            }
        )
        return httpSecurity.build()
    }
}