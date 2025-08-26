package net.cnoga.paint.controllers;

import net.cnoga.paint.services.FileIOService;

public abstract class AbstractIOProvider {
  protected FileIOService fileIOService;

  public void setFileIOService(FileIOService fileIOService) {
    this.fileIOService = fileIOService;
  }
}