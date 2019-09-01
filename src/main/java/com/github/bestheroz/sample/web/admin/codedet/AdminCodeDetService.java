package com.github.bestheroz.sample.web.admin.codedet;

import com.github.bestheroz.sample.web.admin.codedet.response.GetSampleCodeDetVOListResponseVO;
import com.github.bestheroz.sample.web.admin.codedet.response.GetSampleCodeDetVOResponseVO;
import com.github.bestheroz.sample.web.tablevo.samplecodedet.TableSampleCodeDetDAO;
import com.github.bestheroz.sample.web.tablevo.samplecodedet.TableSampleCodeDetVO;
import com.github.bestheroz.sample.web.tablevo.samplecodemst.TableSampleCodeMstDAO;
import com.github.bestheroz.standard.common.exception.CommonException;
import com.github.bestheroz.standard.common.util.MyMapperUtils;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.List;

@Service
public class AdminCodeDetService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private PlatformTransactionManager platformTransactionManager;
    @Autowired
    private HttpSession session;
    @Autowired
    private TableSampleCodeMstDAO tableSampleCodeMstDAO;
    @Autowired
    private TableSampleCodeDetDAO tableSampleCodeDetDAO;

    // DETAIL PART
    public List<GetSampleCodeDetVOListResponseVO> getList(final String grcode) throws CommonException {
        final TableSampleCodeDetVO tableSampleCodeDetVO = new TableSampleCodeDetVO();
        tableSampleCodeDetVO.setGrcode(grcode);
        return MyMapperUtils.writeObjectAsArrayList(this.tableSampleCodeDetDAO.getList(tableSampleCodeDetVO, Collections.singleton("grcode"), "UPD_DT DESC"), GetSampleCodeDetVOListResponseVO.class);
    }

    public GetSampleCodeDetVOResponseVO getVO(final String grcode, final String code) throws CommonException {
        final TableSampleCodeDetVO tableSampleCodeDetVO = new TableSampleCodeDetVO();
        tableSampleCodeDetVO.setGrcode(grcode);
        tableSampleCodeDetVO.setCode(code);
        return MyMapperUtils.writeObjectAsObject(this.tableSampleCodeDetDAO.getVO(tableSampleCodeDetVO, Sets.newHashSet("grcode", "code")), GetSampleCodeDetVOResponseVO.class);
    }

    public void insert(final TableSampleCodeDetVO vo) throws CommonException {
        this.tableSampleCodeDetDAO.insert(vo);
        this.session.removeAttribute("ValueLabel." + vo.getGrcode());
    }

    public void update(final TableSampleCodeDetVO vo) throws CommonException {
        this.tableSampleCodeDetDAO.update(vo, Sets.newHashSet("grcode", "code"), null);
        this.session.removeAttribute("ValueLabel." + vo.getGrcode());
    }

    public void delete(final String grcode, final String code) throws CommonException {
        final TableSampleCodeDetVO tableSampleCodeDetVO = new TableSampleCodeDetVO();
        tableSampleCodeDetVO.setGrcode(grcode);
        tableSampleCodeDetVO.setCode(code);
        this.tableSampleCodeDetDAO.delete(tableSampleCodeDetVO, Sets.newHashSet("grcode", "code"));
        this.session.removeAttribute("ValueLabel." + tableSampleCodeDetVO.getGrcode());
    }

}
