package org.michibe.libraryshowcase.configuration.web;

import org.michibe.libraryshowcase.modules.user.model.UserId;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToUserIdConverter implements Converter<String, UserId> {
    @Override
    public UserId convert(String source) {
        return UserId.ofString(source);
    }
}
