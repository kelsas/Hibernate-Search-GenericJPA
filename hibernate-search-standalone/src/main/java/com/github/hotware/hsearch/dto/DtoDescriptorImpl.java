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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.github.hotware.hsearch.dto.DtoDescriptor.DtoDescription.FieldDescription;
import com.github.hotware.hsearch.dto.annotations.DtoField;
import com.github.hotware.hsearch.dto.annotations.DtoFields;
import com.github.hotware.hsearch.dto.annotations.DtoOverEntity;

public class DtoDescriptorImpl implements DtoDescriptor {

	@Override
	public DtoDescription getDtoDescription(Class<?> clazz) {
		final Map<String, Set<FieldDescription>> fieldDescriptionsForProfile = new HashMap<>();
		DtoOverEntity[] dtoOverEntity = clazz
				.getAnnotationsByType(DtoOverEntity.class);
		if (dtoOverEntity.length != 1) {
			throw new IllegalArgumentException(
					"clazz must specify exactly one "
							+ "DtoOverEntity annotation at a class level");
		}
		java.lang.reflect.Field[] declared = clazz.getDeclaredFields();
		Arrays.asList(declared)
				.forEach((field) -> {
					// should be accessible :)
						field.setAccessible(true);
						List<DtoField> annotations = new ArrayList<>();
						{
							DtoFields dtoFields = field
									.getAnnotation(DtoFields.class);
							if (dtoFields != null) {
								annotations.addAll(Arrays.asList(dtoFields
										.value()));
							} else {
								DtoField dtoField = field
										.getAnnotation(DtoField.class);
								if (dtoField != null) {
									annotations.add(dtoField);
								}
							}
						}
						annotations.forEach((annotation) -> {
							String profileName = annotation.profileName();
							String fieldName = annotation.fieldName();
							if (fieldName
									.equals(DtoDescription.DEFAULT_FIELD_NAME)) {
								// if we want to support
								// hierarchies at any time
								// in the future we have to
								// change this!
								fieldName = field.getName();
							}
							Set<FieldDescription> fieldDescriptions = fieldDescriptionsForProfile
									.computeIfAbsent(profileName, (key) -> {
										return new HashSet<>();
									});
							FieldDescription fieldDesc = new FieldDescription(
									fieldName, field);
							if (fieldDescriptions.contains(fieldDesc)) {
								throw new IllegalArgumentException(
										"profile "
												+ profileName
												+ " already has a field to project from for "
												+ field);
							}
							fieldDescriptions.add(fieldDesc);
						});
					});
		if (fieldDescriptionsForProfile.isEmpty()) {
			throw new IllegalArgumentException(
					"no DtoField(s) found! The passed class is no annotated DTO");
		}
		return new DtoDescription(clazz, dtoOverEntity[0].entityClass(),
				fieldDescriptionsForProfile);
	}

}
