package com.controller;

import com.entity.StaffEntity;
import com.model.PaginationModel;
import com.model.StaffModel;
import com.model.StaffResourceModel;
import com.resolver.anotation.Pagination;
import com.resolver.anotation.RequestPagingParam;
import com.service.StaffService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Map;
import java.util.NoSuchElementException;

import static com.utils.ValidatorUtils.*;

/**
 * Controller layer
 */
@RestController
@Validated
@RequestMapping(path = "/staffs")
public class StaffController {
    private final StaffService staffService;

    private static final Logger LOGGER = LoggerFactory.getLogger(StaffController.class);

    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    /**
     * Create new staff belong to a department
     *
     * @param requestModel
     * @return response entity contains created staff
     */
    @PostMapping(path = "",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<StaffModel> saveStaff(@Valid @RequestBody StaffModel requestModel) {

        StaffModel responseModel = staffService.createStaff(requestModel);
        return new ResponseEntity<>(responseModel, HttpStatus.OK);
    }


    /**
     * Update information of a staff
     *
     * @param staffId
     * @param staffModel
     * @return response entity for updated staff
     */
    @PutMapping(path = "/{staffId}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Object> updateStaff(@PathVariable("staffId") @Min(0) int staffId,
                                              @Valid @RequestBody StaffModel staffModel) {
        staffModel.setId(staffId);
        StaffModel responseModel = staffService.updateStaff(staffModel);
        return new ResponseEntity<>(responseModel, HttpStatus.OK);
    }

    /**
     * Delete a staff
     *
     * @param staffId
     * @return response entity for deleted staff
     */
    @DeleteMapping(path = "/{staffId}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Object> deleteStaff(@PathVariable @Min(0) int staffId) throws NoSuchElementException {
        StaffModel staffModel = staffService.deleteStaffById(staffId);
        return new ResponseEntity<>(staffModel, HttpStatus.OK);
    }


    /**
     * Find staff by id
     *
     * @param staffId
     * @return searched staff by id
     */
    @GetMapping(path = "/{staffId}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Object> findById(@Min(0) @PathVariable int staffId) throws NoSuchElementException {
        StaffModel model = staffService.findById(staffId);
        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    /**
     * Find all staff by first name or last name
     *
     * @param @StaffPagination
     * @param searchedValue
     * @return information of resource
     */
    @GetMapping(path = "",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Object> findByFirstnameOrLastname(
            @RequestParam(value = "searchedValue", required = false, defaultValue = "") String searchedValue,
            @RequestPagingParam PaginationModel pagination) {

        StaffResourceModel resource = staffService.findByLastnameOrFirstname(pagination, searchedValue);
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }


}
