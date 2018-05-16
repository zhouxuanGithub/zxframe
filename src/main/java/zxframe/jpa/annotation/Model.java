package zxframe.jpa.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import zxframe.annotation.ScanningAnnotation;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ScanningAnnotation
public @interface Model {
	/**
	 * 使用的数据源
	 */
	String dsname() default "default";
}
