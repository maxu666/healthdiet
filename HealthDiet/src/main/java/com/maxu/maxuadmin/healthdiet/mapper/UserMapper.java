package com.maxu.maxuadmin.healthdiet.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.maxu.maxuadmin.healthdiet.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("SELECT DISTINCT u.* FROM user u " +
            "JOIN chat_session cs ON u.id = cs.user_id " +
            "WHERE cs.nutritionist_id = #{nutritionistId} " +
            "ORDER BY u.id")
    List<User> selectConsultUsers(@Param("nutritionistId") Long nutritionistId);

}
