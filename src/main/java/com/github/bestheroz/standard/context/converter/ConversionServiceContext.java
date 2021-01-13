package com.github.bestheroz.standard.context.converter;

import java.util.Set;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.stereotype.Component;

@Component
@Primary
public class ConversionServiceContext extends DefaultConversionService
    implements ConversionService {

  @Bean(name = "conversionService")
  public ConversionService getConversionService() {
    final ConversionServiceFactoryBean bean = new ConversionServiceFactoryBean();
    bean.setConverters(Set.of(new InstantConverter()));
    bean.afterPropertiesSet();
    return bean.getObject();
  }
}
