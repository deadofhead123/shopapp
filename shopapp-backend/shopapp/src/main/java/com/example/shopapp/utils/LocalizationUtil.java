package com.example.shopapp.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class LocalizationUtil {
    private final MessageSource messageSource;
    private final LocaleResolver localeResolver;

    public String getLocalizedMessage(String message, Object ... params){ // "Object ... params" là spread operator, tức là bạn có thể truyền nhiều giá trị, hoặc 1 giá trị
        HttpServletRequest request = WebUtil.getCurrentRequest();
        Locale locale = localeResolver.resolveLocale(request);
        List<Object> paramMessages = new ArrayList<>();

        for(Object param : params){
            String messageCode = param.toString();
            if(messageCode.contains(".") && messageCode.contains("_")) paramMessages.add(messageSource.getMessage(messageCode, null, locale));
            else paramMessages.add(param);
        }

        return messageSource.getMessage(message, paramMessages.toArray(), locale);
    }
}
