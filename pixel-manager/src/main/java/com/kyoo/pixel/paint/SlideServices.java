package com.kyoo.pixel.paint;

import com.kyoo.pixel.paint.service.*;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public final class SlideServices {
  private ComponentService componentService;
  private DisplayService displayService;
  private InteractionService interactionService;
  private InteractiveComponentService interactiveComponentService;
  private IOService ioService;
  private PointerService pointerService;
  private PropertyService propertyService;
  private SelectionService selectionService;
  private StateService stateService;
}
