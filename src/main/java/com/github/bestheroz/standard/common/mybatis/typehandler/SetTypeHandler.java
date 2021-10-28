package com.github.bestheroz.standard.common.mybatis.typehandler;

import java.util.Set;
import org.apache.ibatis.type.MappedTypes;

@MappedTypes(value = {Set.class})
public class SetTypeHandler extends AbstractSetTypeHandler<Set<?>> {}
