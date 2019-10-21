package com.kyoo.pixel.connection.components;

import com.kyoo.pixel.connection.components.impl.Led;

public interface LedComponent extends ConnectionComponent {

  Led getFirstLed();

  Led getLastLed();
}
