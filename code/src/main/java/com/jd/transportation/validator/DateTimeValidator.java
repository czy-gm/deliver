package com.jd.transportation.validator;

import com.jd.transportation.annotation.DateTime;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * 时间校验类
 *
 * @author czy_gm
 * @version 1.0
 * @since 2021/6/5
 */
public class DateTimeValidator implements ConstraintValidator<DateTime, String> {

    private static final Logger logger = LoggerFactory.getLogger(DateTimeValidator.class);

    private DateTime dateTimeAnnotation;

    @Override
    public void initialize(DateTime dateTime) {
        this.dateTimeAnnotation = dateTime;
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        String format = dateTimeAnnotation.format();
        if (StringUtils.isBlank(s) || s.length() != format.length()) {
            return false;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        try {
            dateFormat.parse(s);
        } catch (ParseException e) {
            logger.error("datetime parse error.", e);
            return false;
        }
        return true;
    }
}
