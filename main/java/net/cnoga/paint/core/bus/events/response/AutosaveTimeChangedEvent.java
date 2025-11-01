package net.cnoga.paint.core.bus.events.response;

/** Event indicating the autosave timer has changed.
 * @param seconds the new autosave time in seconds
 */
public record AutosaveTimeChangedEvent(int seconds) {

}
