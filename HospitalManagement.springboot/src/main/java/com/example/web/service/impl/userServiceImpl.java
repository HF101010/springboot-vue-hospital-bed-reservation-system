package com.example.web.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.web.SysConst;
import com.example.web.dto.*;
import com.example.web.dto.query.*;
import com.example.web.entity.*;
import com.example.web.mapper.*;
import com.example.web.enums.*;
import com.example.web.service.*;
import com.example.web.tools.dto.*;
import com.example.web.tools.exception.CustomException;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.lang.reflect.InvocationTargetException;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import lombok.SneakyThrows;
import java.io.IOException;
import com.example.web.tools.*;
import com.example.web.tools.JWTUtils;
import java.text.DecimalFormat;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.springframework.web.bind.annotation.RequestParam;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
/**
 * 用户信息功能实现类
 */
@Service
public class userServiceImpl extends ServiceImpl<userMapper, user> implements userService {

    /**
     * 操作数据库的user表mapper对象
     */
    @Autowired
    private userMapper userMapper;

  
   /**
     * 构建表查询sql
     */
    private LambdaQueryWrapper<user> BuilderQuery(userPagedInput input) {
       //声明一个支持用户信息查询的(拉姆达)表达式
        LambdaQueryWrapper<user> queryWrapper = Wrappers.<user>lambdaQuery()
                .eq(input.getId() != null && input.getId() != 0, user::getId, input.getId());
   //如果前端搜索传入查询条件则拼接查询条件
      

 
    
      return queryWrapper;
    }
  
    /**
     * 处理用户信息对于的外键数据
     */
   private List<userDto> DispatchItem(List<userDto> items) throws InvocationTargetException, IllegalAccessException {
       // 可以在这里处理外键关联数据
       return items; 
   }
  
    /**
     * 用户信息分页查询
     */
    @SneakyThrows
    @Override
    public PagedResult<userDto> List(userPagedInput input) {
			//构建where条件+排序
        LambdaQueryWrapper<user> queryWrapper = BuilderQuery(input);
        // 动态排序处理
        if (input.getSortItem() != null) {
            // 根据字段名动态排序
            queryWrapper.last("ORDER BY " + input.getSortItem().getFieldName()
                    + (input.getSortItem().getIsAsc() ? " ASC" : " DESC"));
        } else {
            // 默认按ID从大到小排序（如果表中没有creation_time字段）
            queryWrapper = queryWrapper.orderByDesc(user::getId);
        }

        //构建一个分页查询的model
        Page<user> page = new Page<>(input.getPage(), input.getLimit());
         //从数据库进行分页查询获取用户信息数据
        IPage<user> pageRecords= userMapper.selectPage(page, queryWrapper);
        //获取所有满足条件的数据行数
        Long totalCount= userMapper.selectCount(queryWrapper);
        //把user实体转换成user传输模型
        List<userDto> items= Extension.copyBeanList(pageRecords.getRecords(),userDto.class);

		   DispatchItem(items);
        //返回一个分页结构给前端
        return PagedResult.GetInstance(items,totalCount);

    }
  
    /**
     * 单个用户信息查询
     */
    @SneakyThrows
    @Override
    public userDto Get(userPagedInput input) {
       if(input.getId()==null)
        {
         return new userDto();
        }
      
       PagedResult<userDto> pagedResult = List(input);
        return pagedResult.getItems().stream().findFirst().orElse(new userDto()); 
    }

    /**
     *用户信息创建或者修改
     */
    @SneakyThrows
    @Override
    public userDto CreateOrEdit(userDto input) {
        //声明一个用户信息实体
        user user=input.MapToEntity();  
        //调用数据库的增加或者修改方法
        saveOrUpdate(user);
        //把传输模型返回给前端
        return user.MapToDto();
    }
    /**
     * 用户信息删除
     */
    @Override
    public void Delete(IdInput input) {
        user entity = userMapper.selectById(input.getId());
        userMapper.deleteById(entity);
    }

    /**
     * 用户信息批量删除
     */
    @Override
    public void BatchDelete(IdsInput input) {
        for (Integer id : input.getIds()) {
            IdInput idInput = new IdInput();
            idInput.setId(id);
            Delete(idInput);
        }
    }

    /**
     * 登录
     */
    @Override
    public String SignIn(userDto input) {
        LambdaQueryWrapper<user> queryWrapper = Wrappers.<user>lambdaQuery();
        if (Extension.isNotNullOrEmpty(input.getUsername())) {
            queryWrapper.eq(user::getUsername, input.getUsername());
        }
        if (Extension.isNotNullOrEmpty(input.getPassword())) {
            queryWrapper.eq(user::getPassword, Extension.MD5Encrypt(input.getPassword()));
        }
        if (Extension.isNotNullOrEmpty(input.getRole())) {
            queryWrapper.eq(user::getRole, input.getRole());
        }

        List<user> items = userMapper.selectList(queryWrapper);
        if (items.stream().count() == 0) {
            throw new CustomException("请检查登录的账号或者密码,角色是否都正确!");
        }
        Map<String, String> map = new HashMap<>();
        map.put(SysConst.UserIdClaim, items.get(0).getId().toString());
        // 将role字符串转换为Integer（如果role是"1"或"2"等数字字符串）
        Integer roleType = 2; // 默认用户
        try {
            roleType = Integer.parseInt(items.get(0).getRole());
        } catch (NumberFormatException e) {
            // 如果不是数字，根据role字符串判断
            if ("管理员".equals(items.get(0).getRole()) || "admin".equalsIgnoreCase(items.get(0).getRole())) {
                roleType = 1;
            }
        }
        map.put(SysConst.RoleTypeClaim, roleType.toString());
        String token = JWTUtils.getToken(map);
        return token;
    }

