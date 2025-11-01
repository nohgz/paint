/**
 * Contains all "Brews" for the Paint application.
 *
 * <p>Brews are application-level services that subscribe to the EventBus
 * and handle specific responsibilities such as:</p>
 *
 * <ul>
 *   <li>Managing autosave functionality ({@link net.cnoga.paint.core.brews.AutosaveBrew})</li>
 *   <li>Providing file IO operations ({@link net.cnoga.paint.core.brews.FileIOBrew})</li>
 *   <li>Managing global keyboard shortcuts ({@link net.cnoga.paint.core.brews.KeystrokeBrew})</li>
 *   <li>Logging events asynchronously ({@link net.cnoga.paint.core.brews.LoggerBrew})</li>
 *   <li>Program lifecycle management ({@link net.cnoga.paint.core.brews.ProgramBrew})</li>
 *   <li>Running a simple web server ({@link net.cnoga.paint.core.brews.SimpleWebServerBrew})</li>
 *   <li>Subwindow creation and management ({@link net.cnoga.paint.core.brews.SubWindowBrew})</li>
 *   <li>Workspace lifecycle and canvas handling ({@link net.cnoga.paint.core.brews.WorkspaceBrew})</li>
 * </ul>
 *
 * <p>Each Brew acts as an EventBusSubscriber and is responsible for translating
 * events into actions</p>
 */

package net.cnoga.paint.core.brews;