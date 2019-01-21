package com.example.zxf.myapp.dagger;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * 自定义注解
 * @scope 自定义注解名称
 * 使用scope 和 Applicatin 实现全局单例
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface ApplicationScope {
}
