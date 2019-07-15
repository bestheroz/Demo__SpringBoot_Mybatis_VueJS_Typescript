package com.github.bestheroz.standard.test;

import com.github.bestheroz.standard.common.util.MyTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

@SpringJUnitConfig(locations = {"file:src/main/webapp/WEB-INF/spring/root-context.xml", "file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml"})
@WebAppConfiguration
@Transactional
public class DefaultTestClass {
    @Autowired
    protected WebApplicationContext wac;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MyTestUtils.setMockMvc(MockMvcBuilders.webAppContextSetup(this.wac).build());
    }

    @Test
    public void 초기구동확인() throws Exception {
        MyTestUtils.performGet("/").andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
    }

}
