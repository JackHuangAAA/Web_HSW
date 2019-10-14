package com.ethink.fps.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FingerPrinterMatcherImpl implements IFingerPrinterMatcher{

    @Autowired
    private IFingerPrintStore store;
}
