/*-
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

    public Contains() { }

    public Contains(Map map) {
        if (map == null) {
            return
        }
        this.url = map['url']
        this.producer = map['producer']
        this.type = map['type']
    }
}
