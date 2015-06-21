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

import org.junit.After
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.lappsgrid.serialization.lif.Annotation
import org.lappsgrid.serialization.lif.Container
import org.lappsgrid.serialization.lif.View

import static org.junit.Assert.assertNotNull
import static org.junit.Assert.assertTrue

/**
 * @author Keith Suderman
 */
public class ContainerTest {

    public final String INPUT_FILE_NAME = "/Bartok.txt"

    private File TEST_FILE

    public ContainerTest() {}

    @Before
    public void setup()
    {
        TEST_FILE = File.createTempFile("containerTest", ".json")
    }

    @After
    public void tearDown()
    {
        if (!TEST_FILE.delete())
        {
            TEST_FILE.deleteOnExit()
        }
        TEST_FILE = null
    }

    @Ignore
    public void testTempFile()
    {
        println "Temp file is ${TEST_FILE.path}"
        assertTrue("Temp file not found", TEST_FILE.exists())
    }

    @Test
    public void testPrettyJson()
    {
        println "ContainerTest.testPrettyJson"
        Container original = new Container();
        original.text = getResource(INPUT_FILE_NAME)
        TEST_FILE.withWriter('UTF-8') {
            it.write(Serializer.toPrettyJson(original))
            it.flush()
            it.close()
        }


        final String json = TEST_FILE.getText('UTF-8')
        Container copy = Serializer.parse(json, Container);
        assertTrue(original.text == copy.text)
    }

    @Test
    public void testJson()
    {
        println "ContainerTest.testJson"
        final Container original = new Container()
        original.text = getResource(INPUT_FILE_NAME)
        TEST_FILE.withWriter('UTF-8') {
            it.write(Serializer.toJson(original))
            it.flush()
            it.close()
        }
        final String json = TEST_FILE.getText('UTF-8')
        Container copy = Serializer.parse(json, Container)
        assertTrue(original.text == copy.text)
    }

    @Test
    public void testContainerMetadata()
    {
        println "ContainerTest.testContainerMetadata"
        Container container = new Container()
        container.text = 'Hello world'
        container.language = 'en'
        container.metadata.text = 'text'
        container.metadata.map = [foo:'foo', bar:'bar']
        container.metadata.list = [0,1,2,3,4]

        String json = Serializer.toJson(container)
        container = Serializer.parse(json, Container)
        assertTrue container.text == 'Hello world'
        assertNotNull container.metadata
        assertNotNull container.metadata.map
        assertTrue container.metadata.map.foo == 'foo'
        assertTrue container.metadata.map.bar == 'bar'
        assertNotNull container.metadata.list
        assertTrue container.metadata.list instanceof List
        assertTrue container.metadata.list.size() == 5
        assertTrue container.metadata.list[0] == 0
        assertTrue container.metadata.list[4] == 4
//        println Serializer.toPrettyJson(container)
    }

    @Test
    public void testAnnotations() {
        println "ContainerTest.testAnnotations"
        Container container = new Container()
        container.text = 'hello world'
        container.metadata = [test: 'this is a test']
        View view = container.newView()
        view.metadata.producedBy = 'Test code'
        def a = view.newAnnotation()
        a.id = 'a12'
        a.start = 0
        a.end = 5
        a.label = 'Token'
        a.atType = 'Token'
        a.type = 'Lapps:TextAnnotation'
        a.features.pos = 'NN'
        a.features.lemma = 'hello'

        String json = Serializer.toPrettyJson(container)

        container = Serializer.parse(json, Container)
        assertTrue(container.views.size() == 1)
        view = container.views[0]
        assertTrue(view.annotations.size() == 1)
        a = view.annotations[0]
        assertTrue(a.label == 'Token')
        assertTrue(a.type == 'Lapps:TextAnnotation')
        println json
    }

    @Ignore
    public void testContext() {
        println "ContainerTest.testContext"
        String uri = 'http://langrid.org/vocab/morpheme'
        Container container = new Container()
        Map context = container.context
        context['morpheme'] = [
                '@id': uri,
                '@type': '@id'
        ]
        container.text = 'hello world'
        def view = new View()
        def a = new Annotation()
        a.label = 'morpheme'
        a.id = "m1"
        a.start = 0
        a.end = 5
        String json = Serializer.toPrettyJson(container)
        container = Serializer.parse(json, Container)
        assertTrue(container.context.toString(), container.context.morpheme != null)
        assertTrue(container.context.morpheme['@id'] == uri)
        assertTrue(container.context.morpheme['@type'] == '@id')
        println json
    }

//    @Test
//    public void testLocalContext() {
//        Container container = new Container(true)
//        assertTrue("Context is not a map!", container.context instanceof Map)
//        // Now check a few a few values in the context.
//        assertTrue('http://vocab.lappsgrid.org/' == container.context['@vocab'])
//        assertTrue('http://vocab.lappsgrid.org/types/' == container.context.types)
//    }

    @Test
    public void testDefaultContext() {
        Container container = new Container()
        assertTrue container.context == Container.REMOTE_CONTEXT
        String json = Serializer.toJson(container)
        container = Serializer.parse(json, Container)
        assertTrue container.context == Container.REMOTE_CONTEXT

        container = new Container(Container.ContextType.REMOTE)
        assertTrue container.context == Container.REMOTE_CONTEXT

        container = new Container(Container.ContextType.LOCAL)
        assertTrue container.context == Container.LOCAL_CONTEXT
    }

    @Ignore
    public void testRemoteContext() {
        Container container = new Container(false)
        assertTrue("Context is not a string!", container.context instanceof String)
        assertTrue(container.context == "http://vocab.lappsgrid.org/context-1.0.0.jsonld")
        // Make sure the URL can be dereferenced.
        URL url = new URL(container.context)
        assertNotNull(url.text)
    }

    private String getResource(String name) {
        return this.class.getResource(name).getText('UTF-8')
    }
}
