package org.lappsgrid.serialization

import org.lappsgrid.serialization.lif.Annotation
import org.lappsgrid.serialization.lif.Contains
import org.lappsgrid.serialization.lif.View

/**
 * @author Keith Suderman
 */
class Utils {
    static String deepCopy(String string) {
        return string
    }

    static Number deepCopy(Number number) {
        return number
    }

    static List deepCopy(List list) {
        List result = []
        list.each { result.add(deepCopy(it)) }
        return result
    }

    static Set deepCopy(Set set) {
        Set result = new HashSet()
        set.each  { result.add(deepCopy(it)) }
        return result
    }

    static Map deepCopy(Map map) {
        Map result = [:]
        map.each { key,value ->
            result[key] = deepCopy(value)
        }
        return result
    }

    static Annotation deepCopy(Annotation annotation) {
        return new Annotation(annotation)
    }

    static Contains deepCopy(Contains contains) {
        return new Contains(contains)
    }

    static View deepCopy(View view) {
        return new View(view)
    }
}
