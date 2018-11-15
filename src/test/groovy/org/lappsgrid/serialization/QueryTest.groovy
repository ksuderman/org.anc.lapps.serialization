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

import org.junit.*

import static org.junit.Assert.*

import org.lappsgrid.serialization.lif.Container
import org.lappsgrid.serialization.lif.View

/**
 * @author Keith Suderman
 */
class QueryTest {
    @Test
    void  testFindGreeting() {
        Container container = ContainerFactory.createContainer()
        List<View> views = container.findViewsThatContain('Greeting')
        assertTrue views.size() == 2

        views = container.findViewsThatContainBy('Greeting', 'http://www.anc.org')
        assertTrue views.size() == 1

        views = container.findViewsThatContainBy('Greeting', 'http://gate.ac.uk')
        assertTrue views.size() == 1
    }

//    boolean contains(View view, String type, String producer=null) {
//        Map map = view.metadata['contains']
//        if (!map) return false
//        Contains contains = map[type]
//        if (!contains) return false
//        if (producer) {
//            return contains.producer == producer
//        }
//        return true
//    }
}
