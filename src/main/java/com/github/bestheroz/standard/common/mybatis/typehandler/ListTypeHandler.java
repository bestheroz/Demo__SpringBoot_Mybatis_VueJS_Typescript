package com.github.bestheroz.standard.common.mybatis.typehandler;

import java.util.List;
import org.apache.ibatis.type.MappedTypes;

@MappedTypes(value = {List.class})
public class ListTypeHandler extends AbstractListTypeHandler<List<?>> {}
