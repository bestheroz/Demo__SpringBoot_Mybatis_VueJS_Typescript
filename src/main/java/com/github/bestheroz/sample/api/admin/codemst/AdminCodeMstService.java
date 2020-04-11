package com.github.bestheroz.sample.api.admin.codemst;

import com.github.bestheroz.sample.api.entity.samplecodedet.TableSampleCodeDetRepository;
import com.github.bestheroz.sample.api.entity.samplecodemst.TableSampleCodeMstRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Slf4j
@Service
public class AdminCodeMstService {
    @Resource private TableSampleCodeMstRepository tableSampleCodeMstRepository;
    @Resource private TableSampleCodeDetRepository tableSampleCodeDetRepository;

    @Transactional
    public void delete(final String groupCode) {
        this.tableSampleCodeDetRepository.deleteByGroupCode(groupCode);
        this.tableSampleCodeMstRepository.deleteById(groupCode);
    }
}
