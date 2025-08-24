package net.cnoga.paint.interfaces;

import net.cnoga.paint.services.ProgramIOService;

public interface ProgramIOAware {
  // Should be tied to the stage.
  void setProgramIOService(ProgramIOService programIOService);
}