package com.view.server;

import java.io.Serializable;
import java.util.List;

public class ExchangeContainer implements Serializable {
	final Command state;
	final String name;
	final String name2;
	final List<?> list;
	final Integer nb;
	final Boolean isWitch;

	ExchangeContainer(Command state) {
		this.state = state;
		isWitch = null;
		name = null;
		name2 = null;
		list = null;
		nb = null;
	}

	ExchangeContainer(Command state, int nb) {
		this.state = state;
		this.nb = nb;
		isWitch = null;
		name = null;
		name2 = null;
		list = null;
	}

	ExchangeContainer(Command state, String name) {
		this.state = state;
		this.name = name;
		isWitch = state == Command.SHOW_PLAYER_IDENTITY ? true : null;
		name2 = null;
		list = null;
		nb = null;
	}

	ExchangeContainer(Command state, String name, int nb) {
		this.state = state;
		this.name = name;
		this.nb = nb;
		isWitch = null;
		name2 = null;
		list = null;
	}

	ExchangeContainer(Command state, String name, String name2) {
		this.state = state;
		this.name = name;
		this.name2 = name2;
		isWitch = null;
		list = null;
		nb = null;
	}

	ExchangeContainer(Command state, String name, List<?> list) {
		this.state = state;
		this.name = name;
		this.list = list;
		isWitch = null;
		name2 = null;
		nb = null;
	}

	private ExchangeContainer(Command state, String name, String name2, List<?> list, Integer nb, Boolean isWitch) {
		this.state = state;
		this.name = name;
		this.name2 = name2;
		this.list = list;
		this.nb = nb;
		this.isWitch = isWitch;
	}

	public ExchangeContainer setActive() {
		return new ExchangeContainer(
				state.switchPA(),
				name,
				name2,
				list,
				nb,
				isWitch
		);
	}
}
