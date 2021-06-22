package com.utils;

import com.entity.StaffEntity;
import com.model.StaffResourceModel;
import com.resolver.anotation.Pagination;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.NoSuchElementException;

import static com.utils.ValidatorUtils.checkExistFieldOfClass;

public class PaginationUtils {

    public static Pageable covertToPageable(Pagination pagination) {
        Pageable pageable;

        if (pagination.sortType().equals("dsc")) {
            pageable = PageRequest.of(pagination.page(), pagination.perPage(), Sort.by(pagination.sortBy()).descending());
        } else {
            pageable = PageRequest.of(pagination.page(), pagination.perPage(), Sort.by(pagination.sortBy()).ascending());
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
