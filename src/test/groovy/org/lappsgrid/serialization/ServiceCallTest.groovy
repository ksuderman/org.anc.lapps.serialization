package org.lappsgrid.serialization

import org.junit.*
import org.lappsgrid.serialization.aas.Token
import org.lappsgrid.serialization.lif.Container
import org.lappsgrid.serialization.service.Execute

import static org.junit.Assert.*

/**
 * @author Keith Suderman
 */
class ServiceCallTest {

    @Test
    void testExecute() {
        println "ServiceCallTest.testExecute"
        Container container = ContainerFactory.createContainer()
        Execute before = new Execute(container)
        String json = before.asJson() //Serializer.toPrettyJson(before)
        println json
        Execute after = Serializer.parse(json, Execute)

        compareContainers(before.payload, after.payload)
    }

    @Test
    void testExecuteNoToken() {
        println "ServiceCallTest.testExecuteNoToken"
        Container container = ContainerFactory.createContainer();
        Execute before = new Execute(container)
        String json = Serializer.toPrettyJson(before)
        Execute after = Serializer.parse(json, Execute)
        assertTrue before.discriminator == after.discriminator
        compareContainers before.payload, after.payload
    }

    @Test
    void testExecuteFromMap() {
        println "ServiceCallTest.testExecuteFromMap"
        Container c1 = ContainerFactory.createContainer()
        Execute execute = new Execute(c1)
        String json = Serializer.toPrettyJson(execute)
//        println json
        Map map = Serializer.parse(json, Map)
//        println "Discriminator is ${map.discriminator}"
        Container c2 = new Container(map.payload)
        println Serializer.toPrettyJson(c2)
//        println c1.language
//        println c2.language
        assertTrue c1.language == c2.language
        assertTrue c1.text == c2.text
        assertTrue c1.views.size() == c2.views.size()
        assertTrue c1.metadata.size() == c2.metadata.size()
        compareMaps c1.metadata, c2.metadata
    }

    void compareContainers(Container c1, Container c2) {
        check "Content text", c1.content.value, c2.content.value
        check "Content language", c1.content.language, c2.content.language
        check "Text", c1.text, c2.text
        check "Language", c1.language, c2.language
        check "Number of views", c1.views.size(), c2.views.size()
        compareMaps c1.metadata, c2.metadata
    }

    void check(String message, def expected, def actual) {
        if (expected != actual) {
            fail "${message}: Expect $expected Found $actual"
        }
    }

    void compareMaps(Map map1, Map map2) {
        assertTrue map1.size() == map2.size()
        map1.each { key, value ->
            Object object = map2[key]
            assertNotNull "Expected a value for ${key}", object
            if (value instanceof Map) {
                if (object instanceof Map) {
                    compareMaps(value, (Map)object)
                }
                else {
                    fail "Expected Map for ${key} Found ${object.class.name}"
                }
            }
            else if (value instanceof Collection) {
                if (object instanceof Collection) {
                    compareCollections((Collection)value, (Collection)object)
                }
                else {
                    fail "${key} expected Collection Found ${object.class.name}"
                }
            }
            else {
                assertTrue "$key Expected $value Found $object", value == object
            }
        }
    }

    void compareCollections(Collection c1, Collection c2) {
        assertTrue c1.size() == c2.size()
        c1.each { assertTrue c2.contains(it) }
        c2.each { assertTrue c1.contains(it) }
    }

    void compareTokens(Token t1, Token t2) {
        assertTrue 'Token UUIDs do not match', t1.uuid == t2.uuid
        assertTrue 'Token issuers do not match', t1.issuer == t2.issuer
        assertTrue 'Token timestamps do not match', t1.timestamp == t2.timestamp
        assertTrue 'Token lifetimes do not match', t1.lifetime == t2.lifetime
    }
}