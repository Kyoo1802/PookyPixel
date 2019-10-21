package com.kyoo.pixel.connection.components;

import java.util.Optional;

public interface LedComponent extends ConnectionComponent {

  Led getFirstLed();

  Led getLastLed();
}
