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

package org.lappsgrid.serialization

import com.github.fge.jsonschema.core.report.ProcessingMessage
import com.github.fge.jsonschema.core.report.ProcessingReport
import org.anc.json.validator.Validator
import org.junit.*
import org.lappsgrid.serialization.datasource.GetRequest
import org.lappsgrid.serialization.datasource.ListRequest
import org.lappsgrid.serialization.lif.Container

import static org.junit.Assert.*

/**
 * @author Keith Suderman
 */
class ValidationTests {

    @Test
    void validateGet() {
//        String json = Serializer.toPrettyJson(new Get(token, "key"))
        String json = new GetRequest("key").asJson()
        validate(json, "action/get-schema.json")
    }

    @Test
    void validateListNoArgs() {
        String json = Serializer.toPrettyJson(new ListRequest())
        validate(json, "action/list-schema.json")
    }

    @Test
    void  validateListWithArgs() {
        final int start = 1
        final int end = 2
        ListRequest list = new ListRequest(start, end)
//        String json = Serializer.toPrettyJson(list)
        validate(list.asJson(), 'action/list-schema.json')
    }

    @Test
    void validateContainer() {
        Container container = ContainerFactory.createContainer()
        String json = Serializer.toPrettyJson(container)
        validate(json, "lif-schema.json")
    }

    void validate(String json, String schemaName) {
        // TODO Uncomment this entire block
        Validator validator = getValidator(schemaName)
        ProcessingReport result = validator.validate(json)
        if (!result.success) {
            println json
            result.asList().each { ProcessingMessage message ->
                println message.message
            }
            fail('Validation failed')
        }
    }

    // TODO Uncomment this function when re-enabling validation tests.
    private Validator getValidator(String name) {
        return new Validator(new URL("http://vocab.lappsgrid.org/schema/$name"))
    }

}
