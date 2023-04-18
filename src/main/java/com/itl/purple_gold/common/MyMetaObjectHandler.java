package com.itl.purple_gold.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 自定义元对象处理器
 * 实现Mybatis的公共字段自动填充
 */

@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {

    /**
     * 自动字段填充【插入】
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("自动字段填充【插入】");
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("createUser", BaseContext.getCurrentId());
        metaObject.setValue("updateUser", BaseContext.getCurrentId());
    }

    /**
     * 自动字段填充【更新】
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("自动字段填充【更新】");
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("updateUser", BaseContext.getCurrentId());
    }
}
