package com.importre.kotlin.enumerize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Generate {@link Enum} extensions.
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.FIELD)
public @interface EnumExt {
}
