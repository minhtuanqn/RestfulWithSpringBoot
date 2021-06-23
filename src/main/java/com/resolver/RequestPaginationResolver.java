package com.resolver;

import com.model.PaginationModel;
import com.resolver.anotation.RequestPagingParam;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

public class RequestPaginationResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(RequestPagingParam.class) != null;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String page = request.getParameter("page");
        String perPage = request.getParameter("perPage");
        String sortType = request.getParameter("sortType");
        String sortBy = request.getParameter("sortBy");
        int pageNum = 0;
        int perPageNum = 10;
        if(page != null) {
            pageNum = Integer.parseInt(page);
        }
        if(perPage != null) {
            perPageNum = Integer.parseInt(perPage);
        }

        return new PaginationModel(pageNum, perPageNum, sortBy, sortType);
    }
}
