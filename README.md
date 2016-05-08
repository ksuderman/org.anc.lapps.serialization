LAPPS Exchange Data Structures (LEDS).
======================================

### Build Status

[![Master build status](http://grid.anc.org:9080/travis/svg/lapps/org.lappsgrid.serialization?branch=master)](https://travis-ci.org/lapps/org.lappsgrid.serialization)
[![Develop build status](http://grid.anc.org:9080/travis/svg/lapps/org.lappsgrid.serialization?branch=develop)](https://travis-ci.org/lapps/org.lappsgrid.serialization)

The LAPPS Exchange Data Structures are a small set of Java classes (Groovy classes 
actually) that provide the data model for the JSON-LD data exchanged by services on the
LAPPS grid. 

## Maven

To use this package you need to add following dependency to the project's pom.xml file:

```xml
<dependency>
  <groupId>org.lappsgrid</groupId>
  <artifactId>serialization</artifactId>
  <version>2.3.0</version>
</dependency>
```

## Usage

### Serialization

The `org.lappsgrid.serialization.Serializer` class provides static methods for converting
between the LEDS objects and their JSON representations.

```groovy
Container container = new Container()
...
String json = Serializer.toJson(container)
...
Container anotherContainer = Serializer.parse(json, Container.class)
```

### LAPPS Interchange Format

#### Container


There are three main classes that make up LEDS:

1. **Annotation** Used to store information about a single annotation
```groovy
class Annotation {
       String type
       Long start
       Long end
       Map features
       Map metadata
}
```

2. **View** Used to store the annotations for a single view of the document.
```groovy
class View {
      List<Annotation> annotations
      Map metadata
}
```

3. **Container** Used to store the original data (text, audio, etc) plus all annotations produces by all
processing services.
```groovy
class Container {
        String text
        String language
        List<View> views
        Map metadata
}
```

## Contexts

Container objects define a @context in the JSON document itself.  To refer to the remote @context at http://vocab.lappsgrid.org/context-1.0.0.jsonld create a Container and pass _false_ as the only parameter.

```java
Container containerWithLocalContext = new Container();
Container containerWithRemoteContext = new Container(false);
```

When working with a local @context the entries in the @context are stored in a hash map that can be manipulated at runtime:

```java
Container container = new Container(); // Creates a container with a local @context object.
Map context = container.getContext();
context.put("myToken", "http://example.com/token");
```

## Metadata

Almost every LEDS object contains a hash map for storing metadata.  In general applications are
free to store whatever metadata they need in these maps.  The only required metadata
is the _contains_ map in the _Views_.

The _contains_ map contains information about the annotations available in that
view.  The _contains_
map looks like

```
"metadata" : {
      "contains" : {
        "Token" : {
          "producer" : "http://service.that.produces.the.tokens",
          "type" : "tokenization:custom"
        }
      }
    },
```

To simplify the process of creating the _contains_ map the View class
provides a _addContains(String label, String producer, String type)_ method. For
 example the above JSON can be generated with:

```java
Container container = new Container(false);
View view = new View()
view.addContains("Token", "http://service.that.produces.the.tokens", "tokenization:custom");
container.getViews().add(view)
```

## Examples

### Java

```java
Annotation a = new Annotation();
a.setType("token");
a.setStart(0);
a.setEnd(5);
a.getFeatures().put("pos", "UH")

View view = new View();
view.getMetadata().put("pass", "1");
view.getAnnotation().add(a);

Container container = new Container();
container.setText("Hello world");
container.setLanguage("en");
container.getViews().add(view);

String json = Serializer.toPrettyJson(container);
System.out.println(json);
...

Container container = Serializer.parse(json, Container.class)
```

### Groovy
```groovy
Annotation a = new Annotation(type:'token', start:0, end:5)
a.features['pos'] = 'UH'

View view = new View()
view.metadata['pass'] = 1
view.annotations.add(a)

Container container = new Container(text:'Hello world')
container.views.add(view)

String json = Serializer.toPrettyJson(container)

...

Container container = Serializer.parse(json, Container)
```
