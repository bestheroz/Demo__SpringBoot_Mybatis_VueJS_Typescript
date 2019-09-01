package com.github.bestheroz.sample.web.admin.codemst;

import com.github.bestheroz.sample.web.admin.codemst.response.GetSampleCodeMstVOListResponseVO;
import com.github.bestheroz.sample.web.tablevo.samplecodedet.TableSampleCodeDetDAO;
import com.github.bestheroz.sample.web.tablevo.samplecodedet.TableSampleCodeDetVO;
import com.github.bestheroz.sample.web.tablevo.samplecodemst.TableSampleCodeMstDAO;
import com.github.bestheroz.sample.web.tablevo.samplecodemst.TableSampleCodeMstVO;
import com.github.bestheroz.standard.common.exception.CommonException;
import com.github.bestheroz.standard.common.util.MyMapperUtils;
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
import java.util.Collections;
import java.util.List;

@Service
public class AdminCodeMstService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private PlatformTransactionManager platformTransactionManager;
    @Autowired
    private HttpSession session;
    @Autowired
    private TableSampleCodeMstDAO tableSampleCodeMstDAO;
    @Autowired
    private TableSampleCodeDetDAO tableSampleCodeDetDAO;

    public List<GetSampleCodeMstVOListResponseVO> getList() throws CommonException {
        return MyMapperUtils.writeObjectAsArrayList(this.tableSampleCodeMstDAO.getList(new TableSampleCodeMstVO(), Collections.EMPTY_SET, "UPD_DT DESC"), GetSampleCodeMstVOListResponseVO.class);
    }

    public TableSampleCodeMstVO getVO(final String grcode) throws CommonException {
        final TableSampleCodeMstVO tableSampleCodeMstVO = new TableSampleCodeMstVO();
        tableSampleCodeMstVO.setGrcode(grcode);
        return MyMapperUtils.writeObjectAsObject(this.tableSampleCodeMstDAO.getVO(tableSampleCodeMstVO, Collections.singleton("grcode")), TableSampleCodeMstVO.class);
    }

    public void insert(final TableSampleCodeMstVO vo) throws CommonException {
        this.tableSampleCodeMstDAO.insert(vo);
        this.session.removeAttribute("ValueLabel." + vo.getGrcode());
    }

    public void update(final TableSampleCodeMstVO vo) throws CommonException {
        this.tableSampleCodeMstDAO.update(vo, Collections.singleton("grcode"), null);
        this.session.removeAttribute("ValueLabel." + vo.getGrcode());
    }

    // @Transactional
    public void delete(final String grcode) throws CommonException {
        final DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
        defaultTransactionDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        final TransactionStatus status = this.platformTransactionManager.getTransaction(defaultTransactionDefinition);

        try {
            final TableSampleCodeDetVO tableSampleCodeDetVO = new TableSampleCodeDetVO();
            tableSampleCodeDetVO.setGrcode(grcode);
            try {
                this.tableSampleCodeDetDAO.delete(tableSampleCodeDetVO, Collections.singleton("grcode"));
            } catch (final CommonException e) {
                if (!e.isExceptionNoDataSuccesss()) {
                    this.logger.warn(ExceptionUtils.getStackTrace(e));
                    throw e;
                }
            }
            final TableSampleCodeMstVO tableSampleCodeMstVO = new TableSampleCodeMstVO();
            tableSampleCodeMstVO.setGrcode(grcode);
            this.tableSampleCodeMstDAO.delete(tableSampleCodeMstVO, Collections.singleton("grcode"));
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
}
