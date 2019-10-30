package com.jira.user.mapper;

import com.jira.user.dto.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserMapper {
    @Select("select user_id as userId,user_name as userName, password "+
            "from userinfo where user_id=#{id}")
    UserInfo getUserById(@Param("id") int id);

    @Select("select user_id as userId,user_name as userName, password " +
            "from userinfo where user_name=#{userName}")
    UserInfo getUserByName(@Param("userName") String userName);
}
