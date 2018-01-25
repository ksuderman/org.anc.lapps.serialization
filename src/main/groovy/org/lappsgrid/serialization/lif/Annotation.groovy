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
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import org.lappsgrid.serialization.LappsIOException
import org.lappsgrid.serialization.Utils

/**
 * Information about a single standoff annotation.
 *
 * @author Keith Suderman
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder(['id', 'start', 'end', '@type', 'type', 'label', 'features', 'metadata'])
public class Annotation {
    /** A unique ID assigned to this annotation.
     * <p>
     * <em>Note:</em> This ID value is assigned to the annotation by the framework
     * and is not to be confused with an ID (xml:id, etc) value that the annotation
     * itself might contain.
     */
    String id

    /** The label used for the annotation, e.g. tok, s, etc. */
    String label

    /** The {@literal @}type value (if any) for the JSON element. */
    @JsonProperty('@type')
    String atType

    /** A type from the Lappsgrid vocabulary. */
    String type

    /** The start offset of the annotation. */
    Long start = null

    /** The end offset of the annotation. */
    Long end = null

    /** Features of the annotation. Features are assumed to be String name/value pairs. */
    Map features

    /** Features assigned by the framework to the annotation. E.g. a confidence
     * score, the processor that generated the annotation etc.
     */
    Map metadata

    public Annotation() {
        this.features = [:]
        this.metadata = [:]
    }

    public Annotation(String type, long start, long end) {
        this.atType = type
        this.start = start
        this.end = end
        this.features = [:]
        this.metadata = [:]
    }

    public Annotation(String id, String type, long start, long end) {
        this.id = id
        this.atType = type
        this.start = start
        this.end = end
        this.features = [:]
        this.metadata = [:]
    }

    public Annotation(String id, String type, String label, long start, long end) {
        this.id = id
        this.atType = type
        this.label = label
        this.start = start
        this.end = end
        this.features = [:]
        this.metadata = [:]
    }

    public Annotation(Annotation annotation) {
        this.id = annotation.id
        this.label = annotation.label
        this.start = annotation.start
        this.end = annotation.end
        this.atType = annotation.atType
        this.type = annotation.type
        if (annotation.features) {
            this.features = Utils.deepCopy(annotation.features)
        }
        else {
            this.features = [:]
        }
        if (annotation.metadata) {
            this.metadata = Utils.deepCopy(annotation.metadata)
        }
        else {
            this.metadata = [:]
        }
    }

    public Annotation(Map map) {
        map.each { key, value ->
            switch(key) {
                case 'label':
                    this.label = value
                    break
                case '@type':
                    this.atType = value
                    break
                case 'type':
                    this.type = value
                    break
                case 'start':
                    this.start = value as Long
                    break
                case 'end':
                    this.end = value as Long
                    break
                case 'id':
                    this.id = value
                    break
                case 'features':
                    if (value) {
                        this.features = Utils.deepCopy(value)
                    }
                    else {
                        this.features = [:]
                    }
                    break
                case 'metadata':
                    if (value) {
                        this.metadata = Utils.deepCopy(value)
                    }
                    else {
                        this.metadata = [:]
                    }
                    break
                default:
                    features[key] = Utils.deepCopy(value)
                    break
            }
        }
        if (features == null) {
            features = [:]
        }
        if (metadata == null) {
            metadata = [:]
        }
    }

    @JsonIgnore
    void addFeature(String name, String value) {
        features[name] = value
    }

    @JsonIgnore
    void addFeature(String name, Boolean value) {
        features[name] = value
    }

    @JsonIgnore
    void addFeature(String name, Number value) {
        features[name] = value
    }

    @JsonIgnore
    void addFeature(String name, Map value) {
        features[name] = value
    }

    @JsonIgnore
    void addFeature(String name, List value) {
        features[name] = value
    }

    @JsonIgnore
    void addFeature(String name, Set value) {
        features[name] = value
    }


    @JsonIgnore
    String getFeature(String name) {
        return features[name]
    }

    @JsonIgnore
    List getFeatureList(String name) throws IllegalArgumentException {
        Object value = get(name, { new ArrayList() })
        if (!(value instanceof List)) {
            throw new IllegalArgumentException("Feature value is not a List object.")
        }
        return (List) value
    }

    @JsonIgnore
    Map getFeatureMap(String name) throws IllegalArgumentException {
        Object value = get(name, { new HashMap() })
        if (!(value instanceof Map)) {
            throw new IllegalArgumentException("Feature value is not a Map object.")
        }
        return (Map) value
    }

    @JsonIgnore
    Set getFeatureSet(String name) throws IllegalArgumentException {
        Object value = get(name, { new HashSet() })
        if (value instanceof Set) {
            return (Set) value
        }
        // TODO: This is a hackaround until the serialization classes
        // fully support JSON-LD and the @set datatype.  Until then
        // the serialization classes don't know that something that looks
        // like a list (i.e. ['a', 'b', 'c']) is actually supposed to
        // be parsed as a Set.
        if (value instanceof List) {
            return new HashSet((List)value)
        }
        throw new IllegalArgumentException("Feature value is not a Set object.")
    }

    protected Object get(String name, Closure factory) {
        Object value = features[name]
        if (value) {
            return value
        }
        value = factory()
        features[name] = value
        return value
    }

    String toString() {
        if (label) {
            return "${label} (${start}-${end}) ${features}"
        }
        int index = atType.lastIndexOf('/');
        if (index < 0) {
            index = 0;
        }
        String name = atType.substring(0);
        return "${name} (${start}-${end}) ${features}"
    }

}
