package com.shift.common.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author saito
 *
 */
public class NotOnlyWhitespaceValidator implements ConstraintValidator<NotOnlyWhitespace, String> {


    /**
     * インターフェースコンストラクタ
     *
     * @param constraintAnnotation
     */
    @Override
    public void initialize(NotOnlyWhitespace constraintAnnotation) {
    }


    /**
     * NotOnlyWhitespace実装処理
     *
     * <p>
     * 空白文字及び改行のみではなく、何かしらの文字が含まれているを判別<br>
     * ただし、値がnullまたは空文字のときは必ずバリデーション成功(true)になる<br>
     * true: 空白文字及び改行のみ<br>
     * false: 空白文字及び改行のみで何かしらの文字が含まれていない
     * </p>
     *
     * @param value アノテーションのついた値
     * @param context ConstraintValidatorContext
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        // nullのとき
        if (value == null) {
            return true;
        }

        //空文字及び改行コードを全て空文字に変換する
        String afterOnlyChar = value.replaceAll("[\r|\n|\r\n|\\s]+", "");

        // 変換前の値が空白文字及び改行コードのみではなく、何かしらの文字があるとき
        if (!"".equals(afterOnlyChar)) {
            return true;
        }

        return false;
    }
}