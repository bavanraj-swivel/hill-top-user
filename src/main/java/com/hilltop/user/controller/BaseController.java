package com.hilltop.user.controller;

import com.hilltop.user.domain.response.ResponseDto;
import com.hilltop.user.domain.response.ResponseWrapper;
import com.hilltop.user.enumeration.ErrorMessage;
import com.hilltop.user.enumeration.SuccessMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
 * Base controller
 */
@CrossOrigin
public class BaseController {

    /**
     * This method is used to get success response.
     *
     * @param successMessage success message
     * @param responseDto    response data
     * @param httpStatus     http status
     * @return success response.
     */
    protected ResponseEntity<ResponseWrapper> getSuccessResponse(SuccessMessage successMessage,
                                                                 ResponseDto responseDto, HttpStatus httpStatus) {
        ResponseWrapper responseWrapper = new ResponseWrapper(successMessage.getMessage(), responseDto);
        return new ResponseEntity<>(responseWrapper, httpStatus);
    }

    /**
     * This method is used to send bad request error response.
     *
     * @param errorMessage error message
     * @return bad request error response.
     */
    protected ResponseEntity<ResponseWrapper> getBadRequestErrorResponse(ErrorMessage errorMessage) {
        ResponseWrapper responseWrapper = new ResponseWrapper(errorMessage.getMessage());
        return new ResponseEntity<>(responseWrapper, HttpStatus.BAD_REQUEST);
    }

    /**
     * This method sends internal server error response.
     *
     * @return internal server error response.
     */
    protected ResponseEntity<ResponseWrapper> getInternalServerError() {
        ResponseWrapper responseWrapper = new ResponseWrapper(ErrorMessage.INTERNAL_SERVER_ERROR.getMessage());
        return new ResponseEntity<>(responseWrapper, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
