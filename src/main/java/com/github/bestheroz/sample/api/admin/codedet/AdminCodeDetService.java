package com.github.bestheroz.sample.api.admin.codedet;

import com.github.bestheroz.sample.api.admin.codedet.response.GetSampleCodeDetVOListResponseVO;
import com.github.bestheroz.sample.api.admin.codedet.response.GetSampleCodeDetVOResponseVO;
import com.github.bestheroz.sample.api.tablevo.samplecodedet.TableSampleCodeDetDAO;
import com.github.bestheroz.sample.api.tablevo.samplecodedet.TableSampleCodeDetVO;
import com.github.bestheroz.standard.common.exception.CommonException;
import com.github.bestheroz.standard.common.util.MyMapperUtils;
import com.google.common.collect.ImmutableSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.List;

@Service
public class AdminCodeDetService {
    @Autowired
    private PlatformTransactionManager platformTransactionManager;
    @Autowired
    private HttpSession session;
    @Autowired
    private TableSampleCodeDetDAO tableSampleCodeDetDAO;

    // DETAIL PART
    public List<GetSampleCodeDetVOListResponseVO> getList(final String groupCode) throws CommonException {
        final TableSampleCodeDetVO tableSampleCodeDetVO = new TableSampleCodeDetVO();
        tableSampleCodeDetVO.setGroupCode(groupCode);
        return MyMapperUtils
                .writeObjectAsArrayList(this.tableSampleCodeDetDAO.getList(tableSampleCodeDetVO, Collections.singleton("groupCode"), "UPDATED DESC"), GetSampleCodeDetVOListResponseVO.class);
    }

    public GetSampleCodeDetVOResponseVO getOne(final String groupCode, final String code) throws CommonException {
        final TableSampleCodeDetVO tableSampleCodeDetVO = new TableSampleCodeDetVO();
        tableSampleCodeDetVO.setGroupCode(groupCode);
        tableSampleCodeDetVO.setCode(code);
        return MyMapperUtils.writeObjectAsObject(this.tableSampleCodeDetDAO.getOne(tableSampleCodeDetVO, ImmutableSet.of("groupCode", "code")), GetSampleCodeDetVOResponseVO.class);
    }

    public void insert(final TableSampleCodeDetVO vo) throws CommonException {
        this.tableSampleCodeDetDAO.insert(vo);
        this.session.removeAttribute("ValueLabel." + vo.getGroupCode());
    }

    public void update(final TableSampleCodeDetVO vo) throws CommonException {
        this.tableSampleCodeDetDAO.update(vo, ImmutableSet.of("groupCode", "code"), null);
        this.session.removeAttribute("ValueLabel." + vo.getGroupCode());
    }

    public void delete(final String groupCode, final String code) throws CommonException {
        final TableSampleCodeDetVO tableSampleCodeDetVO = new TableSampleCodeDetVO();
        tableSampleCodeDetVO.setGroupCode(groupCode);
        tableSampleCodeDetVO.setCode(code);
        this.tableSampleCodeDetDAO.delete(tableSampleCodeDetVO, ImmutableSet.of("groupCode", "code"));
        this.session.removeAttribute("ValueLabel." + tableSampleCodeDetVO.getGroupCode());
    }

}
