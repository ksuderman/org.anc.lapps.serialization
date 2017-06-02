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

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * The Content object is a JSON "value object", that is, a JSON object with a {@literal @}value
 * field. The optional {@literal @}language field is also included.
 * <p>
 * <b>NOTE</b> Users will typically not use this class directly. Instead users will use
 * the text and language getters and setters on the {@link Container} object.
 * <pre>
 *     <code>
 *         contaner.setText("Hello world");
 *         container.setLanguage("en");
 *     </code>
 * </pre>
 *
 * @author Keith Suderman
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Content {
    @JsonProperty('@value')
    String value
    @JsonProperty('@language')
    String language

    public Content() { }

    public Content(Content content) {
        this.value = content.value
        this.language = content.language
    }

//    public Content(Map map) {
//        this.value = map['value']
//        this.language = map['language']
//    }
}
