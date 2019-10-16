package com.ethink.fps.domain.service;

import com.ethink.fps.domain.DO.Finger;
import com.machinezoo.sourceafis.FingerprintMatcher;
import com.machinezoo.sourceafis.FingerprintTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Base64;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class FingerPrinterMatcherImpl implements IFingerPrinterMatcher {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private double threshold = 40;
    private int dpi = 500;

    @Autowired
    private IFingerPrintStore store;

    @Override
    public Finger match(String base64Picture) {
        AtomicReference<Finger> result = new AtomicReference<>(null);
        byte[] image = Base64.getDecoder().decode(base64Picture);
        FingerprintTemplate src = new FingerprintTemplate().dpi(dpi).create(image);
        FingerprintMatcher matcher = new FingerprintMatcher().index(src);
        store.forEach((Finger finger) -> {
            FingerprintTemplate target = new FingerprintTemplate().deserialize(finger.getTemp());
            double score = matcher.match(target);
            logger.info("tag:{},score:{}",finger.getTag(),score);
            if (score > threshold) {
                result.set(finger);
                return true;
            }
            return false;
        });
        return result.get();
    }

    @Override
    public String getTemp(String base64Picture) {
        byte[] image = Base64.getDecoder().decode(base64Picture);
        FingerprintTemplate temp = new FingerprintTemplate().dpi(dpi).create(image);
        return temp.serialize();
    }
}
