package zxframe.cache.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Cache {
	/**
	 * 使用本地缓存
	 */
	boolean lcCache() default false;
	/**
	 * 使用远程缓存
	 */
	boolean rcCache() default false;
	/**
	 * 远程缓存缓存时长，单位秒，默认*小时，-1为永久
	 */
	int rcETime() default 1200;
	/**
	 * 是否严格读写
	 * 严格读写，存在缓存组删除就得开启
	 * 非严格读写(默认)，不存在缓存组删除就可以关闭，提升效率
	 */
	boolean strictRW() default false;
	/**
	 * 本地缓存里的数据每次都是克隆取出，一份新的数据
	 */
	boolean lcCacheDataClone() default true;
}
