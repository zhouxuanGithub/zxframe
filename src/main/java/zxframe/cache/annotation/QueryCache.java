package zxframe.cache.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import zxframe.annotation.ScanningAnnotation;

//对查询缓存模型获取类需要加，需要对内部方法进行处理
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ScanningAnnotation
public @interface QueryCache {

}
