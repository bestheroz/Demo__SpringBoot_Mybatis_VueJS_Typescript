package com.github.bestheroz.standard.context.filter;

import com.github.bestheroz.standard.common.util.MyMapperUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.MediaType;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class ReadableRequestWrapper extends HttpServletRequestWrapper { // 상속
    private final Charset encoding;
    private final Map<String, String[]> params = new HashMap<>();
    private byte[] rawData;

    public ReadableRequestWrapper(final HttpServletRequest request) {
        super(request);
        this.params.putAll(request.getParameterMap()); // 원래의 파라미터를 저장

        final String charEncoding = request.getCharacterEncoding(); // 인코딩 설정
        this.encoding = StringUtils.isBlank(charEncoding) ? StandardCharsets.UTF_8 : Charset.forName(charEncoding);

        try {
            final InputStream is = request.getInputStream();
            this.rawData = IOUtils.toByteArray(is); // InputStream 을 별도로 저장한 다음 getReader() 에서 새 스트림으로 생성

            // body 파싱
            final String collect = this.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            if (StringUtils.isEmpty(collect)) { // body 가 없을경우 로깅 제외
                return;
            }
            if (request.getContentType() != null && request.getContentType().contains(MediaType.MULTIPART_FORM_DATA_VALUE)) { // 파일 업로드시 로깅제외
                return;
            }
            final JsonElement parse = MyMapperUtils.writeObjectAsJsonElement(collect);
            if (parse instanceof JsonArray) {
                this.setParameter("requestBody", MyMapperUtils.writeObjectAsString(collect));
            } else {
                final JsonObject jsonObject = MyMapperUtils.writeObjectAsJsonObject(collect);
                final Iterator iterator = jsonObject.keySet().iterator();
                while (iterator.hasNext()) {
                    final String key = (String) iterator.next();
                    this.setParameter(key, jsonObject.get(key).toString().replace("\"", "\\\""));
                }
            }
        } catch (final Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
    }

    @Override
    public String getParameter(final String name) {
        final String[] paramArray = this.getParameterValues(name);
        if (paramArray != null && paramArray.length > 0) {
            return paramArray[0];
        } else {
            return null;
        }
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return Collections.unmodifiableMap(this.params);
    }

    @Override
    public Enumeration<String> getParameterNames() {
        return Collections.enumeration(this.params.keySet());
    }

    @Override
    public String[] getParameterValues(final String name) {
        String[] result = null;
        final String[] dummyParamValue = this.params.get(name);

        if (dummyParamValue != null) {
            result = new String[dummyParamValue.length];
            System.arraycopy(dummyParamValue, 0, result, 0, dummyParamValue.length);
        }
        return result;
    }

    public void setParameter(final String name, final String value) {
        final String[] param = {value};
        this.setParameter(name, param);
    }

    public void setParameter(final String name, final String[] values) {
        this.params.put(name, values);
    }

    @Override
    public ServletInputStream getInputStream() {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.rawData);

        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(final ReadListener readListener) {
                // Do nothing
            }

            @Override
            public int read() {
                return byteArrayInputStream.read();
            }
        };
    }

    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(this.getInputStream(), this.encoding));
    }
}
