package com.itl.purple_gold.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;


/**
 * 全局异常处理
 */
@ControllerAdvice(annotations = {RestController.class, Controller.class})//拦截具有@RestController或@Controller注解的相关类
@ResponseBody //返回结果封装为json
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 主键重复异常处理方法
     * @return
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException exception){
        log.info(exception.getMessage());
        if (exception.getMessage().contains("Duplicate entry")){
            String[] split = exception.getMessage().split(" ");
            String msg=split[2]+"已存在";
            return R.error(msg);
        }
        return R.error("未知错误");
    }

    /**
     * 菜品分类删除异常处理方法
     * @return
     */
    @ExceptionHandler(CustomerException.class)
    public R<String> exceptionHandler(CustomerException exception){
        log.info(exception.getMessage());

        return R.error(exception.getMessage());
    }
}
