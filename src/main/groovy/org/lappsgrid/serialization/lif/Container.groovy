/*
 * Copyright 2014 The Language Application Grid
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.lappsgrid.serialization.lif

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import org.lappsgrid.serialization.LappsIOException
import org.lappsgrid.serialization.Utils

/**
 * Container objects are the outer wrapper for all LIF objects.
 * <p>
 * A Lappsgrid Container consists of:
 * <ol>
 *     <li>the text content (with possibly the language).</li>
 *     <li>some metadata</li>
 *     <li>a list of {@link View}s</li>
 * </ol>
 * The Container object also contains helper methods to find the {@link View}s
 * containing a given annotation type:
 * <p>
 * For example:
 * <pre>
 *     <code>
 *         String token = "http://vocab.lappsgrid.org/Token"
 *         Container container = new Container();
 *         container.setText("Goodbye cruel world, I am leaving you today.");
 *         container.setLanguage("en");
 *         View view = container.newView();
 *         view.addAnnotation("tok1", token, 0, 7);
 *         ...
 *         List<View> views = container.findViewsThatContain(token);
 *         assertEquals(view, views.get(0));
 *     </code>
 * </pre>
 * <p>
 * Since LIF objects are serialized to JSON-LD they also define a {@code context}. By
 * default all LIF objects refer to the remote context document
 * <a href="http://vocab.lappsgrid.org/context-1.0.0.jsonld">http://vocab.lappsgrid.org/context-1.0.0.jsonld</a>.
 * However, a local (in-line) context can be used with allows the context to be
 * manipulated at runtime.
 * <pre>
 *     <code>
 *         Container container = new Container(ContextType.LOCAL);
 *         Map<String,String> context = (Map) container.getContext();
 *         context.put("@vocab", "http://example.com/custom/vocabulary");
 *     </code>
 * </pre>
 *
 * @author Keith Suderman
 */
@JsonPropertyOrder(["context","metadata","text","views"])
public class Container {

    public enum ContextType {
        LOCAL, REMOTE
    }

    /** The text that is to be annotated. */
    @JsonProperty('text')
    Content content;

    /** Any meta-data attached to this container. */
    Map metadata

    /** The list of annotations that have been created for the text. */
    List<View> views

    @JsonProperty("@context")
    Object context

    public static final String REMOTE_CONTEXT = "http://vocab.lappsgrid.org/context-1.0.0.jsonld"

    //TODO Keeping the local context up to date is a never ending process.
    public static final Map LOCAL_CONTEXT = [
        '@vocab':'http://vocab.lappsgrid.org/',
        'meta':'http://vocab.lappsgrid.org/metadata/',
        'lif':'http://vocab.lappsgrid.org/interchange/',
        'types':'http://vocab.lappsgrid.org/types/',
        'metadata': 'meta:metadata',
        'contains': 'meta:contains',
        'producer': 'meta:producer',
        'url': ['@id':'meta:url', '@type':'@id'],
        'type':['@id':'meta:type', '@type':'@id'],
        'version':'meta:version',
        'text':'lif:text',
        'views': 'lif:views',
        'annotations': 'lif:annotations',
        'tokenization': 'types:tokenization/',
        'tagset': 'types:posType/',
        'ner': 'types:ner/',
        'coref': "types:coref/",
        'chunk': "types:chunk/",
        'lookup': "types:lookup/",
        'token': "http://vocab.lappsgrid.org/Token#",
        "common": "http://vocab.lappsgrid.org/Annotation#",
        "id":"common:id",
        "start":"common:start",
        "label":"common:label",
        "end":"common:end",
        "pos":"token:pos",
        "lemma":"token:lemma",
        "kind":"token:kind",
        "length":"token:length",
        "orth":"token:orth",
    ]

    /** Default (empty) constructor uses the remote context. */
    public Container() {
        this(ContextType.REMOTE)
    }

    public Container(Container container) {
        this.content = new Content(container.content)
        this.metadata = Utils.deepCopy(container.metadata)
        this.views = Utils.deepCopy(container.views)
        if (container.context instanceof String) {
            this.context = container.context
        }
        else {
            this.context = Utils.deepCopy((Map)container.context)
        }
    }

    protected Container(ContextType type) {
        content = new Content()
//        mapper = new ObjectMapper()
        metadata = new HashMap<String,Object>();
        views = new ArrayList<View>()
        if (type == ContextType.LOCAL) {
            context = LOCAL_CONTEXT
        }
        else {
            context = REMOTE_CONTEXT
        }
    }


    public Container(Map map) {
        initFromMap(map)
    }

    @JsonIgnore
    void setLanguage(String lang) {
        content.language = lang
    }

    @JsonIgnore
    String getLanguage() {
        return content.language
    }

    @JsonIgnore
    void setText(String text) {
        this.content.value = text
    }

    @JsonIgnore
    String getText() {
        return this.content.value
    }

    View newView(String id) {
        View view = newView()
        view.id = id
        return view
    }

    View newView() {
        View view = new View();
        views.add(view)
        return view
    }

    void addView(View view) {
        this.views << view
    }

    View getView(int index) {
        if (index >= 0 && index < views.size()) {
            return views[index]
        }
        return null
    }

    View findViewById(String id) {
        views.find { it.id == id }
    }

    List<View> findViewsThatContain(String type) {
        views.findAll { it?.metadata?.contains && it.metadata.contains[type] }
    }

    List<View> findViewsThatContainBy(String type, String producer) {
        views.findAll { it?.metadata?.contains && it.metadata.contains[type]?.producer == producer }
    }

    void setMetadata(String name, Object value) {
        this.metadata[name] = value
    }

    Object getMetadata(String name) {
        return this.metadata[name]
    }

    void define(String name, String iri) throws LappsIOException
    {
        if (!(this.context instanceof Map)) {
            throw new LappsIOException("Can not define a context field when a remote context is used.")
        }
        if (this.context[name]) {
            throw new LappsIOException("Context for ${name} already defined.")
        }
        this.context[name] = [
            '@id': iri,
            '@type': '@id'
        ]
    }

    private void initFromMap(Map map) {
        if (map == null) {
            return
        }
        if (map['@context']) {
            this.context = Utils.deepCopy(map['@context'])
        }
        else {
            this.context = Container.REMOTE_CONTEXT
        }

        this.content = new Content()//value:map.text.value, language:map.text.language)
        this.text = map.text['@value']
        this.language = map.text['@language']
        this.metadata = Utils.deepCopy(map.metadata)
//        this.views = Utils.deepCopy(map.views)
//        this.metadata = [:]
//        map.metadata.each { name, value ->
//            this.metadata[name] = value
//        }
        this.views = []
        map.views.each { v ->
//            View view = new View(v)
            this.views << new View(v)
        }
    }

}
