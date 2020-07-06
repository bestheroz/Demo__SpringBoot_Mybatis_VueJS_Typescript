package com.github.bestheroz.standard.context.converter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.stereotype.Component;

@Component
@Primary
public class ConversionServiceContext extends DefaultConversionService implements ConversionService {
    @Bean(name = "conversionService")
    public ConversionService getConversionService() {
//        final Set<Converter<?, ?>> set = new HashSet<>();
//        set.add(new DateTimeConverter());
//        set.add(new OffsetDateTimeConverter());
        final ConversionServiceFactoryBean bean = new ConversionServiceFactoryBean();
//        bean.setConverters(set);
        bean.afterPropertiesSet();
        return bean.getObject();
    }
}
