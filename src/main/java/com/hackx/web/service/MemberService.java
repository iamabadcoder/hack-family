package com.hackx.web.service;

import com.hackx.web.domain.MemberDO;

public interface MemberService {
    MemberDO queryById(Long id);
}
