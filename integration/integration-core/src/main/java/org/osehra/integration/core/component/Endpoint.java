package org.osehra.integration.core.component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.beans.factory.annotation.Qualifier;

/**
 * A qualifier for resolving multiple component spring beans.
 * 
 * @author Julian Jewel
 */
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Qualifier
public @interface Endpoint {
	/**
	 * Get the value.
	 */
	String value();
}
