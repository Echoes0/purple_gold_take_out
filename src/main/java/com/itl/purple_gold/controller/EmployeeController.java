package com.itl.purple_gold.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.purple_gold.common.R;
import com.itl.purple_gold.entity.Employee;
import com.itl.purple_gold.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登录
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee ) {
        //密码MD5加密
        String password=employee.getPassword();
        password= DigestUtils.md5DigestAsHex(password.getBytes());

        //根据用户名查询
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<Employee>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);//获取唯一用户名（唯一约束）

        //未查到
        if (emp==null) {
            return R.error("登录失败");
        }

        //密码不匹配
        if (!emp.getPassword().equals(password)){
            return R.error("登录失败");
        }

        //员工状态被禁用
        if (emp.getStatus()==0)
        {
            return R.error("账号已禁用");
        }

        //登录成功，将员工id存入session，返回登录成功结果
        request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);
    }

    /**
     * 员工退出
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        //移除session，返回退出成功结果
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    /**
     * 新增员工
     * @param employee
     * @return
     */
    @PostMapping
    public R<String> save( @RequestBody Employee employee) {
        log.info("新增员工,信息:{}",employee.toString());

        //初始密码123456，并MD5加密
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

        //保存创建的账户
        employeeService.save(employee);

        return R.success("新增员工成功");
    }


    /**
     * 员工信息分页查询
     * Page：MybatisPlus提供的返回类型
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        log.info("员工信息分页查询，第{}页,每页{}条，条件：{}",page,pageSize,name);

        //分页构造器
        Page<Employee> pageInfo=new Page(page,pageSize);

        //条件构造器
        LambdaQueryWrapper<Employee> queryWrapper= new LambdaQueryWrapper<>();
        //过滤条件
        queryWrapper.like(!StringUtils.isEmpty(name),Employee::getName,name);
        //排序条件
        queryWrapper.orderByDesc(Employee::getUpdateTime);

        //查询
        employeeService.page(pageInfo,queryWrapper);

        return R.success(pageInfo);
    }

    /**
     * 根据id修改员工信息
     * @param employee
     * @return
     */

    @PutMapping
    public R<String> update(@RequestBody Employee employee) {

        //执行修改
        employeeService.updateById(employee);

        log.info("员工账户信息修改为{}", employee);
        return R.success("信息修改成功");
    }

    /**
     * 根据id查询员工信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id) {

        log.info("通过id{}查询员工",id);

        Employee employee = employeeService.getById(id);

        if (employee==null){
            return R.error("未查询到员工信息");
        }
        return R.success(employee);
    }
}
