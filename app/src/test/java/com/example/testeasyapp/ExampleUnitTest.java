package com.example.testeasyapp;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    private  MainActivity mainActivity;
    private DBHandler dbHandler;

    @Before
    public  void setUp(){
        mainActivity = new MainActivity();
    }

    @Test
    public void remain_isCorrect(){
        double result = mainActivity.getRemain(1000,500);
        assertEquals(500.00,result,0.001);
    }
}