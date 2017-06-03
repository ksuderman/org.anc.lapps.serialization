package org.lappsgrid.serialization

import groovy.json.JsonBuilder
import org.junit.*
import org.lappsgrid.serialization.lif.Annotation
import org.lappsgrid.serialization.lif.Contains
import org.lappsgrid.serialization.lif.View

import static org.junit.Assert.*

/**
 * @author Keith Suderman
 */
class ViewTests {

    View view

    @Before
    void setup() {
        view = new View()
        view.id = 'v0'
    }

    @After
    void teardown() {
        view = null
    }

    @Test
    void findAnnotationById() {
        Annotation w1 = view.newAnnotation("w1", "type", 0, 1)
        Annotation w2 = view.newAnnotation("w2", "type", 1, 2)
        Annotation actual = view.findById("w1")
        assertNotNull actual
        assertTrue w1.is(actual)
        assertEquals(0, w1.start)
        assertEquals(1, w1.end)

        actual = view.findById("w2")
        assertNotNull actual
        assertTrue(w2.is(actual))
        assertEquals(1, w2.start)
        assertEquals(2, w2.end)

        actual = view.findById("foobar")
        assertNull actual
    }

    @Test
    void findByAtType() {
        view.newAnnotation("w1", "word", 0, 1)
        view.newAnnotation("w2", "word", 1, 2)
        view.newAnnotation("w3", "word", 2, 3)
        view.newAnnotation("s1", "sentence", 3, 4)

        List<Annotation> list = view.findByAtType("word")
        assertNotNull list
        assertEquals(3, list.size())

        list = view.findByAtType("sentence")
        assertNotNull list
        assertEquals(1, list.size())

        list = view.findByAtType("foobar")
        assertNotNull list
        assertEquals(0, list.size())
    }

    @Test
    void noStart() {
        Annotation expected = view.newAnnotation("w1", "type")
        String json = Serializer.toJson(expected)
        Annotation actual = Serializer.parse(json, Annotation)
        assertNotNull(actual)
        assertNull(actual.start)
        assertNull(actual.end)
    }

    @Test
    void canAddContains() {
        view.addContains("dummy", "test", "dummyType")
        assertTrue(view.contains("dummy"))
        Contains contains = view.getContains("dummy")
        assert(contains != null)
        assertEquals(2, contains.size())
        assertEquals("test", contains.producer)
        assertEquals("dummyType", contains.type)
    }

    @Test
    void canAddArbitraryContainsMetadata() {
        view.addContains("dummy", "test", "dummyType")
        assertTrue(view.contains("dummy"))
        view.getContains("dummy").addMetadata("arbitrariness", "true")
        assertTrue(view.getContains("dummy").getMetadata("arbitrariness") == "true")
    }

}
