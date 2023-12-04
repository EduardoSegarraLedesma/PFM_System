package com.pfm.pfm_system;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class PfmSystemApplicationTests {

    @Autowired
    private PfmSystemApplication application;

    @Test
    void contextLoads() {
        assertNotNull(application);
    }

}
