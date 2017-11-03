package com.neotys.authentication.md5.hash;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MD5HashActionTest {
	@Test
	public void shouldReturnType() {
		final MD5HashAction action = new MD5HashAction();
		assertEquals("MD5Hash", action.getType());
	}

}
