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
import static org.lappsgrid.discriminator.Discriminators.Uri
import org.lappsgrid.serialization.lif.Container.ContextType
import org.lappsgrid.serialization.lif.Annotation
import org.lappsgrid.serialization.lif.Container
import org.lappsgrid.serialization.lif.View

import static org.junit.Assert.assertEquals
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

    @Test
    public void testInitFromMap() {
        Container container = ContainerFactory.createContainer()

        // A round-trip through JSON calls the Container.initFromMap
        // method.
        String json = new DataContainer(container).asJson()
        Data data = Serializer.parse(json, Data)
        Container copy = new Container(data.payload)
        assertEquals container.text, copy.text
        assertEquals(container.language, copy.language)
        assertEquals(container.views.size(), copy.views.size())
        assertEquals(container.context, copy.context)
    }

    @Test
    public void testCopyConstructor() {
        Container container = new Container()
        container.metadata.key = 'value'
        container.text = "Hello world"
        container.language = "en"
        View view = container.newView()
        view.id = "v1"
        view.add(new Annotation("w1", "word", 0, 5))
        view.add(new Annotation("w2", "word", 6, 11))
        view.addContains("words", "tests", "string")

        Container copy = new Container(container)
        assertEquals container.text, copy.text
        assertEquals 'en', container.language
        assertEquals 'en', copy.language
        assertEquals container.language, copy.language
        assertEquals container.context, copy.context
        assertEquals 1, copy.metadata.size()
        assertNotNull copy.metadata['key']
        assertEquals 'value', copy.metadata.key
        assertEquals container.views.size(), copy.views.size()
        View v1 = container.views[0]
        View v2 = copy.views[0]
        assertEquals v1.id, v2.id
        assertEquals 1, v2.metadata.size()
        assertNotNull v2.metadata['contains']
        assertNotNull v2.metadata.contains.words
        assertEquals 'tests', v2.metadata.contains.words.producer
        assertEquals 'string', v2.metadata.contains.words.type
        assertEquals 2, v2.annotations.size()
        assertEquals "w1", v2.annotations[0].id
        assertEquals "w2", v2.annotations[1].id
    }

    @Test
    void testFindAllNoContains() {
        Container container = new Container()
        View view = container.newView()
        List<View> views = container.findViewsThatContain(Uri.TOKEN)
        assertNotNull views
        assert views.isEmpty()
    }

    @Test
    void testFindAll() {
        Container container = new Container()
        newView(container, Uri.TOKEN)
        newView(container, Uri.SENTENCE)
        newView(container, Uri.TOKEN)
        newView(container, Uri.SENTENCE)
        newView(container, Uri.NE)
        newView(container, Uri.TOKEN)

        List<View> views = container.findViewsThatContain(Uri.TOKEN)
        assertEquals(3, views.size())
        views = container.findViewsThatContain(Uri.SENTENCE)
        assertEquals(2, views.size())
        views = container.findViewsThatContain(Uri.NE)
        assertEquals(1, views.size())
        views = container.findViewsThatContain('foobar')
        assertNotNull(views)
        assertEquals(0, views.size())

        views = container.findViewsThatContainBy(Uri.TOKEN, 'Test')
        assertEquals(3, views.size())
        views = container.findViewsThatContainBy(Uri.TOKEN, 'foobar')
        assertEquals(0, views.size())
        views = container.findViewsThatContainBy(Uri.SENTENCE, 'Test')
        assertEquals(2, views.size())
        views = container.findViewsThatContainBy(Uri.NE, 'Test')
        assertEquals(1, views.size())
    }

    @Test
    public void testNewViewString() {
        Container container = new Container()
        View v1 = container.newView("v1")
        View v2 = container.newView("v2")

        assertEquals 2, container.views.size()
        assertEquals "v1", container.views[0].id
        assertEquals "v2", container.views[1].id
    }

    // To expose https://github.com/lapps/org.lappsgrid.serialization/issues/23
    // The Container(Map) constructor was not creating the list of Views correctly
    // instead creating lists of Maps instead of List<View> and List<Annotation>.
    @Test
    public void testMapConstructor() {
        Container expected = new Container()
        expected.text = "hello world"
        expected.language = "en"
        View view = expected.newView("v0")
        view.newAnnotation("a1", Uri.TOKEN, 0, 5)
        view.newAnnotation("a2", Uri.TOKEN, 6, 11)
        view = expected.newView("v1")
        view.newAnnotation("s1", Uri.SENTENCE, 0, 11)
        view = expected.newView("v2")
        view.newAnnotation("ne1", Uri.PERSON, 6, 11)

        String json = Serializer.toJson(expected)
        Map map = Serializer.parse(json, HashMap.class)

        Container container = new Container(map)
        assertEquals expected.text, container.text
        assertEquals expected.views.size(), container.views.size()

        View expectedView = expected.views[0]
        View containerView = container.views[0]
        assertEquals expectedView.class, containerView.class
        assertEquals expectedView.annotations.size(), containerView.annotations.size()
        assertEquals expectedView.annotations[0].class, containerView.annotations[0].class

    }

    void newView(Container container, String type) {
        View view = container.newView()
        if (type) {
            view.addContains(type, 'Test', 'test')
        }
    }

    //https://github.com/lapps/org.lappsgrid.serialization/issues/25
    @Test
    public void testNullContext() {
        Map map = [
                //'@context': 'the context',
                metadata: [:],
                text: ['@value':'hello', '@language': 'en'],
                views: [
                        [
                                id:'v0',
                                annotations: [
                                        [id:'a0', '@type':Uri.TOKEN, start:0, end:5]
                                ]
                        ],
                        [
                                id:'v1',
                                annotations: [
                                        [id:'a0', '@type':Uri.TOKEN, start:0, end:5]
                                ]
                        ]
                ]
        ]
        Container container = new Container(map)
        assertEquals('hello', container.text)
        assertEquals('en', container.language)
        assertEquals(2, container.views.size())
        assertEquals(1, container.views[0].annotations.size())
        assertEquals(1, container.views[1].annotations.size())
        assertEquals(null, container.context)
    }

    @Test
    void findViewById() {
        Container container = new Container(ContextType.REMOTE)
        View v0 = container.newView("v0")
        View v1 = container.newView("v1")
        View v2 = container.newView("v2")

        assert container.views.size() == 3
        assert v0.is(container.findViewById('v0'))
        assert v1.is(container.findViewById('v1'))
        assert v2.is(container.findViewById('v2'))
        assert null == container.findViewById('abc')
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
