package com.controller;

import com.model.StaffModel;
import com.service.DefaultService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller layer
 */
@RestController
@RequestMapping(path = "/staff")
public class DefaultController {
    private DefaultService defaultService;
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultController.class);

    public DefaultController(DefaultService defaultService) {
        this.defaultService = defaultService;
    }

    /**
     * Handle exception by hibernate validator
     * @param ex
     * @return  map contains errors
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
     * Restfull api for create staff
     * @param staffModel
     * @return response entity contains saved staff
     */
    @PostMapping(path = "",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<StaffModel> saveStaff(@Valid @RequestBody StaffModel staffModel) {
        defaultService.createStaff(staffModel);
        return new ResponseEntity<StaffModel>(staffModel, HttpStatus.OK);
    }

    /**
     * Restfull api for update existed staff
     * @param staffModel
     * @param id of updated staff
     * @return response entity contains updated staff
     */
    @PutMapping(path = "/{id}",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity updateStaff(@Valid @RequestBody StaffModel staffModel,
                                      @NotNull @Digits(integer=10, fraction=0) @Positive @PathVariable Integer id) {
        staffModel.setId(id);
        boolean isUpdated = defaultService.updateStaff(staffModel);
        if(isUpdated) {
            return new ResponseEntity<StaffModel>(staffModel, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<Object>(null, HttpStatus.NOT_FOUND);
        }
    }

//    @GetMapping(path = "/{id}",
//            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
//            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
//    public ResponseEntity<StaffModel> findById(@PathVariable Integer id) {
//        StaffModel model = defaultService.findById(id);
//        return new ResponseEntity<StaffModel>(model, HttpStatus.OK);
//    }



//    @DeleteMapping(path = "/{id}",
//            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
//            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
//    public ResponseEntity deleteStaff(@PathVariable Integer id) {
//        defaultService.deleteStaffById(id);
//        return new ResponseEntity("Delete success", HttpStatus.ACCEPTED);
//    }
//
//    @GetMapping(path = "/all",
//            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
//            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
//    public ResponseEntity<List<StaffModel>> findAll() {
//        List<StaffModel> modelList = defaultService.findAll();
//        return new ResponseEntity<>(modelList, HttpStatus.OK);
//    }
//
//    @GetMapping(path = "/first-name",
//            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
//            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
//    public ResponseEntity<List<StaffModel>> findByFirstName(@RequestParam String firstName) {
//        List<StaffModel> modelList = defaultService.findByFirstName(firstName);
//        return new ResponseEntity<>(modelList, HttpStatus.OK);
//    }

//    @GetMapping(path = "{lastname}/find-by-lastname",
//    consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
//    produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
//    public ResponseEntity<List<StaffModel>> findByLastName(@PathVariable String lastName) {
//
//    }

}
