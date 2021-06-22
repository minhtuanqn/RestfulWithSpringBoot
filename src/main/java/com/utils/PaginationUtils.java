package com.utils;

import com.customexception.NoSuchFieldSortByOfClassException;
import com.entity.StaffEntity;
import com.model.StaffResourceModel;
import com.resolver.anotation.Pagination;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


import static com.utils.ValidatorUtils.checkExistFieldOfClass;

public class PaginationUtils {

    public static Pageable covertToPageable(Pagination pagination) {
        Pageable pageable;
        String defaultSortBy = "firstName";

        if(pagination.sortBy() != null && !checkExistFieldOfClass(StaffEntity.class, pagination.sortBy())) {
                throw new NoSuchFieldSortByOfClassException("Can not define sortBy");
        }
        if (pagination.sortType() != null && pagination.sortType().equals("dsc")) {
            pageable = PageRequest.of(pagination.page(), pagination.perPage(), Sort.by(defaultSortBy).descending());
        } else {
            pageable = PageRequest.of(pagination.page(), pagination.perPage(), Sort.by(defaultSortBy).ascending());
        }
        return pageable;
    }

    public static StaffResourceModel buildPagination(Pagination pagination, Page<StaffEntity> page, StaffResourceModel resource) {
        resource.setTotalPage(page.getTotalPages());
        resource.setTotal((int) page.getTotalElements());
        resource.setPage(pagination.page());
        resource.setPerPage(pagination.perPage());
        return resource;
    }
}
