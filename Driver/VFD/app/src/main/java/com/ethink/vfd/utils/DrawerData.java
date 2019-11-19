package com.ethink.vfd.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

public enum DrawerData {
    Drawer1("1#1", 1),
    Drawer2("2#1", 2),
    Drawer3("1#2", 3),
    Drawer4("2#2", 4),
    Drawer5("1#3", 5),
    Drawer6("2#3", 6),
    Drawer7("1#4", 7),
    Drawer8("2#4", 8),
    Drawer9("1#5", 9),
    Drawer10("2#5", 10),
    Drawer11("1#6", 11),
    Drawer12("2#6", 12),
    Drawer13("1#7", 13),
    Drawer14("2#7", 14),
    Drawer15("1#8", 15),
    Drawer16("2#8", 16);


    private String point;
    private int location;

    DrawerData(String point, int location) {
        this.point = point;
        this.location = location;
    }

    public static int getLocationByPoint(String point) {
        for (DrawerData dd : DrawerData.values()) {
            if (dd.point.equals(point)) {
                return dd.location;
            }

        }
        return -1;
    }

    protected static Logger logger = LoggerFactory.getLogger("DrawerData");

    /**
     * @param num 格式为：1#1,2#1,
     */
    public static Set<Integer> getDrawerList(String num) {
        Set<Integer> set = new HashSet<>();
        String[] strings = num.split(",");
        for (String string : strings) {
            set.add(getLocationByPoint(string));
        }
        return set;

    }


}
