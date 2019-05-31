package org.lappsgrid.serialization

import org.lappsgrid.serialization.lif.Container
import static org.lappsgrid.discriminator.Discriminators.Uri;

/**
 * The DataContainer class extends Data<Container> so that we can pass
 * Class instances.  For example, JSON serialization requires a class instances
 * but Java does not allow us to write Data<Container>.class, the best be can do is
 * Data.class which loses the fact that the Data object contains a Container object
 * as its payload.
 * <p>
 * Since the payload is always a Container we can reliably set the discriminator
 * to Uri.LAPPS as well.
 *
 * @author Keith Suderman
 */
class DataContainer extends Data<Container> {
    DataContainer() {
        this.discriminator = Uri.LAPPS
        this.parameters = [:]
    }

    DataContainer(Container payload) {
        this.discriminator = Uri.LAPPS
        this.payload = payload;
        this.parameters = [:]
    }

    DataContainer(Map map) {
        this.discriminator = Uri.LAPPS
        this.payload = map.payload
        this.parameters = map.parameters
    }
}
