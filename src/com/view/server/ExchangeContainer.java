package com.view.server;

import com.model.card.CardName;

import java.io.Serializable;
import java.util.List;

public class ExchangeContainer implements Serializable {
	final Command state;
	final String name;
	final String name2;
	final List<?> list;
	final Integer nb;
	final CardName cardName;
	final Boolean isWitch;

	ExchangeContainer(Command state) {
		this.state = state;
		isWitch = null;
		name = null;
		name2 = null;
		list = null;
		nb = null;
		cardName = null;
	}

	ExchangeContainer(Command state, int nb) {
		this.state = state;
		this.nb = nb;
		isWitch = null;
		name = null;
		name2 = null;
		list = null;
		cardName = null;
	}

	ExchangeContainer(Command state, String name) {
		this.state = state;
		this.name = name;
		isWitch = null;
		name2 = null;
		list = null;
		nb = null;
		cardName = null;
	}

	ExchangeContainer(Command state, String name, int nb) {
		this.state = state;
		this.name = name;
		this.nb = nb;
		isWitch = null;
		name2 = null;
		list = null;
		cardName = null;
	}

	ExchangeContainer(Command state, String name, CardName cardName) {
		this.state = state;
		this.name = name;
		this.cardName = cardName;
		isWitch = null;
		name2 = null;
		list = null;
		nb = null;
	}

	ExchangeContainer(Command state, String name, boolean isWitch) {
		this.state = state;
		this.name = name;
		this.isWitch = isWitch;
		name2 = null;
		list = null;
		nb = null;
		cardName = null;
	}

	ExchangeContainer(Command state, String name, String name2) {
		this.state = state;
		this.name = name;
		this.name2 = name2;
		isWitch = null;
		list = null;
		nb = null;
		cardName = null;
	}

	ExchangeContainer(Command state, String name, List<?> list) {
		this.state = state;
		this.name = name;
		this.list = list;
		isWitch = null;
		name2 = null;
		nb = null;
		cardName = null;
	}

	ExchangeContainer(Command state, String name, String name2, List<?> list, Integer nb, CardName cardName, Boolean isWitch) {
		this.state = state;
		this.name = name;
		this.name2 = name2;
		this.list = list;
		this.nb = nb;
		this.cardName = cardName;
		this.isWitch = isWitch;
	}

	public ExchangeContainer setActive() {
		return new ExchangeContainer(
				state.switchPA(),
				name,
				name2,
				list,
				nb,
				cardName,
				isWitch
		);
	}
}
