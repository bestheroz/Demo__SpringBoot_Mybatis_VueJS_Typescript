package com.github.bestheroz.sample.web.admin.menu;

import com.github.bestheroz.sample.web.admin.menu.response.GetSampleMenuMstVOListResponseVO;
import com.github.bestheroz.sample.web.tablevo.samplemenumst.TableSampleMenuMstDAO;
import com.github.bestheroz.sample.web.tablevo.samplemenumst.TableSampleMenuMstVO;
import com.github.bestheroz.standard.common.exception.CommonException;
import com.github.bestheroz.standard.common.valuelabel.response.GetValueLabeVOListResponseVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AdminMenuService {
    @Resource
    private AdminMenuDAO adminMenuDAO;
    @Resource
    private TableSampleMenuMstDAO tableSampleMenuMstDAO;

    public List<GetSampleMenuMstVOListResponseVO> getList() throws CommonException {
        return this.adminMenuDAO.getList(new TableSampleMenuMstVO(), Collections.EMPTY_SET, "UPD_DT DESC");
    }

    public GetSampleMenuMstVOListResponseVO getVO(final Integer menuId, final String menuNm) throws CommonException {
        final TableSampleMenuMstVO tableSampleMenuMstVO = new TableSampleMenuMstVO();
        Set<String> whereKeys = new HashSet<>();
        tableSampleMenuMstVO.setMenuId(menuId);
        tableSampleMenuMstVO.setMenuNm(menuNm);
        if (menuId != null) {
            whereKeys.add("menuId");
        }
        if (menuNm != null) {
            whereKeys.add("menuNm");
        }
        return this.adminMenuDAO.getVO(tableSampleMenuMstVO, whereKeys);
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

    List<GetValueLabeVOListResponseVO> getMenuTypG() throws CommonException {
        return this.adminMenuDAO.getMenuTypG();
    }
}
