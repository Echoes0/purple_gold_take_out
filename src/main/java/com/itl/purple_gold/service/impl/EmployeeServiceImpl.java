package com.itl.purple_gold.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.purple_gold.entity.Employee;
import com.itl.purple_gold.mapper.EmployeeMapper;
import com.itl.purple_gold.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper,Employee> implements EmployeeService {
}
