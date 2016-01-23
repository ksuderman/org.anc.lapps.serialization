package org.lappsgrid.serialization

import org.junit.*
import org.lappsgrid.serialization.lif.Annotation

import static org.junit.Assert.*

/**
 * @author Keith Suderman
 */
class AnnotationTests {

    Annotation annotation

    @Before
    void setup() {
        annotation = new Annotation("id", "label", 0, 1)
    }
    @Test
    void testCopyConstructor() {
        annotation.features.pos = 'VB'
        annotation.features.word = 'be'
        annotation.features.list = ['a', 'b', 'c']
        annotation.features.set = ['d', 'e', 'f'] as HashSet
        Annotation copy = new Annotation(annotation)
        // This ensures a deep copy was performed of original.
        annotation.features = [:]
        assertEquals('id', copy.id)
        assertEquals('label', copy.label)
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
        annotation.addFeature("set", [1,2,3] as HashSet)
        String json = Serializer.toPrettyJson(annotation)
        annotation = Serializer.parse(json, Annotation)
        assertTrue(annotation.features.set == [1,2,3])
    }

    @Test
    void testGetFeatureSet() {
        annotation.addFeature('set', [1, 2, 3] as HashSet)
        Object value = annotation.getFeatureSet('set')
        assertTrue (value instanceof Set)
        Set set = (Set) value
        assertTrue set == [1,2,3] as HashSet
    }

    @Test
    void testFeatureFeatureListAsSet() {
        annotation.addFeature('set', [1,2,3])
        Object value = annotation.getFeatureSet('set')
        assertTrue (value instanceof Set)
        Set set = (Set) value
        assertEquals(3, set.size())
        assertTrue(set.contains(1))
        assertTrue(set.contains(2))
        assertTrue(set.contains(3))
    }

    @Test(expected = IllegalArgumentException)
    void testGetFeatureSetEx() {
        annotation.addFeature('name', 'value')
        annotation.getFeatureSet('name')
    }

    @Test
    void testGetFeatureMap() {
        annotation.addFeature('map', [key: 'value'])
        Map map = annotation.getFeatureMap('map')
        assertNotNull(map)
        assertEquals(1, map.size())
        assertEquals('value', map.key)
    }

    @Test(expected = IllegalArgumentException)
    void testGetFeatureMapEx() {
        annotation.addFeature('name', 'value')
        annotation.getFeatureMap('name')
    }

    @Test
    void testGetFeatureList() {
        annotation.addFeature('list', [1,2,3])
        Object value = annotation.getFeatureList('list')
        assertTrue(value instanceof List)
        List list = (List) value
        assertEquals(3, list.size())
        assertEquals(1, list[0])
        assertEquals(2, list[1])
        assertEquals(3, list[2])
    }

    @Test(expected = IllegalArgumentException)
    void testGetFeatureListEx() {
        annotation.addFeature('name', 'value')
        annotation.getFeatureList('name')
    }

}
