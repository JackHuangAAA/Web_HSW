package com.ethink.fps.api.action;

import com.ethink.fps.api.VO.MatchResult;
import com.ethink.fps.api.VO.RestResult;
import com.ethink.fps.api.action.common.Action;
import com.ethink.fps.api.action.common.RspCode;
import com.ethink.fps.domain.DO.Finger;
import com.ethink.fps.domain.service.IFingerPrintStore;
import com.ethink.fps.domain.service.IFingerPrinterMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Administrator on 2016/12/29.
 */
@RestController
@RequestMapping("/")
public class StoreAction extends Action {

    @Autowired
    private IFingerPrinterMatcher fingerPrinterMatcher;
    @Autowired
    private IFingerPrintStore fingerPrintStore;

    @RequestMapping(value = "/match", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public RestResult match(@RequestBody String base64Pic) {
        Finger finger = fingerPrinterMatcher.match(base64Pic);
        RestResult result = new RestResult();
        if (finger != null) {
            MatchResult matchResult = new MatchResult();
            matchResult.setId(finger.getId());
            matchResult.setTag(finger.getTag());
            result.setData(matchResult);
        } else {
            result.setCode(RspCode.NOT_MATCH);
            result.setMessage("not match");
        }
        return result;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public RestResult register(@RequestParam("tag") String tag, @RequestBody String base64Pic) {
        fingerPrintStore.addFinger(tag, base64Pic);
        RestResult result = new RestResult();
        return result;
    }

    @RequestMapping(value = "/unregister", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public RestResult unregister(@RequestParam(value = "tag", required = false) String tag, @RequestParam(value = "id", required = false) Integer id) {
        if (id != null) {
            fingerPrintStore.removeFinger(id);
        } else if (tag != null) {
            fingerPrintStore.removeFinger(tag);
        }
        RestResult result = new RestResult();
        return result;
    }
}
