package net.cnoga.paint.core.bus.events.request;

/** Sets the autosave interval.
 * @param minutes the autosave interval in minutes
 */
public record SetAutosaveIntervalRequest(Integer minutes) {

}
