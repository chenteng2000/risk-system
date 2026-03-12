package com.investment.risk.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.investment.risk.entity.BusiProject;
import com.investment.risk.mapper.BusiProjectMapper;
import com.investment.risk.service.BusiProjectService;
import org.springframework.stereotype.Service;

@Service
public class BusiProjectServiceImpl extends ServiceImpl<BusiProjectMapper, BusiProject> implements BusiProjectService {
}