package org.lappsgrid.serialization

import org.lappsgrid.serialization.lif.Annotation
import org.lappsgrid.serialization.lif.Contains
import org.lappsgrid.serialization.lif.View

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

/**
 * @author Keith Suderman
 */
class Utils {

    static final ZoneId UTC = ZoneId.of("UTC")

    static String deepCopy(String string) {
        return string
    }

    static Number deepCopy(Number number) {
        return number
    }

    static Boolean deepCopy(Boolean bool) {
        return bool
    }

    static List deepCopy(List list) {
        List result = []
        list.each {
            if (it != null) {
                result.add(deepCopy(it))
            }
        }
        return result
    }

    static Set deepCopy(Set set) {
        Set result = new HashSet()
        set.each  {
            if (it != null) {
                result.add(deepCopy(it))
            }
        }
        return result
    }

    static Map deepCopy(Map map) {
        Map result = [:]
        map.each { key,value ->
            if (value) {
                result[key] = deepCopy(value)
            }
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

    static String timestamp() {
        return ZonedDateTime.now(UTC).toString()
    }

    static ZonedDateTime parseTime(String time) {
        return ZonedDateTime.parse(time)
    }

}
