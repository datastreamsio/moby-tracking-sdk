package io.o2mc.sdk.current.business;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UtilTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void generateTimestamp() {
    }

    @Test
    public void isValidEndpoint() {
        // List of valid endpoints
        List<String> valid = new ArrayList<>();
        valid.add("http://google.com"); // web endpoint
        valid.add("http://www.google.com"); // web endpoint
        valid.add("http://test.google.com"); // web endpoint
        valid.add("http://test.google.com:5000"); // web endpoint
        valid.add("http://test.google.com:5000/events"); // web endpoint
        valid.add("http://test.google.com:5000/events?key=value"); // web endpoint
        valid.add("http://127.0.0.1"); // local endpoint
        valid.add("http://192.168.1.1:5000/"); // local endpoint
        valid.add("http://10.0.2.2:5000/events"); // local endpoint

        for (String s : valid) {
            assertTrue(String.format("Endpoint '%s' should be valid. It's considered invalid.", s), Util.isValidEndpoint(s)); // should be true
        }

        // List of invalid endpoints -- mistyped the valid ones on deliberately
        List<String> invalid = new ArrayList<>();
        invalid.add("http://googlecom"); // web endpoint
        invalid.add("http:/www.google.com"); // web endpoint
        invalid.add("htp://test.google.com:5000"); // web endpoint
        invalid.add("http:test.google.com:5000/events"); // web endpoint
        invalid.add("http://testgooglecom:5000/events?key=value"); // web endpoint
        invalid.add("http://127.0.1"); // local endpoint
        invalid.add("http://.168.1.1:5000/"); // local endpoint
        invalid.add("http//0.0.2.2:5000/events"); // local endpoint

        for (String s : invalid) {
            assertFalse(String.format("Endpoint '%s' should be invalid. It's considered valid.", s), Util.isValidEndpoint(s)); // should be false
        }
    }

    @Test
    public void isValidDispatchInterval() {
        // Test valid dispatch intervals (anywhere between 1 second and 1 hour)
        int second = 1;
        assertTrue(Util.isValidDispatchInterval(second));

        int minute = 60;
        assertTrue(Util.isValidDispatchInterval(minute));

        int fiveMinutes = 60 * 5;
        assertTrue(Util.isValidDispatchInterval(fiveMinutes));

        int fiftyFiveMinutes = 60 * 55;
        assertTrue(Util.isValidDispatchInterval(fiftyFiveMinutes));

        // Test invalid dispatch intervals (anywhere above 1 hour and below 1 second)
        int hourAndSecond = 60 * 60 + 1;
        assertFalse(Util.isValidDispatchInterval(hourAndSecond));

        int twoHours = 60 * 60 * 2;
        assertFalse(Util.isValidDispatchInterval(twoHours));

        int negativeNumber = -10;
        assertFalse(Util.isValidDispatchInterval(negativeNumber));
    }
}