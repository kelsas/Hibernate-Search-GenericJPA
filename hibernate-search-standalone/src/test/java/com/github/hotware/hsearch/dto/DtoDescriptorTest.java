/*
 * Copyright 2015 Martin Braun
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.hotware.hsearch.dto;

import com.github.hotware.hsearch.dto.DtoDescriptor;
import com.github.hotware.hsearch.dto.DtoDescriptorImpl;
import com.github.hotware.hsearch.dto.DtoDescriptor.DtoDescription;
import com.github.hotware.hsearch.dto.annotations.DtoField;
import com.github.hotware.hsearch.dto.annotations.DtoOverEntity;

import junit.framework.TestCase;

public class DtoDescriptorTest extends TestCase {

	// the value of entityClass isn't that important in this test
	// but we want to check if it's set properly in the resulting
	// DtoDescription
	@DtoOverEntity(entityClass = B.class)
	public static class A {

		@DtoField(fieldName = "toastFieldName", profileName = "toast")
		@DtoField
		String fieldOne;

		@DtoField
		String fieldTwo;

	}

	public static class B {

	}

	@DtoOverEntity(entityClass = C.class)
	public static class C {

		// this should be the reason for an exception
		// when used with the Descriptor
		@DtoField
		@DtoField
		String field;

	}

	public void testDescriptor() {
		DtoDescriptor descriptor = new DtoDescriptorImpl();
		DtoDescription description = descriptor.getDtoDescription(A.class);
		assertEquals(A.class, description.getDtoClass());
		assertEquals(B.class, description.getEntityClass());
		assertEquals(1, description.getFieldDescriptionsForProfile("toast")
				.size());
		assertEquals("toastFieldName", description
				.getFieldDescriptionsForProfile("toast").iterator().next()
				.getFieldName());
		assertEquals(
				2,
				description.getFieldDescriptionsForProfile(
						DtoDescription.DEFAULT_PROFILE).size());

		int found = 0;
		for (DtoDescription.FieldDescription fDesc : description
				.getFieldDescriptionsForProfile(DtoDescription.DEFAULT_PROFILE)) {
			if ("fieldOne".equals(fDesc.getFieldName())) {
				++found;
			} else if ("fieldTwo".equals(fDesc.getFieldName())) {
				++found;
			}
		}
		if (found != 2) {
			fail("the default profile for " + A.class
					+ " should have 2 different FieldDescriptions");
		}

		try {
			descriptor.getDtoDescription(C.class);
			fail("invalid description with two fieldnames annotated to one field in the same profile"
					+ " should yield an exception");
		} catch (IllegalArgumentException e) {

		}
	}
}
