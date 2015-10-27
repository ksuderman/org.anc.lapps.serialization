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

import com.fasterxml.jackson.annotation.JsonPropertyOrder

/**
 * A View consists of some metadata and a list of annotations.
 * <p>
 * Each LAPPS processing service will generally place the annotations it generates in
 * its own View object.  This makes it easier to determine the annotations
 * produces by each processor and to quickly extract that subset of annotations.
 * <p>
 * However, the concept of a View is meant to be very generic and can contain
 * any arbitrary collection of annotations grouped with arbitrary metadata.
 *
 * @author Keith Suderman
 */
@JsonPropertyOrder(['id', 'metadata', 'annotations'])
public class View {
    /**
     * A unique ID value for this view.
     */
    String id

    /**
     * User defined metadata for this processing step.
     */
    Map metadata

    /**
     * The annotations that belong to this processing step.
     */
    List<Annotation> annotations

    public View() {
        metadata = [:]
        annotations = []
    }

    public View(Map map) {
        if (map == null) {
            return
        }
        this.id = map['id']
        metadata = [:]
        map.metadata.each { name, value ->
            metadata[name] = value
        }
        //annotations = map.annotations
        annotations = []
        map.annotations.each { a ->
            annotations << new Annotation(a)
        }
    }

    /**
     * Adds the name/value pair to the metadata map.
     *
     */
    void addMetaData(String name, Object value) {
        metadata[name] = value
    }

    /**
     * Adds an annotation to the processing step's list of annotations.
     */
    void addAnnotation(Annotation annotation) {
        annotations << annotation
    }

    /**
     * Adds an annotation to the processing step's list of annotations.
     */
    void add(Annotation annotation) {
        annotations << annotation
    }

    /**
     * Returns true if the metadata.contains map contains the named key. Returns
     * false otherwise.
\    */
    boolean contains(String name) {
        return metadata.contains[name] != null
    }

    /**
     * Creates and returns a new Annotation.
     */
    Annotation newAnnotation() {
        Annotation a = new Annotation()
        annotations.add(a)
        return a
    }

    /**
     * Creates and returns a new Annotation.
     */
    Annotation newAnnotation(String id, String type, long start=-1, long end=-1) {
        Annotation a = newAnnotation()
        a.id = id
        a.setAtType(type)
        a.setStart(start)
        a.setEnd(end)
        return a
    }

    Contains getContains(String name) {
        return metadata?.contains[name]
    }

    /**
     * Adds an entry to the <i>contains</i> section in the view object.
     * @param name The URI of the annotation type being created.
     * @param producer The tool or program that generated the view.
     * @param type The annotation type. Currently this field is under-defined.
     */
    void addContains(String name, String producer, String type) {
//        ValueObject containsType = new ValueObject(type:type, value:value)
        if (metadata.contains == null) {
            metadata.contains = [:]
        }
        metadata.contains[name] = new Contains(producer:producer, type:type)
    }

}
