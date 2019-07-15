package com.github.bestheroz.sample.web.admin.valuelabel;

import com.github.bestheroz.sample.web.admin.valuelabel.response.GetSampleCodeDetVOListResponseVO;
import com.github.bestheroz.sample.web.admin.valuelabel.response.GetSampleCodeMstVOListResponseVO;
import com.github.bestheroz.sample.web.tablevo.samplecodedet.TableSampleCodeDetDAO;
import com.github.bestheroz.sample.web.tablevo.samplecodedet.TableSampleCodeDetVO;
import com.github.bestheroz.sample.web.tablevo.samplecodemst.TableSampleCodeMstDAO;
import com.github.bestheroz.sample.web.tablevo.samplecodemst.TableSampleCodeMstVO;
import com.github.bestheroz.standard.common.exception.CommonException;
import com.github.bestheroz.standard.common.util.MyMapperUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class AdminValueLabelService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private PlatformTransactionManager platformTransactionManager;
    @Autowired
    private HttpSession session;
    @Autowired
    private TableSampleCodeMstDAO tableSampleCodeMstDAO;
    @Autowired
    private TableSampleCodeDetDAO tableSampleCodeDetDAO;

    public List<GetSampleCodeMstVOListResponseVO> getSampleCodeMstVOList(final String grcode) throws CommonException {
        final List<String> whereKey = new ArrayList<>();
        if (StringUtils.isNotEmpty(grcode)) {
            whereKey.add("grcode");
        }
        final TableSampleCodeMstVO tableSampleCodeMstVO = new TableSampleCodeMstVO();
        tableSampleCodeMstVO.setGrcode(grcode);
        return MyMapperUtils.writeObjectAsArrayList(this.tableSampleCodeMstDAO.getSampleCodeMstVOList(tableSampleCodeMstVO, whereKey, "UPD_DT DESC"), GetSampleCodeMstVOListResponseVO.class);
    }

    public void insertSampleCodeMst(final TableSampleCodeMstVO vo) throws CommonException {
        this.tableSampleCodeMstDAO.insertSampleCodeMst(vo);
        this.session.removeAttribute("ValueLabel." + vo.getGrcode());
    }

    public void updateSampleCodeMst(final TableSampleCodeMstVO vo) throws CommonException {
        this.tableSampleCodeMstDAO.updateSampleCodeMst(vo, Collections.singletonList("grcode"), null);
        this.session.removeAttribute("ValueLabel." + vo.getGrcode());
    }

    // @Transactional
    public void deleteCOMM_CODE(final String grcode) throws CommonException {
        final DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
        defaultTransactionDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        final TransactionStatus status = this.platformTransactionManager.getTransaction(defaultTransactionDefinition);

        try {
            final TableSampleCodeDetVO tableSampleCodeDetVO = new TableSampleCodeDetVO();
            tableSampleCodeDetVO.setGrcode(grcode);
            try {
                this.tableSampleCodeDetDAO.deleteSampleCodeDet(tableSampleCodeDetVO, Collections.singletonList("grcode"));
            } catch (final CommonException e) {
                if (!e.isExceptionNoDataSuccesss()) {
                    this.logger.warn(ExceptionUtils.getStackTrace(e));
                    throw e;
                }
            }
            final TableSampleCodeMstVO tableSampleCodeMstVO = new TableSampleCodeMstVO();
            tableSampleCodeMstVO.setGrcode(grcode);
            this.tableSampleCodeMstDAO.deleteSampleCodeMst(tableSampleCodeMstVO, Collections.singletonList("grcode"));
            this.session.removeAttribute("ValueLabel." + tableSampleCodeMstVO.getGrcode());
            this.platformTransactionManager.commit(status);
        } catch (final CommonException e) {
            if (!status.isCompleted()) {
                this.platformTransactionManager.rollback(status);
            }
            this.logger.warn(ExceptionUtils.getStackTrace(e));
            throw e;
        }
    }

    // DETAIL PART
    public List<GetSampleCodeDetVOListResponseVO> getSampleCodeDetVOList(final String grcode, final String code) throws CommonException {
        final List<String> whereKey = new ArrayList<>();
        whereKey.add("grcode");
        if (StringUtils.isNotEmpty(code)) {
            whereKey.add("code");
        }
        final TableSampleCodeDetVO tableSampleCodeDetVO = new TableSampleCodeDetVO();
        tableSampleCodeDetVO.setGrcode(grcode);
        tableSampleCodeDetVO.setCode(code);
        return MyMapperUtils.writeObjectAsArrayList(this.tableSampleCodeDetDAO.getSampleCodeDetVOList(tableSampleCodeDetVO, whereKey, "UPD_DT DESC"), GetSampleCodeDetVOListResponseVO.class);
    }

    public void insertSampleCodeDet(final TableSampleCodeDetVO vo) throws CommonException {
        this.tableSampleCodeDetDAO.insertSampleCodeDet(vo);
        this.session.removeAttribute("ValueLabel." + vo.getGrcode());
    }

    public void updateSampleCodeDet(final TableSampleCodeDetVO vo) throws CommonException {
        this.tableSampleCodeDetDAO.updateSampleCodeDet(vo, Arrays.asList("grcode", "code"), null);
        this.session.removeAttribute("ValueLabel." + vo.getGrcode());
    }

    public void deleteSampleCodeDet(final String grcode, final String code) throws CommonException {
        final TableSampleCodeDetVO tableSampleCodeDetVO = new TableSampleCodeDetVO();
        tableSampleCodeDetVO.setGrcode(grcode);
        tableSampleCodeDetVO.setCode(code);
        this.tableSampleCodeDetDAO.deleteSampleCodeDet(tableSampleCodeDetVO, Arrays.asList("grcode", "code"));
        this.session.removeAttribute("ValueLabel." + tableSampleCodeDetVO.getGrcode());
    }

}
