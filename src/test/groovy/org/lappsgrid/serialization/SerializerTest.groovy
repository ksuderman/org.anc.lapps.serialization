/*
 * Copyright (C) 2017 The Language Applications Grid
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.lappsgrid.serialization

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.Test
import org.lappsgrid.serialization.lif.Annotation
import static org.lappsgrid.discriminator.Discriminators.*

/**
 * @author Keith Suderman
 */
class SerializerTest {
    @Test
    void confirmNullValuesExcluded() {

        Annotation a = new Annotation("a1", Uri.TOKEN, 0, 5)
        a.metadata = null
        assert null != a.features
        assert 0 == a.features.size()
        assert null == a.metadata
        String json = Serializer.toPrettyJson(a)
        println json
        assert !json.contains("metadata")
        Map map = Serializer.parse(json, HashMap)
        assert map.metadata == null
        assert map.features == null
    }

    @Test
    void zeroFalseRetained() {
        Annotation a = new Annotation("a1", Uri.TOKEN, 0, 5)
        a.features.isAnswer = false
        a.features.score = 0

        String json = Serializer.toJson(a)
        println json
        Map map = Serializer.parse(json, HashMap)
        assert 2 == a.features.size()
        assert false == a.features.isAnswer
        assert 0 == a.features.score
    }

    Map makeMap() {
        Map object = [:]
        object.value = "non null"
        object.nullValue = null
        object.map = [:]
        object.map.value = "non null"
        object.map.nullValue = null
        return object
    }
}
