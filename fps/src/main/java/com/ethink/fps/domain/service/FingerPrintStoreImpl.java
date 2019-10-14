package com.ethink.fps.domain.service;

import com.ethink.fps.domain.Do.Finger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class FingerPrintStoreImpl implements IFingerPrintStore {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @PostConstruct
    public void onInit(){
        jdbcTemplate.execute("CREATE table IF NOT EXISTS finger(id INTEGER PRIMARY KEY, templete TEXT)");
    }


    @Override
    public void addFinger(String templete) {

    }

    @Override
    public void removeFinger(int id) {

    }

    @Override
    public List<Finger> queryFingerList() {
        return null;
    }
}
