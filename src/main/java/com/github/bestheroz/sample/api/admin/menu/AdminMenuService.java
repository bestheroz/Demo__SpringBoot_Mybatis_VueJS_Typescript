package com.github.bestheroz.sample.api.admin.menu;

import com.github.bestheroz.sample.api.admin.menu.response.GetSampleMenuMstVOListResponseVO;
import com.github.bestheroz.sample.api.tablevo.samplemenumst.TableSampleMenuMstDAO;
import com.github.bestheroz.sample.api.tablevo.samplemenumst.TableSampleMenuMstVO;
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
        return this.adminMenuDAO.getList(new TableSampleMenuMstVO(), Collections.EMPTY_SET, "UPDATED DESC");
    }

    public GetSampleMenuMstVOListResponseVO getOne(final Integer menuId, final String menuName) throws CommonException {
        final TableSampleMenuMstVO tableSampleMenuMstVO = new TableSampleMenuMstVO();
        final Set<String> whereKeys = new HashSet<>();
        tableSampleMenuMstVO.setMenuId(menuId);
        tableSampleMenuMstVO.setMenuName(menuName);
        if (menuId != null) {
            whereKeys.add("menuId");
        }
        if (menuName != null) {
            whereKeys.add("menuName");
        }
        return this.adminMenuDAO.getOne(tableSampleMenuMstVO, whereKeys);
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

    List<GetValueLabeVOListResponseVO> getMenuTypeG() throws CommonException {
        return this.adminMenuDAO.getMenuTypeG();
    }
}
