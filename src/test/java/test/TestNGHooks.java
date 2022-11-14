package test;

import org.testng.annotations.*;

public class TestNGHooks {

    @Test
    public void doSth(){
        System.out.println("Testing!");
    }

    @BeforeSuite
    public void beforeSuite(){
        System.out.println("Before Suite!");
    }

    @BeforeTest
    public void beforeTest(){
        System.out.println("\tBefore Test!");
    }

    @BeforeClass
    public void beforeClass(){
        System.out.println("\t\tBefore Class!");
    }

    @BeforeMethod
    public void beforeMethod(){
        System.out.println("\t\t\tBefore Method!");
    }

    @AfterMethod
    public void afterMethod(){
        System.out.println("\t\t\tAfter Method!");
    }

    @AfterClass
    public void afterClass(){
        System.out.println("\t\tAfter Class!");
    }

    @AfterTest
    public void afterTest(){
        System.out.println("\tAfter Test!");
    }

    @AfterSuite
    public void afterSuite(){
        System.out.println("After Suite!");
    }
}
