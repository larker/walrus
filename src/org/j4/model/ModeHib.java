/**
 * 
 */
package org.j4.model;

import java.io.Serializable;

import org.j4.model.Rubric.Mode;
import org.j4.utils.EnumUserType;

/**
 * Helper class to assist saving of mode enumeration to Hibernate persistency
 * layer
 */
public class ModeHib extends EnumUserType<Mode> implements Serializable{ 
	private static final long serialVersionUID = -6819654353402720453L;

	public ModeHib() {
		super(Mode.class);
	}
}