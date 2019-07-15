package com.github.bestheroz.sample.web.admin.menu;

import com.github.bestheroz.sample.web.admin.menu.response.GetSampleMenuMstVOListResponseVO;
import com.github.bestheroz.sample.web.tablevo.samplemenumst.TableSampleMenuMstDAO;
import com.github.bestheroz.sample.web.tablevo.samplemenumst.TableSampleMenuMstVO;
import com.github.bestheroz.standard.common.exception.CommonException;
import com.github.bestheroz.standard.common.valuelabel.response.GetValueLabeVOListResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class AdminMenuService {
    @Autowired
    private AdminMenuDAO adminMenuDAO;
    @Autowired
    private TableSampleMenuMstDAO tableSampleMenuMstDAO;

    public List<GetSampleMenuMstVOListResponseVO> getSampleMenuMstVOList(final Integer menuId, final String menuNm) throws CommonException {
        final TableSampleMenuMstVO tableSampleMenuMstVO = new TableSampleMenuMstVO();
        tableSampleMenuMstVO.setMenuId(menuId);
        tableSampleMenuMstVO.setMenuNm(menuNm);
        return this.adminMenuDAO.getSampleMenuMstVOList(tableSampleMenuMstVO);
    }

    public void insertSampleMenuMst(final TableSampleMenuMstVO vo) throws CommonException {
        this.tableSampleMenuMstDAO.insertSampleMenuMst(vo);
    }

    public void updateSampleMenuMst(final TableSampleMenuMstVO vo) throws CommonException {
        this.tableSampleMenuMstDAO.updateSampleMenuMst(vo, Collections.singletonList("menuId"), null);
    }

    public void deleteSampleMenuMst(final Integer menuId) throws CommonException {
        final TableSampleMenuMstVO vo = new TableSampleMenuMstVO();
        vo.setMenuId(menuId);
        this.tableSampleMenuMstDAO.deleteSampleMenuMst(vo, Collections.singletonList("menuId"));
    }

    public List<GetValueLabeVOListResponseVO> getPMenuValueLableVOList() throws CommonException {
        return this.adminMenuDAO.getPMenuValueLableVOList();
    }
}
