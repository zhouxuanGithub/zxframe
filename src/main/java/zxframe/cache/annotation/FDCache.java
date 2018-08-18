package zxframe.cache.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//方法级数据缓存
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FDCache {
	String group() default "zxframe-fdcache-default";
}