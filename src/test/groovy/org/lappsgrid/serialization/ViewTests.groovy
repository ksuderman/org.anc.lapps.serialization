package org.lappsgrid.serialization

import groovy.json.JsonBuilder
import org.junit.*
import org.lappsgrid.serialization.lif.Annotation
import org.lappsgrid.serialization.lif.Container
import org.lappsgrid.serialization.lif.Contains
import org.lappsgrid.serialization.lif.View

import java.time.ZoneId

import static org.lappsgrid.discriminator.Discriminators.*;

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
        view.getContains("dummy").put("arbitrariness", "true")
        assertTrue(view.getContains("dummy").get("arbitrariness") == "true")
    }

    @Test
    void containsArbitraryFields() {
        view.addContains('T', 'T.producer', 'T.type')
        view.metadata.contains['T'].dependsOn = 'v1'
        println new JsonBuilder(view).toPrettyString()
    }

    @Test
    void metadataSerialization() {
        Container c = new Container();
        View v = c.newView();
        v.addMetaData("key", "value")
        v.addMetaData("list", [0,1,2,3,4])

        Data data = new Data(Uri.LIF, c);
        data = Serializer.parse(data.asJson());
        c = new Container((Map) data.payload);
        v = c.getView(0);
        assertEquals("value", v.getMetaData("key"));
        List list = (List) v.getMetaData("list");
        assert 5 == list.size();
        for (int i=0; i < 5; ++i) {
            assert i == list.get(i);
        }
    }

    @Test
    void hasTimestamp() {
        View v = new View()
        assert null != v.getTimestamp()

        View clone = new View(v)
        assert null != clone.getTimestamp()
        assert v.getTimestamp() != clone.getTimestamp()
    }

}
