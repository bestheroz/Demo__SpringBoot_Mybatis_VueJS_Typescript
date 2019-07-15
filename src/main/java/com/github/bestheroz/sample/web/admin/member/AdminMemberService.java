package com.github.bestheroz.sample.web.admin.member;

import com.github.bestheroz.sample.web.admin.member.response.GetSampleMemberMstVOListResponseVO;
import com.github.bestheroz.sample.web.tablevo.samplemembermst.TableSampleMemberMstDAO;
import com.github.bestheroz.sample.web.tablevo.samplemembermst.TableSampleMemberMstVO;
import com.github.bestheroz.standard.common.exception.CommonException;
import com.github.bestheroz.standard.common.util.MyMapperUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class AdminMemberService {
    @Autowired
    private TableSampleMemberMstDAO tableMemberMstDAO;

    public List<GetSampleMemberMstVOListResponseVO> getSampleMemberMstVOList(final String memberId) throws CommonException {
        final List<String> whereKey = new ArrayList<>();
        if (StringUtils.isNotEmpty(memberId)) {
            whereKey.add("memberId");
        }
        final TableSampleMemberMstVO tableSampleMemberMstVO = new TableSampleMemberMstVO();
        tableSampleMemberMstVO.setMemberId(memberId);
        return MyMapperUtils.writeObjectAsArrayList(this.tableMemberMstDAO.getSampleMemberMstVOList(tableSampleMemberMstVO, whereKey, "UPD_DT DESC"), GetSampleMemberMstVOListResponseVO.class);
    }

    public void insertSampleMemberMst(final TableSampleMemberMstVO vo) throws CommonException {
        if (StringUtils.isEmpty(vo.getUpdMemberId())) {
            vo.setUpdMemberId(vo.getRegMemberId());
        }
        this.tableMemberMstDAO.insertSampleMemberMst(vo);
    }

    public void updateSampleMemberMst(final TableSampleMemberMstVO vo) throws CommonException {
        this.tableMemberMstDAO.updateSampleMemberMst(vo, Collections.singletonList("memberId"), null);
    }

    public void deleteSampleMemberMst(final String memberId) throws CommonException {
        final TableSampleMemberMstVO tableSampleMemberMstVO = new TableSampleMemberMstVO();
        tableSampleMemberMstVO.setMemberId(memberId);
        this.tableMemberMstDAO.deleteSampleMemberMst(tableSampleMemberMstVO, Collections.singletonList("memberId"));
    }
}
