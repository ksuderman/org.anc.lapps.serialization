/*
 * Copyright 2014 The Language Application Grid
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.lappsgrid.serialization.lif

import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.CompileStatic

/**
 * A JSON-LD <a href="http://www.w3.org/TR/json-ld/#dfn-value-object">value object</a>.
 * <p>
 * While a value object may contains other keys, for our purposes we only need
 * the <i>{@literal @}value</i> and <i>{@literal @}type</i> keys.
 * <p>
 * This class isn't currently used anywhere anymore.
 * @author Keith Suderman
 */
@CompileStatic
class ValueObject {
    @JsonProperty('@value')
    String value
    @JsonProperty('@type')
    String type

    public ValueObject() { }

    public ValueObject(String type, String value)
    {
        this.type = type;
        this.value = value;
    }

    public ValueObject(Map map) {
        if (map == null) {
            return
        }
        this.type = map['type']
        this.value = map['value']
    }
}
