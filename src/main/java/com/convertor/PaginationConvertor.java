package com.convertor;

import com.customexception.NotFoundFieldOfClassException;
import com.entity.StaffEntity;
import com.model.PaginationModel;
import com.model.ResourceModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import static com.utils.ValidatorUtils.checkExistFieldOfClass;

/**
 * Class for support pagination
 * @param <M> Type of model
 * @param <E> Type of entity
 */
public class PaginationConvertor<M, E> {

    /**
     * Create pageable for sort
     * @param pagination
     * @param defaultSortBy
     * @return
     */
    public Pageable covertToPageable(PaginationModel pagination, String defaultSortBy, Class<E> classType) {

        // Define sort by field for paging
        String sortBy = defaultSortBy;
        if (pagination.getSortBy() != null) {
            if (!checkExistFieldOfClass(classType, pagination.getSortBy())) {
                throw new NotFoundFieldOfClassException("Can not define sortBy");
            }
            sortBy = pagination.getSortBy();
        }

        //Build Pageable
        Pageable pageable;
        if (pagination.getSortType() != null && pagination.getSortType().equals("dsc")) {
            pageable = PageRequest.of(pagination.getPage(), pagination.getPerPage(), Sort.by(sortBy).descending());
        } else {
            pageable = PageRequest.of(pagination.getPage(), pagination.getPerPage(), Sort.by(sortBy).ascending());
        }
        return pageable;
    }

    /**
     * Build pagination
     * @param pagination
     * @param page
     * @param resource
     * @return
     */
    public ResourceModel<M> buildPagination(PaginationModel pagination, Page<E> page, ResourceModel<M> resource) {
        resource.setTotalPage(page.getTotalPages());
        resource.setTotal((int) page.getTotalElements());
        resource.setPage(pagination.getPage());
        resource.setPerPage(pagination.getPerPage());
        return resource;
    }
}
