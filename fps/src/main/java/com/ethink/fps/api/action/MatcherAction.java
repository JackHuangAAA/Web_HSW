package com.ethink.fps.api.action;

import com.ethink.fps.api.action.common.Action;
import com.ethink.fps.api.Vo.MatchRequest;
import com.ethink.fps.api.Vo.RestResult;
import com.ethink.fps.domain.service.IFingerPrinterMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Administrator on 2016/12/29.
 */
@RestController
@RequestMapping("/matcher")
public class MatcherAction extends Action {

    @Autowired
    private IFingerPrinterMatcher fingerPrinterMatcher;

    @RequestMapping(value = "/macthOne", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public RestResult Match(@RequestBody MatchRequest request) {
        RestResult result = new RestResult();
        result.setData("");
        return result;
    }
}
