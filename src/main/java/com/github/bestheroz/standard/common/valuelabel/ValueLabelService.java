package com.github.bestheroz.standard.common.valuelabel;

import com.github.bestheroz.standard.common.exception.CommonException;
import com.github.bestheroz.standard.common.valuelabel.response.GetValueLabeVOListResponseVO;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ValueLabelService {
    @Autowired
    private ValueLabelDAO valueLabelDAO;

    public List<GetValueLabeVOListResponseVO> getList(final String groupCode) throws CommonException {
        return this.valueLabelDAO.getList(groupCode);
    }

    public String getLabelByValue(final Map<String, Object> params) throws CommonException {
        return this.valueLabelDAO.getLabelByValue(params);
    }

    public JsonObject getValueLabelVoListToJsonObject(final String groupCode) throws CommonException {
        final JsonObject result = new JsonObject();
        for (final GetValueLabeVOListResponseVO vo : this.getList(groupCode)) {
            result.addProperty(vo.getValue(), vo.getLabel());
        }
        return result;
    }
}
