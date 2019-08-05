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
	/**
	 * 映射的表名，不配置则为类名，并且全小写
	 * @return
	 */
	String tbname() default "";
}
