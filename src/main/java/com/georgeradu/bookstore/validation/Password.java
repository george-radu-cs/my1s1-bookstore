package com.georgeradu.bookstore.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordValidator.class)
@Documented
public @interface Password {

    String message() default "Invalid password";

    String messageMinLength() default "Password must be at least {min} characters long";

    String messageMaxLength() default "Password must be at most {max} characters long";

    String messageUppercase() default "Password must contain at least one uppercase letter";

    String messageLowercase() default "Password must contain at least one lowercase letter";

    String messageDigit() default "Password must contain at least one digit";

    String messageSpecialCharacter() default "Password must contain at least one special character";


    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int minLength() default 8;

    int maxLength() default 255;

    boolean requireUppercase() default true;

    boolean requireLowercase() default true;

    boolean requireDigit() default true;

    boolean requireSpecialCharacter() default true;
}
