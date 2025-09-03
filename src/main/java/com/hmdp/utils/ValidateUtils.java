package com.hmdp.utils;

import com.hmdp.exception.ValidateException;
import net.sf.jsqlparser.util.validation.ValidationException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Objects;

/**
 * @author tankaiwen
 */
public class ValidateUtils {
    public static void notNull(Object object, String message) {
        if (Objects.isNull(object)) {
            throw new ValidationException( message);
        }
    }

    public static void notBlank(String str, String message){
        if (StringUtils.isBlank(str)) {
            throw new ValidateException( message);
        }
    }

    public static void notEmpty(Collection<?> coll, String message) {
        if (CollectionUtils.isEmpty(coll)) {
            throw new ValidateException( message);
        }
    }

    public static void isTrue(Boolean expression, String message) {
        if (!expression) {
            throw new ValidateException( message);
        }
    }
}
