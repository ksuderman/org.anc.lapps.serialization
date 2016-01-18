package org.lappsgrid.serialization

import org.junit.*
import org.lappsgrid.serialization.lif.Annotation

import static org.junit.Assert.*

/**
 * @author Keith Suderman
 */
class AnnotationTests {

    @Test
    void testCopyConstructor() {
        Annotation original = new Annotation("label", "type", 0, 1)
        original.features.pos = 'VB'
        original.features.word = 'be'
        original.features.list = ['a', 'b', 'c']
        original.features.set = ['d', 'e', 'f'] as HashSet
        Annotation copy = new Annotation(original)
        // This ensures a deep copy was performed of original.
        original.features = [:]
        assertEquals('label', copy.label)
        assertEquals('type', copy.type)
        assertEquals(0, copy.start)
        assertEquals(1, copy.end)
        assertEquals(4, copy.features.size())
        assertEquals('VB', copy.features.pos)
        assertEquals('be', copy.features.word)
        assertTrue copy.features.list == ['a', 'b', 'c']
        assertTrue copy.features.set == ['d', 'e', 'f'] as HashSet
    }

    @Test
    void testAddSetFeature() {
        Annotation annotation = new Annotation("label", "type", 0, 1)
        annotation.addFeature("set", [1,2,3] as HashSet)
        String json = Serializer.toPrettyJson(annotation)
        annotation = Serializer.parse(json, Annotation)
        assertTrue(annotation.features.set == [1,2,3])

    }
}
