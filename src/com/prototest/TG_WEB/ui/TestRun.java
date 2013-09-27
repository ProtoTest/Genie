/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prototest.TG_WEB.ui;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class TestRun {
   public static void main(String[] args) {
      Result result = JUnitCore.runClasses(TestCase.class);
      for (Failure failure : result.getFailures()) {
         System.out.println(failure.toString());
      }
      System.out.println(result.wasSuccessful());
   }
}  	