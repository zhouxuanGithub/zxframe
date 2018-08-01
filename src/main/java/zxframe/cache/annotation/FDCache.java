package zxframe.cache.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//方法级数据缓存
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface FDCache {
	/**
	 * 使用本地缓存
	 */
	boolean lcCache() default false;
	/**
	 * 使用远程缓存
	 */
	boolean rcCache() default false;
	/**
	 * 远程缓存缓存时长，单位秒，默认2小时，-1为永久
	 */
	int rcETime() default 7200;
	/**
	 * 本地缓存里的数据每次都是克隆取出，一份新的数据
	 */
	boolean lcCacheDataClone() default true;
}
