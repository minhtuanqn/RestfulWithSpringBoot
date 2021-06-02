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

import javax.validation.Valid;
import java.util.HashMap;
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

//    /**
//     * Handle exception for parameter
//     *
//     * @param ex
//     * @return
//     */
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler({ConstraintViolationException.class})
//    public Map<String, String> handleValidationExceptions(
//            ConstraintViolationException ex) {
//        Map<String, String> errors = new HashMap<>();
//        Iterator<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations().iterator();
//        while (constraintViolations.hasNext()) {
//            ConstraintViolation constraintViolation = constraintViolations.next();
//            errors.put(constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage());
//        }
//        return errors;
//    }

    private Map<String, String> checkValidId(String id) {
        Map<String, String> invalidId = new HashMap<>();
        Integer defaultId = checkNotNegativeIntNumber(id, invalidId, "id");
        if(defaultId == null) {
            return invalidId;
        }
        return null;
    }

    /**
     * Create staff
     *
     * @param staffModel
     * @return response entity contains saved staff
     */
    @PostMapping(path = "",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<StaffModel> saveStaff(@Valid @RequestBody StaffModel staffModel) {
        StaffModel responseModel = defaultService.createStaff(staffModel);
        return new ResponseEntity<StaffModel>(responseModel, HttpStatus.OK);
    }

    /**
     * Update staff by id
     *
     * @param staffModel
     * @param id of updated staff
     * @return response entity contains updated staff
     */
    @PutMapping(path = "/{id}",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity updateStaff(@PathVariable String id,
                                      @Valid @RequestBody StaffModel staffModel) {
        Map<String, String> invalidId = checkValidId(id);
        if(invalidId != null && invalidId.size() > 0) {
            return new ResponseEntity(invalidId, HttpStatus.BAD_REQUEST);
        }
        StaffModel responseModel = defaultService.updateStaff(staffModel);
        if (responseModel != null) {
            return new ResponseEntity<StaffModel>(responseModel, HttpStatus.OK);
        } else {
            return new ResponseEntity<StaffModel>(new StaffModel(), HttpStatus.NOT_FOUND);
        }
    }


    /**
     * Delete staff by id
     *
     * @param id
     * @return status
     */
    @DeleteMapping(path = "/{id}",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity deleteStaff(@PathVariable String id) {
        Map<String, String> invalidId = checkValidId(id);
        if(invalidId != null && invalidId.size() > 0) {
            return new ResponseEntity(invalidId, HttpStatus.BAD_REQUEST);
        }
        StaffModel staffModel = defaultService.deleteStaffById(Integer.parseInt(id));
        if (staffModel != null) {
            return new ResponseEntity(staffModel, HttpStatus.OK);
        } else {
            return new ResponseEntity(new StaffModel(), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Find staff by id
     *
     * @param id
     * @return Staff object
     */
    @GetMapping(path = "/{id}",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<StaffModel> findById(@PathVariable String id) {
        Map<String, String> invalidId = checkValidId(id);
        if(invalidId != null && invalidId.size() > 0) {
            return new ResponseEntity(invalidId, HttpStatus.BAD_REQUEST);
        }
        StaffModel model = defaultService.findById(Integer.parseInt(id));
        if(model == null) {
            return new ResponseEntity<>(new StaffModel(), HttpStatus.NOT_FOUND);
        }
        else
        {
            return new ResponseEntity<StaffModel>(model, HttpStatus.OK);
        }

    }

    /**
     * Find all staff by first name or last name
     * @param page
     * @param perPage
     * @param sortBy
     * @param searchedValue
     * @return information of resource
     */
    @GetMapping(path = "",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<StaffResource> findByFirstnameOrLastname(
            @RequestParam(value = "page", required = false) String page,
            @RequestParam(value = "perPage", required = false) String perPage,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "searchedValue", required = false) String searchedValue,
            @RequestParam(value = "sortType", required = false) String sortType) {
        int defaultPage = 0;
        int defaultPerPage = 10;
        String defaultSortBy = "firstName";
        String defaultSearchedValue = "";
        String defaultSortType = "asc";
        Map<String, String> bindingErr = new HashMap<>();
        if(page != null) {
            Integer tmpPage = checkNotNegativeIntNumber(page, bindingErr, "page");
            if(tmpPage != null) {
                defaultPage = tmpPage;
            }
        }
        if(perPage != null) {
            Integer tmpPerPage = checkNotNegativeIntNumber(perPage, bindingErr, "perPage");
            if(tmpPerPage != null) {
                defaultPerPage = tmpPerPage;
            }
        }
        if(bindingErr.size() > 0) {
            return new ResponseEntity(bindingErr, HttpStatus.BAD_REQUEST);
        }
        else if(!checkGreaterThanZero(defaultPerPage, bindingErr, "perPage")) {
            return new ResponseEntity(bindingErr, HttpStatus.BAD_REQUEST);
        }

        if(sortBy != null) {
            Map<String, String> existError = checkExistFieldOfClass(StaffEntity.class, sortBy, "sortBy");
            if(existError != null && existError.size() > 0) {
                return new ResponseEntity(existError, HttpStatus.NOT_FOUND);
            }
            defaultSortBy = sortBy;
        }
        if(searchedValue != null) {
            defaultSearchedValue = searchedValue;
        }
        if (sortType != null) {
            defaultSortType = sortType;
        }
        StaffResource resource = defaultService.findByLastnameOrFirstname(defaultPage, defaultPerPage,
                defaultSortBy, defaultSearchedValue, defaultSortType);
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }


}
