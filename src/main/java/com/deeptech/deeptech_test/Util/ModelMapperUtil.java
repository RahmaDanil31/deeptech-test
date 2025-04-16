package com.deeptech.deeptech_test.Util;

import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

public class ModelMapperUtil {
    public static <T> T convert(Object source, Class<T> destinationType) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(source, destinationType);
    }

    public static  <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
        ModelMapper modelMapper = new ModelMapper();
        return source.stream()
                .map(element -> modelMapper.map(element, targetClass))
                .collect(Collectors.toList());
    }
}
