package com.glerk.core.config.User;

import com.glerk.core.entity.User;
import com.glerk.core.exception.BusinessException;
import com.glerk.core.exception.ErrorCode;
import com.glerk.core.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.security.Principal;

@Component
public class CurrentUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final UserService userService;

    public CurrentUserArgumentResolver(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(User.class) &&
                parameter.hasParameterAnnotation(CurrentUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String requestURI = request.getRequestURI();

        if (requestURI.startsWith("/v1/oauth")) {
            return null;
        }

        Principal principal = webRequest.getUserPrincipal();
        if (principal == null) {
            throw new BusinessException(ErrorCode.INVALID_TOKEN);
        }

        return userService.findUser(Long.valueOf(principal.getName()));
    }
}
