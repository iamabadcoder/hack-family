package com.hackx.web.service.impl;

import com.hackx.web.domain.MemberDO;
import com.hackx.web.mapper.MemberMapper;
import com.hackx.web.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("memberService")
public class MemberServiceImpl implements MemberService {

    @Autowired
    MemberMapper memberMapper;

    @Override
    public MemberDO queryById(Long id) {
        return memberMapper.queryById(id);
    }
}