    /**
     * 注册
     */
    @Override
    public userDto Register(userDto input) {
        // 对密码进行MD5加密
        input.setPassword(Extension.MD5Encrypt(input.getPassword()));
        // 检查账号名是否存在
        Long userCount = userMapper.selectCount(Wrappers.<user>lambdaQuery()
                .eq(Extension.isNotNullOrEmpty(input.getUsername()), user::getUsername, input.getUsername()));
        if (userCount > 0) {
            throw new CustomException("该账号名已经存在!");
        }
        return CreateOrEdit(input);
    }

    /**
     * 找回密码
     */
    @Override
    public void ForgetPassword(userDto input) {
        // 检查账号名是否存在
        user userEntity = userMapper.selectList(Wrappers.<user>lambdaQuery()
                .eq(Extension.isNotNullOrEmpty(input.getUsername()), user::getUsername, input.getUsername()))
                .stream().findFirst().orElse(null);

        if (userEntity == null) {
            throw new CustomException("该账号名不存在!");
        }
        if (userEntity.getPhone() == null) {
            throw new CustomException("该账号未绑定手机号!");
        }
        if (!userEntity.getPhone().equals(input.getPhone())) {
            throw new CustomException("请输入你绑定的手机号!");
        }
        userEntity.setPassword(Extension.MD5Encrypt(input.getPassword()));
        saveOrUpdate(userEntity);
    }

    /** 修改密码 */
    @Override
    public void ChangePassword(userDto input) {
        user userEntity = userMapper.selectById(input.getId());
        if (!Extension.MD5Encrypt(input.getOrginPassword()).equals(userEntity.getPassword())) {
            throw new CustomException("旧密码错误!");
        }
        // 对当前输入的密码进行加密
        if (Extension.MD5Encrypt(input.getPassword()).equals(userEntity.getPassword())) {
            throw new CustomException("新密码不能与旧密码相同!");
        }

        userEntity.setPassword(Extension.MD5Encrypt(input.getPassword()));
        saveOrUpdate(userEntity);
    }

    /**
     * 重置密码
     */
    @Override
    public void ResetPassword(userDto input) {
        user userEntity = userMapper.selectById(input.getId());
        // 如果前端传入密码，则使用传入的密码；否则使用默认密码123456
        String password = Extension.isNotNullOrEmpty(input.getPassword()) ? input.getPassword() : "123456";
        userEntity.setPassword(Extension.MD5Encrypt(password));
        saveOrUpdate(userEntity);
    }

    /**
     * 用户导出
     */
    @Override
    public void Export(@RequestParam String query, HttpServletResponse response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        userPagedInput input = mapper.readValue(query, userPagedInput.class);
        List<userDto> items = List(input).getItems();

        // 声明一个工作簿
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 生成一个表格，设置表格名称为"用户表"
        HSSFSheet sheet = workbook.createSheet("用户表");
        // 设置表格列宽度为10个字节
        sheet.setDefaultColumnWidth(10);
        // 创建标题的显示样式
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.YELLOW.index);
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        // 创建第一行表头
        HSSFRow headrow = sheet.createRow(0);

        // 表头数据
        String[] header = { "用户名", "真实姓名", "性别", "联系电话", "用户角色", "账号状态" };
        // 遍历添加表头
        for (int i = 0; i < header.length; i++) {
            HSSFCell cell = headrow.createCell(i);
            HSSFRichTextString text = new HSSFRichTextString(header[i]);
            cell.setCellValue(text);
            cell.setCellStyle(headerStyle);
        }

        for (int i = 0; i < items.size(); i++) {
            userDto user = items.get(i);
            HSSFRow row = sheet.createRow(i + 1);

            if (user.getUsername() != null) {
                row.createCell(0).setCellValue(new HSSFRichTextString(user.getUsername()));
            }
            if (user.getReal_name() != null) {
                row.createCell(1).setCellValue(new HSSFRichTextString(user.getReal_name()));
            }
            if (user.getGender() != null) {
                row.createCell(2).setCellValue(new HSSFRichTextString(user.getGender()));
            }
            if (user.getPhone() != null) {
                row.createCell(3).setCellValue(new HSSFRichTextString(user.getPhone()));
            }
            if (user.getRole() != null) {
                row.createCell(4).setCellValue(new HSSFRichTextString(user.getRole()));
            }
            if (user.getStatus() != null) {
                row.createCell(5).setCellValue(new HSSFRichTextString(user.getStatus() ? "启用" : "禁用"));
            }
        }

        // 准备将Excel的输出流通过response输出到页面下载
        response.setContentType("application/octet-stream");
        response.setHeader("Content-disposition", "attachment;filename=" + System.currentTimeMillis() + ".xls");
        response.flushBuffer();
        workbook.write(response.getOutputStream());
        workbook.close();
    }
}
