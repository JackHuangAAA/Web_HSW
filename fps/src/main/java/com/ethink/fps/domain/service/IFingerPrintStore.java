package com.ethink.fps.domain.service;

import com.ethink.fps.domain.DO.Finger;
import java.util.function.Predicate;

public interface IFingerPrintStore {
    int countByTag(String tag);
    int addFinger(String tag,String base64Picture);
    void removeFinger(int id);
    void removeFinger(String tag);
    void forEach(Predicate<Finger> func);
}
