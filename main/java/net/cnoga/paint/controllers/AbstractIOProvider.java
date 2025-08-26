package net.cnoga.paint.controllers;

import net.cnoga.paint.services.FileIOService;

public abstract class AbstractIOProvider {
  protected FileIOService fileIOService;

  public void initFileIOService(FileIOService fileIOService) {
    this.fileIOService = fileIOService;
  }
}