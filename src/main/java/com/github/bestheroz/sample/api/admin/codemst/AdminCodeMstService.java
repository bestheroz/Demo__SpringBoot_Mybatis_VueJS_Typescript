package com.github.bestheroz.sample.api.admin.codemst;

import com.github.bestheroz.sample.api.admin.codemst.response.GetSampleCodeMstVOListResponseVO;
import com.github.bestheroz.sample.api.tablevo.samplecodedet.TableSampleCodeDetDAO;
import com.github.bestheroz.sample.api.tablevo.samplecodedet.TableSampleCodeDetVO;
import com.github.bestheroz.sample.api.tablevo.samplecodemst.TableSampleCodeMstDAO;
import com.github.bestheroz.sample.api.tablevo.samplecodemst.TableSampleCodeMstVO;
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
        return MyMapperUtils.writeObjectAsArrayList(this.tableSampleCodeMstDAO.getList(new TableSampleCodeMstVO(), Collections.EMPTY_SET, "UPDATED DESC"), GetSampleCodeMstVOListResponseVO.class);
    }

    public TableSampleCodeMstVO getVO(final String groupCode) throws CommonException {
        final TableSampleCodeMstVO tableSampleCodeMstVO = new TableSampleCodeMstVO();
        tableSampleCodeMstVO.setGroupCode(groupCode);
        return MyMapperUtils.writeObjectAsObject(this.tableSampleCodeMstDAO.getVO(tableSampleCodeMstVO, Collections.singleton("groupCode")), TableSampleCodeMstVO.class);
    }

    public void insert(final TableSampleCodeMstVO vo) throws CommonException {
        this.tableSampleCodeMstDAO.insert(vo);
        this.session.removeAttribute("ValueLabel." + vo.getGroupCode());
    }

    public void update(final TableSampleCodeMstVO vo) throws CommonException {
        this.tableSampleCodeMstDAO.update(vo, Collections.singleton("groupCode"), null);
        this.session.removeAttribute("ValueLabel." + vo.getGroupCode());
    }

    // @Transactional
    public void delete(final String groupCode) throws CommonException {
        final DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
        defaultTransactionDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        final TransactionStatus status = this.platformTransactionManager.getTransaction(defaultTransactionDefinition);

        try {
            final TableSampleCodeDetVO tableSampleCodeDetVO = new TableSampleCodeDetVO();
            tableSampleCodeDetVO.setGroupCode(groupCode);
            try {
                this.tableSampleCodeDetDAO.delete(tableSampleCodeDetVO, Collections.singleton("groupCode"));
            } catch (final CommonException e) {
                if (!e.isExceptionNoDataSuccesss()) {
                    this.logger.warn(ExceptionUtils.getStackTrace(e));
                    throw e;
                }
            }
            final TableSampleCodeMstVO tableSampleCodeMstVO = new TableSampleCodeMstVO();
            tableSampleCodeMstVO.setGroupCode(groupCode);
            this.tableSampleCodeMstDAO.delete(tableSampleCodeMstVO, Collections.singleton("groupCode"));
            this.session.removeAttribute("ValueLabel." + tableSampleCodeMstVO.getGroupCode());
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
