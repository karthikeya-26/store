package org.karthik.store.services;
import com.adventnet.mfw.*;
import com.adventnet.mfw.bean.BeanUtil;
import com.adventnet.persistence.Persistence;

public class Test {
    public static void main(String[] args) throws Exception {
        Persistence persistence = (Persistence) BeanUtil.lookup("Persistence");
    }
}