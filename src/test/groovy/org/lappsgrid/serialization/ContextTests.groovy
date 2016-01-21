package org.lappsgrid.serialization

import org.junit.*
import org.lappsgrid.serialization.lif.Container

import static org.junit.Assert.*

/**
 * @author Keith Suderman
 */
class ContextTests {

    @Test
    void testLocalContextNotDeleted() {
        Container container = new Container()
        container.text = 'Hello world'
        container.language = 'en'
        container.context = Container.LOCAL_CONTEXT

        // Round-trip the Container through a JSON string.
        String json = new DataContainer(container).asPrettyJson()
        Data data = Serializer.parse(json, Data)
        Container copy = new Container((Map)data.getPayload())
        assertNotNull copy.context
    }

    @Test
    void testRemoteContextNotDeleted() {
        Container container = new Container()
        container.text = 'Hello world'
        container.language = 'en'

        // Round-trip the Container through a JSON string.
        String json = new DataContainer(container).asPrettyJson()
        Data data = Serializer.parse(json, Data)
        Container copy = new Container((Map)data.getPayload())
        assertNotNull copy.context
    }
}
