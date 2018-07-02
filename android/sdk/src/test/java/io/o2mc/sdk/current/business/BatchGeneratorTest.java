package io.o2mc.sdk.current.business;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.o2mc.sdk.current.O2MC;

import static org.junit.Assert.*;

public class BatchGeneratorTest {

    private O2MC o2mc;
    private BatchGenerator batchGenerator;

    @Before
    public void setUp() throws Exception {
//        o2mc = new O2MC(null, null);
        o2mc = new O2MC(null, "http://10.0.2.2:5000/events");
        batchGenerator = new BatchGenerator();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void firstRun() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void generateBatch() {
        assertEquals(4, 2 + 2);
    }
}