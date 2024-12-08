package site.mufen.types.annotations;

import java.lang.annotation.*;

/**
 * @author mufen
 * @Description
 * @create 2024/12/1 00:36
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface DCCValue {
    String value() default "";
}
