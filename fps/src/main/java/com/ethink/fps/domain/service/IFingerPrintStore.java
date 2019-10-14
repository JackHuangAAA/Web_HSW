package com.ethink.fps.domain.service;

import com.ethink.fps.domain.Do.Finger;

import java.util.List;

public interface IFingerPrintStore {

    void addFinger(String templete);
    void removeFinger(int id);
    List<Finger> queryFingerList(int start,int limit);
}
