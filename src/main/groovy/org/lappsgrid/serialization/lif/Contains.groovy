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
import com.fasterxml.jackson.annotation.JsonPropertyOrder

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
@JsonPropertyOrder(['type', 'producer', 'url', 'tagSet', 'dependsOn'])
class Contains {
    @Delegate
    HashMap data = new HashMap()

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
    void setTagSet(String tagSet) {
        data.tagSet = tagSet
    }
    String getTagSet() {
        return tagSet
    }
//    @JsonProperty
//    void setDependsOn(List<DependsOn> dependsOn) {
//        data.dependsOn = dependsOn
//    }
//    List<DependsOn> getDependsOn() {
//        return data.dependsOn
//    }
//    void dependOn(String view, String type) {
//        List<DependsOn> list = data.dependsOn
//        if (list == null) {
//            list = new ArrayList<DependsOn>()
//            data.dependsOn = list
//        }
//        list << new DependsOn(view, [ type ])
//    }
//    void dependsOn(String view, List<String> types) {
//        List<DependsOn> list = data.dependsOn
//        if (list == null) {
//            list = new ArrayList<DependsOn>()
//            data.dependsOn = list
//        }
//        list << new DependsOn(view, types)
//    }
}
