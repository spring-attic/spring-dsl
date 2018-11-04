package org.springframework.dsl.service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import reactor.core.Disposable;

public class ListenerList<T> {

	private List<Consumer<T>> listeners = new ArrayList<>();

	public synchronized void fire(T evt) {
		for (Consumer<T> l : listeners) {
			l.accept(evt);
		}
	}

	public synchronized Disposable add(Consumer<T> l) {
		listeners.add(l);
		return () -> {
			synchronized (this) {
				listeners.remove(l);
			}
		};
	}

}
