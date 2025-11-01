package net.cnoga.paint.core.bus.events.request;

/** Event requesting a theme change.
 * @param theme the filepath of the theme
 */
public record ChangeThemeRequest(String theme) {

}
