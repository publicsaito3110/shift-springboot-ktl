package com.shift.common.annotation;

import com.shift.common.CommonUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author saito
 *
 */
public class NotNgCharValidator implements ConstraintValidator<NotNgChar, String> {


    /**
     * インターフェースコンストラクタ
     *
     * @param constraintAnnotation
     */
    @Override
    public void initialize(NotNgChar constraintAnnotation) {
    }


    /**
     * NotNgCharの実装処理
     *
     * <p>禁止文字が含まれているかを判定<br>
     * ただし、値がnullまたは空文字のときは必ずバリデーション成功となる<br>
     * true: 禁止文字が含まれていない<br>
     * false: 禁止文字が含まれている
     * </p>
     *
     * @param value アノテーションのついた値
     * @param context ConstraintValidatorContext
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        // nullまたは空文字のとき
        if (value == null || value.isEmpty()) {
            return true;
        }

        // 使用不可の文字が含まれていないとき
        String pattern = "[^!\"#$%&'()\\*\\+\\-\\.,\\/:;<=>?@\\[\\\\\\]^_`{|}~]+";
        if (CommonUtil.Companion.isSuccessValidation(value, pattern)) {
            return true;
        }

        return false;
    }
}