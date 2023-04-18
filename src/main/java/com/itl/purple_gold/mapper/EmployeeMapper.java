package com.itl.purple_gold.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itl.purple_gold.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
