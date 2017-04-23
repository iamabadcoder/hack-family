package com.hackx.web.mapper;

import com.hackx.web.domain.MemberDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {
    MemberDO queryById(Long id);
}
