package org.osehra.integration.core.composite;

import org.osehra.integration.core.component.Component;
import org.osehra.integration.core.router.Router;
import org.osehra.integration.core.service.ServiceInvoker;
import org.osehra.integration.core.splitter.Splitter;

/**
 * Composite processor interface. Use Composed Message Processor to process a
 * composite message. The Composed Message Processor splits the message up,
 * routes the sub-messages to the appropriate destinations and re-aggregates the
 * responses back into a single message. The Composed Message Processor uses an
 * Aggregator to reconcile the requests that were dispatched to the multiple
 * inventory systems. Each processing unit sends a response message to the
 * aggregator stating the inventory on hand for the specified item. The
 * Aggregator collects the individual responses and processes them based on a
 * predefined algorithm as described under Aggregator.
 * 
 * @author Julian Jewel
 * @param <T>
 *            Usually java.lang.Object
 */
public interface CompositeProcessor<E, T> extends ServiceInvoker<E, T>,
		Router<E, T> {

	/**
	 * Get the component to invoke.
	 * 
	 * @return the component reference
	 */
	Component<E, T> getEndpoint();

	/**
	 * Get the splitter to use to split the input.
	 * 
	 * @return the splitter
	 */
	Splitter<E, T> getSplitter();
}
