package com.resolver;

import com.resolver.anotation.Pagination;
import org.springframework.core.MethodParameter;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;

@Validated
public class PaginationResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(Pagination.class) != null;
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

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
        if(sortType == null) {
            sortType = "asc";
        }
        if(sortBy == null) {
            sortBy = "firstName";
        }

        int finalPageNum = pageNum;
        int finalPerPageNum = perPageNum;
        String finalSortBy = sortBy;
        String finalSortType = sortType;

        return new Pagination() {
            @Override
            public int page() {
                return finalPageNum;
            }

            @Override
            public int perPage() {
                return finalPerPageNum;
            }

            @Override
            public String sortBy() {return finalSortBy; }

            @Override
            public String sortType() {
                return finalSortType;
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return Pagination.class;
            }
        };
    }
}
