package com.convertor;

import com.customexception.NoSuchFieldSortByOfClassException;
import com.entity.StaffEntity;
import com.model.PaginationModel;
import com.model.StaffResourceModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


import static com.utils.ValidatorUtils.checkExistFieldOfClass;

public class PaginationConvertor {

    public Pageable covertToPageable(PaginationModel pagination, String defaultSortBy) {
        Pageable pageable;

        String sortBy = defaultSortBy;
        if(pagination.getSortBy() != null ) {
            if(!checkExistFieldOfClass(StaffEntity.class, pagination.getSortBy())){
                throw new NoSuchFieldSortByOfClassException("Can not define sortBy");
            }
            sortBy = pagination.getSortBy();
        }
        if (pagination.getSortType() != null && pagination.getSortType().equals("dsc")) {
            pageable = PageRequest.of(pagination.getPage(), pagination.getPerPage(), Sort.by(sortBy).descending());
        } else {
            pageable = PageRequest.of(pagination.getPage(), pagination.getPerPage(), Sort.by(sortBy).ascending());
        }
        return pageable;
    }

    public StaffResourceModel buildPagination(PaginationModel pagination, Page<StaffEntity> page, StaffResourceModel resource) {
        resource.setTotalPage(page.getTotalPages());
        resource.setTotal((int) page.getTotalElements());
        resource.setPage(pagination.getPage());
        resource.setPerPage(pagination.getPerPage());
        return resource;
    }
}
