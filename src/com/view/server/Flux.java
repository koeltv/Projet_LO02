package com.view.server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public record Flux(ObjectOutputStream output, ObjectInputStream input) {
}
