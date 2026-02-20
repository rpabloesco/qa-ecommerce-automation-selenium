package com.raulescobar.tests.smoke;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.raulescobar.pages.LoginPom;

import com.raulescobar.tests.base.BaseTest;

public class SanityTest extends BaseTest {
    
    @Test(groups = {"smoke"})
    public void smoke_openLoginPage() {
        LoginPom login = new LoginPom(driver);
        login.getUrl(config.getEnv("baseUrl"));

        Assert.assertTrue(driver.getTitle().contains("Swag Labs"), "El título no contiene 'Swag Labs'");
    }
}
