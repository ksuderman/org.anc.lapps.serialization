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

import org.junit.After
import org.junit.Before
import org.junit.Test
import static org.junit.Assert.*

import org.lappsgrid.serialization.lif.Contains
import org.lappsgrid.serialization.lif.View

/**
 * @author Keith Suderman
 */
class ContainsTests {
    View view

    @Before
    void setup() {
        view = new View()
        view.id = 'v0'
    }

    @After
    void tearDown() {
        view = null
    }

    @Test
    void justTheDefaultFields() {
        Contains c = view.addContains('T', 'producer', 'type')
        assertEquals 2, c.size()
        assertEquals 'producer', c.producer
        assertEquals 'type', c.type

        c = roundTrip(c)
        assertEquals 2, c.size()
        assertEquals 'producer', c.producer
        assertEquals 'type', c.type
    }

    @Test
    void allFieldsWithHelpers() {
        Contains c = view.addContains('T', 'producer', 'type')
        assert 2 == c.size()
        c.url = 'url'
        c.tagSet = 'tagSet'
        c.dependsOn = [view:'v1', type:'T2']
        assert 5 == c.size()
        assert 'producer' == c.producer
        assert 'type' == c.type
        assert 'url' == c.url
        assert 'tagSet' == c.tagSet

        c = roundTrip(c)
        assert 5 == c.size()
        assert 'producer' == c.producer
        assert 'type' == c.type
        assert 'url' == c.url
        assert 'tagSet' == c.tagSet
    }

    @Test
    void arbitraryField() {
        Contains c = view.addContains('T', 'producer', 'type')
        c.foo = 'bar'
        c.list = [1, 2, 3]

        assert 4 == c.size()
        assert 'producer' == c.producer
        assert 'type' == c.type
        assert 'bar' == c.foo
        assert [1,2,3] == c.list

        c = roundTrip(c)
        assert 4 == c.size()
        assert 'producer' == c.producer
        assert 'type' == c.type
        assert 'bar' == c.foo
        assert [1,2,3] == c.list
    }

    @Test
    void getAndPut() {
        Contains c = view.addContains('T', 'producer', 'type')
        c.put('foo', 'bar')
        c.put('list', [1,2,3])
        assert 4 == c.size()
        assert 'producer' == c.producer
        assert 'type' == c.type
        assert 'bar' == c.foo
        assert [1,2,3] == c.list

        c = roundTrip(c)
        assert 4 == c.size()
        assert 'producer' == c.producer
        assert 'type' == c.type
        assert 'bar' == c.get('foo')
        assert [1,2,3] == c.get('list')
    }

    // Serialize to a JSON string and then parse it back into an object.
    Contains roundTrip(Contains object) {
        String json = Serializer.toJson(object)
        return Serializer.parse(json, Contains)
    }
}
