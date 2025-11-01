/**
 * Manages paint workspaces and related capabilities.
 *
 * <p>
 * A {@code Workspace} represents a single drawing area with a scrollable view,
 * zooming, and undo/redo support.
 * </p>
 *
 * <p>
 * Workspaces interact with the centralized {@link net.cnoga.paint.core.bus.EventBus} to handle
 * user actions and application requests.
 * </p>
 */
package net.cnoga.paint.core.workspace;