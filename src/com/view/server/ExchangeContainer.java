package com.view.server;

import java.io.Serializable;
import java.util.List;

/**
 * The record Exchange container.
 *
 * This record is used to transmit data through a ObjectStream via a standardized object.
 */
record ExchangeContainer(Command command, String name1, String name2, List<?> list, Integer number,
						 Boolean isWitch) implements Serializable {

	/**
	 * Return the active/passive counterpart of the container.
	 *
	 * @return the active/passive counterpart of the container
	 */
	public ExchangeContainer setActive() {
		return new ExchangeContainer(
				command.switchPA(),
				name1,
				name2,
				list,
				number,
				isWitch
		);
	}
}
