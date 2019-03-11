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

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import groovy.transform.CompileStatic
import org.lappsgrid.serialization.LifException

import static org.lappsgrid.discriminator.Discriminators.Uri;


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
@CompileStatic
@JsonPropertyOrder(['type', 'producer', 'url', 'tagSet', 'dependsOn'])
class Contains {

    @JsonIgnore
    String atType

    // TODO: 3/1/2018 find a way to programmatically read in these names from vocabulary
    static HashMap<String,String> tagsetKeys = [
            (Uri.POS)                 : "posTagSet",
            (Uri.NE)                  : "namedEntityCategorySet",
            (Uri.PHRASE_STRUCTURE)    : "categorySet",
            (Uri.DEPENDENCY_STRUCTURE): "dependencySet"
    ]

    @Delegate
    HashMap data

    Contains() {
        data = new HashMap()
    }

    Contains(Map map) {
        this()
        map.each { key, value ->
            put(key, value)
        }
    }

    Contains(Contains contains) {
        this()
        contains.data.each { key, value ->
            put(key, value)
        }
    }

    Contains(Object object) {
        Map map
        if (object instanceof Map) {
            map = (Map) object
        }
        else if (object instanceof Contains) {
            map = ((Contains) object).data
        }
        // TODO See issue #18. Should throw if map == null
        if (map) {
            map.each { key, value ->
                data[key] = value
            }
        }

    }
    @JsonProperty
    void setUrl(String url) {
        data.url = url
    }
    String getUrl() {
        return data.url
    }
    @JsonProperty
    void setProducer(String producer) {
        data.producer = producer
    }
    String getProducer() {
        return data.producer
    }
    @JsonProperty
    void setType(String type) {
        data.type = type
    }
    String getType() {
        return data.type

    }

    @JsonProperty
    void setTagSet(String value) throws LifException {

        if (tagsetKeys.containsKey(atType)) {
            String key = tagsetKeys[getAtType()]
            data[key] = value
        } else {
            throw new LifException("No tagset-like feature is defined for ${this.atType} ")
        }
    }

    String getTagSet() throws LifException {
        String atType = getAtType()
        if (tagsetKeys.containsKey(atType)) {
            String key = tagsetKeys[atType]
            Object data = data[key]
            return data
        } else {
            throw new LifException("No tagset-like feature is defined for ${this.atType} ")
        }
    }

    @JsonProperty
    void setDependsOn(List<Dependency> dependsOn) {
        data.dependsOn = dependsOn
    }

    List<Dependency> getDependsOn() {
        return (List<Dependency>) data.dependsOn
    }

    void dependency(String view, String type) {
        dependency(new Dependency(view, type))
    }

    void dependency(Dependency dependency) {
        List<Dependency> dependencies = getDependsOn()
        if (dependencies == null) {
            dependencies = []
            data.dependsOn = dependencies
        }
        dependencies.add(dependency)
    }
}
