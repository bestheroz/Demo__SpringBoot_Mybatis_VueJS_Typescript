package com.github.bestheroz.standard.common.util;

import com.github.bestheroz.standard.common.constant.CommonCode;
import com.github.bestheroz.standard.common.exception.CommonException;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Map.Entry;

public class MyTestUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyTestUtils.class);

    protected MyTestUtils() {
        throw new UnsupportedOperationException();
    }

    private static MockMvc mockMvc = null;

    public static void setMockMvc(final MockMvc mockMvc) {
        MyTestUtils.mockMvc = mockMvc;
    }

    public static ResultActions performGet(final String url) {
        return performGet(url, null);
    }

    public static ResultActions performGet(final String url, final Object params) {
        try {
            final MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.get(StringUtils.startsWith(url, CommonCode.CONTEXT_PATH) ? url : CommonCode.CONTEXT_PATH + url);
            for (final Entry<String, JsonElement> entry : MyMapperUtils.writeObjectAsJsonObject(params).entrySet()) {
                mockHttpServletRequestBuilder.param(entry.getKey(), String.valueOf(entry.getValue()));
            }
            final ResultActions resultActions = mockMvc.perform(mockHttpServletRequestBuilder);
            MyTestUtils.logReponse(resultActions.andReturn());
            return resultActions;
        } catch (final Throwable e) {
            LOGGER.warn(ExceptionUtils.getStackTrace(e));
            throw new CommonException(e);
        }
    }

    public static ResultActions performPost(final String url) {
        return performPost(url, null);
    }

    public static ResultActions performPost(final String url, final Object params) {
        try {
            final MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.post(StringUtils.startsWith(url, CommonCode.CONTEXT_PATH) ? url : CommonCode.CONTEXT_PATH + url);
            for (final Entry<String, JsonElement> entry : MyMapperUtils.writeObjectAsJsonObject(params).entrySet()) {
                mockHttpServletRequestBuilder.param(entry.getKey(), entry.getValue().getAsString());
            }
            final ResultActions resultActions = mockMvc.perform(mockHttpServletRequestBuilder);
            MyTestUtils.logReponse(resultActions.andReturn());
            return resultActions;
        } catch (final Throwable e) {
            LOGGER.warn(ExceptionUtils.getStackTrace(e));
            throw new CommonException(e);
        }
    }

    public static void logReponse(final MvcResult mvcResult) {
        try {
            final String requestURI = mvcResult.getRequest().getRequestURI();
            final JsonObject response = MyMapperUtils.writeObjectAsJsonObject(mvcResult.getResponse());
            response.remove("content");
            response.remove("outputStream");
            LOGGER.debug("result.header : {}\n{}", requestURI, response.toString());
            LOGGER.debug("result.content : {}\n{}", requestURI, mvcResult.getResponse().getContentAsString());
        } catch (final Throwable e) {
            LOGGER.warn(ExceptionUtils.getStackTrace(e));
            throw new CommonException(e);
        }
    }
}
