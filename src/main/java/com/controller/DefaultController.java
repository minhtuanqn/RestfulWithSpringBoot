package com.controller;

import com.entity.StaffEntity;
import com.model.StaffModel;
import com.model.StaffResource;
import com.service.DefaultService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.utils.ValidatorUtils.*;

/**
 * Controller layer
 */
@RestController
@Validated
@RequestMapping(path = "/staffs")
public class DefaultController {
    private DefaultService defaultService;
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultController.class);

    public DefaultController(DefaultService defaultService) {
        this.defaultService = defaultService;
    }

    /**
     * Handle exception by hibernate validator
     *
     * @param ex
     * @return map contains errors
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    /**
     * Handle exception for parameter
     *
     * @param ex
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ConstraintViolationException.class})
    public Map<String, String> handleValidationExceptions(
            ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        Iterator<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations().iterator();
        while (constraintViolations.hasNext()) {
            ConstraintViolation<?> constraintViolation = constraintViolations.next();
            errors.put(constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage());
        }
        return errors;
    }


    /**
     * Create staff
     *
     * @param staffModel
     * @return response entity contains saved staff
     */
    @PostMapping(path = "",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<StaffModel> saveStaff(@Valid @RequestBody StaffModel staffModel) {
        StaffModel responseModel = defaultService.createStaff(staffModel);
        return new ResponseEntity<>(responseModel, HttpStatus.OK);
    }

    /**
     * Update staff by id
     *
     * @param staffModel
     * @param id         of updated staff
     * @return response entity contains updated staff
     */
    @PutMapping(path = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Object> updateStaff(@PathVariable @Min(0) int id,
                                      @Valid @RequestBody StaffModel staffModel) {
        StaffModel responseModel = defaultService.updateStaff(staffModel);
        if (responseModel == null) {
            return new ResponseEntity<>(new StaffModel(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(responseModel, HttpStatus.OK);
    }


    /**
     * Delete staff by id
     *
     * @param id
     * @return status
     */
    @DeleteMapping(path = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Object> deleteStaff(@PathVariable @Min(0) int id) {
        StaffModel staffModel = defaultService.deleteStaffById(id);
        if (staffModel == null) {
            return new ResponseEntity<>(new StaffModel(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(staffModel, HttpStatus.OK);
    }

    /**
     * Find staff by id
     *
     * @param id
     * @return Staff object
     */
    @GetMapping(path = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Object> findById(@PathVariable @Min(0) int id) {
        StaffModel model = defaultService.findById(id);
        if (model == null) {
            return new ResponseEntity<>(new StaffModel(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(model, HttpStatus.OK);

    }

    /**
     * Find all staff by first name or last name
     *
     * @param page
     * @param perPage
     * @param sortBy
     * @param searchedValue
     * @return information of resource
     */
    @GetMapping(path = "",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Object> findByFirstnameOrLastname(
            @Valid @RequestParam(value = "page", required = false, defaultValue = "0") @Min(0) Integer page,
            @Valid @RequestParam(value = "perPage", required = false, defaultValue = "10") @Min(1) Integer perPage,
            @RequestParam(value = "sortBy", required = false, defaultValue = "firstName") String sortBy,
            @RequestParam(value = "searchedValue", required = false, defaultValue = "") String searchedValue,
            @RequestParam(value = "sortType", required = false, defaultValue = "asc") String sortType) {
        Map<String, String> existError = checkExistFieldOfClass(StaffEntity.class, sortBy, "sortBy");
        if (existError != null && existError.size() > 0) {
            return new ResponseEntity<>(existError, HttpStatus.NOT_FOUND);
        }
        StaffResource resource = defaultService.findByLastnameOrFirstname(page, perPage, sortBy, searchedValue, sortType);
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }


}
