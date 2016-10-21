package test.uk.ac.kent.dover;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import test.uk.ac.kent.dover.fastGraph.*;

public class TestRunner {
	public static void main(String[] args) {
		Result result = JUnitCore.runClasses(FastGraphTest.class);
		for (Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}
		System.out.println(result.wasSuccessful());

		result = JUnitCore.runClasses(ExactIsomorphismTest.class);
		for (Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}
		System.out.println(result.wasSuccessful());

   }
}