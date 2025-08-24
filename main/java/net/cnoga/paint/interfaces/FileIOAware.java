package net.cnoga.paint.interfaces;

import net.cnoga.paint.services.FileIOService;

public interface FileIOAware {
  // This will likely need a second stage to be initialized
  void setFileIOService(FileIOService fileIOService);
}
