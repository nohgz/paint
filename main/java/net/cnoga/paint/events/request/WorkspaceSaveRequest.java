package net.cnoga.paint.events.request;

import net.cnoga.paint.service.FileIOService;
import net.cnoga.paint.service.WorkspaceService;

/**
 * Event signaling that the current workspace should be saved to its
 * associated file location.
 * <p>
 * Typically posted by the {@link FileIOService} when "Save" is chosen.
 * <p>
 * If no file is currently associated, a {@link WorkspaceSaveAsRequest} should be
 * triggered instead.
 * <p>
 * See {@link WorkspaceService}
 * for an implementation of a listener implementing this logic.
 *
 * @author cnoga
 * @version 1.0
 */
public record WorkspaceSaveRequest() {

}
