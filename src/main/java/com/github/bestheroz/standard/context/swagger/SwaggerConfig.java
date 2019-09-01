package com.github.bestheroz.standard.context.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket api1() {
        return this.getDocket("로그인", "com.github.bestheroz.sample.web.login", "/sample/login/**");
    }

    @Bean
    public Docket api2() {
        return this.getDocket("<value, label> 코드", "com.github.bestheroz.standard.common.valuelabel", "/common/valuelabel/**");
    }

    @Bean
    public Docket api3() {
        return this.getDocket("회원관리", "com.github.bestheroz.sample.web.admin.member", "/sample/admin/member/**");
    }

    @Bean
    public Docket api4() {
        return this.getDocket("메뉴관리", "com.github.bestheroz.sample.web.admin.menu", "/sample/admin/menu/**");
    }

    @Bean
    public Docket api5() {
        return this.getDocket("코드mst관리", "com.github.bestheroz.sample.web.admin.codemst", "/sample/admin/codemst/**");
    }

    @Bean
    public Docket api6() {
        return this.getDocket("코드det관리", "com.github.bestheroz.sample.web.admin.codedet", "/sample/admin/codedet/**");
    }

    public Docket getDocket(final String groupName, final String basePackage, final String paths) {
        return new Docket(DocumentationType.SWAGGER_2).groupName(groupName).useDefaultResponseMessages(false).select().apis(RequestHandlerSelectors.basePackage(basePackage))
                .paths(PathSelectors.ant(paths)).build().apiInfo(this.getApiInfo(groupName));
        // return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.basePackage(basePackage)).paths(PathSelectors.ant(paths)).build();
    }

    private ApiInfo getApiInfo(final String groupName) {
        return new ApiInfoBuilder().title("SK M&Service API System").description(groupName + " API Document").termsOfServiceUrl("http://www.myervice.com").version("181107")
                .license("Apache License Version 2.0").licenseUrl("http://www.apache.org/licenses/LICENSE-2.0").build();
    }
}
