package com.github.bestheroz.sample.api.admin.member;

import com.github.bestheroz.sample.api.admin.member.response.GetSampleMemberMstVOResponseVO;
import com.github.bestheroz.sample.api.tablevo.samplemembermst.TableSampleMemberMstDAO;
import com.github.bestheroz.sample.api.tablevo.samplemembermst.TableSampleMemberMstVO;
import com.github.bestheroz.standard.common.exception.CommonException;
import com.github.bestheroz.standard.common.util.MyMapperUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class AdminMemberService {
    @Autowired
    private TableSampleMemberMstDAO tableMemberMstDAO;

    public List<GetSampleMemberMstVOResponseVO> getList() throws CommonException {
        return MyMapperUtils.writeObjectAsArrayList(this.tableMemberMstDAO.getList(new TableSampleMemberMstVO(), Collections.EMPTY_SET, "UPDATED DESC"), GetSampleMemberMstVOResponseVO.class);
    }

    public GetSampleMemberMstVOResponseVO getVO(final String memberId) throws CommonException {
        final TableSampleMemberMstVO tableSampleMemberMstVO = new TableSampleMemberMstVO();
        tableSampleMemberMstVO.setMemberId(memberId);
        return MyMapperUtils.writeObjectAsObject(this.tableMemberMstDAO.getVO(tableSampleMemberMstVO, Collections.singleton("memberId")), GetSampleMemberMstVOResponseVO.class);
    }

    public void insert(final TableSampleMemberMstVO vo) throws CommonException {
        if (StringUtils.isEmpty(vo.getUpdatedBy())) {
            vo.setUpdatedBy(vo.getCreatedBy());
        }
        this.tableMemberMstDAO.insert(vo);
    }

    public void update(final TableSampleMemberMstVO vo) throws CommonException {
        this.tableMemberMstDAO.update(vo, Collections.singleton("memberId"), null);
    }

    public void delete(final String memberId) throws CommonException {
        final TableSampleMemberMstVO tableSampleMemberMstVO = new TableSampleMemberMstVO();
        tableSampleMemberMstVO.setMemberId(memberId);
        this.tableMemberMstDAO.delete(tableSampleMemberMstVO, Collections.singleton("memberId"));
    }
}
