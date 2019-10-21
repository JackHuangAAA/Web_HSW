package com.ethink.fps.domain.service;

import com.ethink.fps.domain.DO.Finger;

public interface IFingerPrinterMatcher {
    Finger match(String base64Picture);
    String getTemp(String base64Picture);
}
