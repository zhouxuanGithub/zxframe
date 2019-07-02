package zxframe.cache.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//方法级数据缓存
//不在@Service中使用，单独建立cache目录使用
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FnCache {
	String group() default "zxframe-fncache-default";
	String[] key() default {};//缓存的 key，指定参数名称，如果不指定，则缺省按照方法的所有参数进行组合
}