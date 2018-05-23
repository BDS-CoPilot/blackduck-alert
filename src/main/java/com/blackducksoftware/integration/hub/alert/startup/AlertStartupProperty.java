/**
 * hub-alert
 *
 * Copyright (C) 2018 Black Duck Software, Inc.
 * http://www.blackducksoftware.com/
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.blackducksoftware.integration.hub.alert.startup;

public class AlertStartupProperty {
    private Class<?> propertyClass;
    private String propertyKey;
    private String fieldName;

    public AlertStartupProperty() {
    }

    public AlertStartupProperty(final Class<?> propertyClass, final String propertyKey, final String fieldName) {
        this.propertyClass = propertyClass;
        this.propertyKey = propertyKey;
        this.fieldName = fieldName;
    }

    public Class<?> getPropertyClass() {
        return propertyClass;
    }

    public void setPropertyClass(final Class<?> propertyClass) {
        this.propertyClass = propertyClass;
    }

    public String getPropertyKey() {
        return propertyKey;
    }

    public void setPropertyKey(final String propertyKey) {
        this.propertyKey = propertyKey;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(final String fieldName) {
        this.fieldName = fieldName;
    }

}
