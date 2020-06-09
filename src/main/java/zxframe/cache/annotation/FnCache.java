/**
 * ZxFrame Java Library
 * https://github.com/zhouxuanGithub/zxframe
 *
 * Copyright (c) 2019 zhouxuan
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 **/
package zxframe.cache.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import zxframe.cache.model.DefaultCacheDataModel;

//方法级数据缓存
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FnCache {
	String group() default DefaultCacheDataModel.zxframeDefaultCacheModelGroup;
	String[] key() default {};//缓存的 key，指定参数名称，如果不指定，则缺省按照方法的所有参数进行组合
}