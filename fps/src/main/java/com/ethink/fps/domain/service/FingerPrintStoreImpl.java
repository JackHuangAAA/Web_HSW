package com.ethink.fps.domain.service;

import com.ethink.fps.domain.DO.Finger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

@Service
public class FingerPrintStoreImpl implements IFingerPrintStore {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private IFingerPrinterMatcher matcher;

    @PostConstruct
    public void onInit() {
        jdbcTemplate.execute("CREATE table IF NOT EXISTS finger(id INTEGER PRIMARY KEY AUTOINCREMENT,tag TEXT, temp TEXT)");
    }

    @Override
    public void addFinger(String tag, String base64Picture) {
        String temp = matcher.getTemp(base64Picture);
        jdbcTemplate.update("insert into finger(tag,temp) values(?,?)", tag, temp);
    }

    @Override
    public void removeFinger(int id) {
        jdbcTemplate.update("delete from finger where id = ?", id);
    }

    @Override
    public void removeFinger(String tag) {
        jdbcTemplate.update("delete from finger where tag = ?", tag);
    }

    @Override
    public void forEach(Predicate<Finger> func) {
        List<Finger> list = jdbcTemplate.queryForList("select * from finger", Finger.class);
        Iterator<Finger> it = list.iterator();
        while (it.hasNext()) {
            Finger finger = it.next();
            if (func.test(finger)) {
                break;
            }
        }
    }
}
