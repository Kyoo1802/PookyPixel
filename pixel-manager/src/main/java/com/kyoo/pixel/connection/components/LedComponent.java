package com.kyoo.pixel.connection.components;

import com.kyoo.pixel.connection.components.impl.Led;
import java.util.Collection;

public interface LedComponent extends ConnectionComponent {

  Led getFirstLed();

  Led getLastLed();

  Collection<Led> getLeds();
}
