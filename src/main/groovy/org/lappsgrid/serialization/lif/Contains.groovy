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
@JsonPropertyOrder(['type', 'producer', 'url', 'tagSet', 'dependsOn'])
class Contains {

    @JsonIgnore
    String atType

    // TODO: 3/1/2018 find a way to programmatically read in these names from vocabulary
    static def tagsetKeys = [(Uri.POS)                 : "posTagSet",
                             (Uri.NE)                  : "namedEntityCategorySet",
                             (Uri.PHRASE_STRUCTURE)    : "categorySet",
                             (Uri.DEPENDENCY_STRUCTURE): "dependencySet"]

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

//    @JsonIgnore
//    void setAtType(String atType) {
//        this.atType = atType
//    }
//
//    @JsonIgnore
//    String getAtType() {
//        return this.atType
//    }

    @JsonProperty
    void setTagSet(String value) throws LifException {
        println "Setting tagset to $value"
        if (tagsetKeys.containsKey(getAtType())) {
            data[tagsetKeys[getAtType()]] = value
        } else {
            throw new LifException("No tagset-like feature is defined for ${this.atType} ")
        }
    }

    String getTagSet() throws LifException {
        println 'Getting tag set.'
        if (tagsetKeys.containsKey(getAtType())) {
            return data[tagsetKeys[getAtType()]]
        } else {
            throw new LifException("No tagset-like feature is defined for ${this.atType} ")
        }
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
