package org.lappsgrid.serialization

import org.lappsgrid.serialization.lif.Annotation
import org.lappsgrid.serialization.lif.Contains
import org.lappsgrid.serialization.lif.View

/**
 * @author Keith Suderman
 */
class Utils {
    static String deepCopy(String string) {
//        println "Copying string $string"
        return string
    }

    static Number deepCopy(Number number) {
//        println "Copying number $number"
        return number
    }

    static Boolean deepCopy(Boolean bool) {
//        println "Copying $bool"
        return bool
    }

    static List deepCopy(List list) {
//        println "Copying list"
        List result = []
        list.each { result.add(deepCopy(it)) }
        return result
    }

    static Set deepCopy(Set set) {
//        println "Copying set"
        Set result = new HashSet()
        set.each  { result.add(deepCopy(it)) }
        return result
    }

    static Map deepCopy(Map map) {
        Map result = [:]
        map.each { key,value ->
//            println "Copying $key"
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
