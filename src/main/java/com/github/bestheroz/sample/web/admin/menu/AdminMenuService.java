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

    public List<GetSampleMenuMstVOListResponseVO> getList(final Integer menuId, final String menuNm) throws CommonException {
        final TableSampleMenuMstVO tableSampleMenuMstVO = new TableSampleMenuMstVO();
        tableSampleMenuMstVO.setMenuId(menuId);
        tableSampleMenuMstVO.setMenuNm(menuNm);
        return this.adminMenuDAO.getList(tableSampleMenuMstVO);
    }

    public void insert(final TableSampleMenuMstVO vo) throws CommonException {
        this.tableSampleMenuMstDAO.insert(vo);
    }

    public void update(final TableSampleMenuMstVO vo) throws CommonException {
        this.tableSampleMenuMstDAO.update(vo, Collections.singleton("menuId"), null);
    }

    public void delete(final Integer menuId) throws CommonException {
        final TableSampleMenuMstVO vo = new TableSampleMenuMstVO();
        vo.setMenuId(menuId);
        this.tableSampleMenuMstDAO.delete(vo, Collections.singleton("menuId"));
    }

    public List<GetValueLabeVOListResponseVO> getMenuTypG() throws CommonException {
        return this.adminMenuDAO.getMenuTypG();
    }
}
