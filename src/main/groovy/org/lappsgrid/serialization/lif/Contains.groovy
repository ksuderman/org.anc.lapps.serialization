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

import com.fasterxml.jackson.annotation.JsonAnyGetter
import com.fasterxml.jackson.annotation.JsonAnySetter
import com.fasterxml.jackson.annotation.JsonIgnore

/**
 * Holds information for the 'contains' sections of a {@link View}'s
 * metadata section.
 * <p/>
 * The <em>contains</em> metadata allows pipelines (planners or composers) to determine the
 * contents of a ProcessingStep without having to traverse the contents of the
 * <em>annotations</em> list.
 *
 * @author Keith Suderman
 */
class Contains {
    /**
     * The URL of the processor that produced the annotations.
     */
    String url;

    /**
     * The name of the processors that produced the annotations.  For Java
     * processors this will be the fully qualified class name of the processor
     * including version information.
     */
    String producer;

    /**
     * The annotation type.
     */
    String type;

    @JsonIgnore
    Map additionalMetadata = [:];

    public Contains() { }

    public Contains(Contains contains) {
        this.url = contains.url
        this.producer = contains.producer
        this.type = contains.type
        this.additionalMetadata = contains.additionalMetadata
    }

    public Contains(Map map) {
        if (map == null) {
            return
        }
        this.url = map.remove('url')
        this.producer = map.remove('producer')
        this.type = map.remove('type')
        this.additionalMetadata = map
    }

    @JsonAnyGetter
    public Map getAdditionalProperties() {
        return additionalMetadata
    }

    @JsonAnySetter
    public addMetadata(String key, String value) {
        additionalMetadata[key] = value
    }

    public getMetadata(String key) {
        if (key == "url") {
            return this.url
        }
        else if (key == "producer") {
            return this.producer
        }
        else if (key == "type") {
            return this.type
        }
        else if (this.additionalMetadata.containsKey(key)) {
            return this.additionalMetadata.get(key)
        } else {
            return null
        }
    }

}
