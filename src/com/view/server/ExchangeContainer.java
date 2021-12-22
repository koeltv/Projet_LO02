package com.view.server;

import com.model.card.CardName;

import java.io.Serializable;
import java.util.List;

public class ExchangeContainer implements Serializable {
	ChangedState state;

	String name;
	String name2;
	List<?> list;
	Integer nb;
	CardName cardName;
	Boolean isWitch;

	ExchangeContainer(ChangedState state) {
		this.state = state;
	}

	ExchangeContainer(ChangedState state, int nb) {
		this.state = state;
		this.nb = nb;
	}

	ExchangeContainer(ChangedState state, String name) {
		this.state = state;
		this.name = name;
	}

	ExchangeContainer(ChangedState state, String name, int nb) {
		this.state = state;
		this.name = name;
		this.nb = nb;
	}

	ExchangeContainer(ChangedState state, String name, CardName cardName) {
		this.state = state;
		this.name = name;
		this.cardName = cardName;
	}

	ExchangeContainer(ChangedState state, String name, boolean isWitch) {
		this.state = state;
		this.name = name;
		this.isWitch = isWitch;
	}

	ExchangeContainer(ChangedState state, String name, String name2) {
		this.state = state;
		this.name = name;
		this.name2 = name2;
	}

	ExchangeContainer(ChangedState state, String name, List<?> list) {
		this.state = state;
		this.name = name;
		this.list = list;
	}

	public ExchangeContainer copy() {
		ExchangeContainer result = new ExchangeContainer(state);
		result.list = list;
		result.name = name;
		result.name2 = name2;
		result.isWitch = isWitch;
		result.cardName = cardName;
		result.nb = nb;
		return result;
	}

	public ExchangeContainer setActive() {
		ExchangeContainer activeContainer = copy();
		activeContainer.state = activeContainer.state.switchPA();
		return activeContainer;
	}
}
