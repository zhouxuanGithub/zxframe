package zxframe.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

//用于需要扫描解析的类
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
@Primary
public @interface ScanningAnnotation {

}
