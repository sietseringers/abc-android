/*
 * AbcTestListener.java
 * Copyright (C) 2015 Sietse Ringers
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301
 * USA
 */

package net.sietseringers.abc.android;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

import java.io.PrintStream;

public class AbcTestListener extends RunListener {
	private PrintStream out;

	private String separator = "----------------------------------------";

	public AbcTestListener(PrintStream out) {
		this.out = out;
	}

	/**
	 * Called before any tests have been run.
	 * */
	public void testRunStarted(Description description)	throws java.lang.Exception
	{
		out.println("Number of testcases to execute : " + description.testCount());
		out.println();
	}

	/**
	 *  Called when all tests have finished
	 * */
	public void testRunFinished(Result result) throws java.lang.Exception
	{
		out.println("Number of testcases successfully executed : " + result.getRunCount());
	}

	/**
	 *  Called when an atomic test is about to be started.
	 * */
	public void testStarted(Description description) throws java.lang.Exception
	{
		out.println("Starting execution of test case : "+ description.getMethodName());
		out.println(separator);
	}

	/**
	 *  Called when an atomic test has finished, whether the test succeeds or fails.
	 * */
	public void testFinished(Description description) throws java.lang.Exception
	{
		out.println(separator);
		out.println("Finished execution of test case : "+ description.getMethodName());
		out.println();
	}

	/**
	 *  Called when an atomic test fails.
	 * */
	public void testFailure(Failure failure) throws java.lang.Exception
	{
		out.println("!!! --> Execution of test case failed!");
		out.println("message: " + failure.getMessage());
		out.println("in test: " + failure.getDescription().getMethodName());
		out.println(failure.getTrace());
	}

	/**
	 *  Called when a test will not be run, generally because a test method is annotated with Ignore.
	 * */
	public void testIgnored(Description description) throws java.lang.Exception
	{
		out.println("Execution of test case ignored : "+ description.getMethodName());
	}
}
