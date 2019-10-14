package com.ethink.fps.api.action.common;

import com.ethink.fps.api.Vo.RestResult;
import com.ethink.fps.exception.ServerException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator on 2016/12/26.
 */
public class Action {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler
    public RestResult exp(HttpServletRequest request, Exception e) {
        logger.error("Action catch exception {}",e);
        RestResult result = new RestResult();
        if(e instanceof ServerException){
            result.setCode(((ServerException) e).getCode());
            result.setMessage(e.getMessage());
        }else if(e instanceof HttpMessageNotReadableException) {

            if(((HttpMessageNotReadableException) e).getRootCause() instanceof InvalidFormatException) {

            }else {

            }
        } else{

        }
        return result;
    }
}
