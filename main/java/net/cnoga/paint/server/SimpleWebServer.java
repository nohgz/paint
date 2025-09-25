package net.cnoga.paint.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javax.imageio.ImageIO;
import net.cnoga.paint.core.brews.WorkspaceBrew;
import net.cnoga.paint.core.workspace.Workspace;

/**
 * Minimal local HTTP server that exposes workspaces as PNG images.
 */
public class SimpleWebServer {

  private static WorkspaceBrew workspaceBrew;
  private HttpServer server;

  /**
   * Constructs the server and starts it.
   *
   * @param brew the WorkspaceBrew providing the workspaces
   * @throws IOException if the server cannot start
   */
  public SimpleWebServer(WorkspaceBrew brew) throws IOException {
    workspaceBrew = brew;
  }

  /** Starts the HTTP server on port 25565. */
  public void start() throws IOException {
    server = HttpServer.create(new InetSocketAddress(25565), 0);
    server.createContext("/", new IndexHandler());
    server.createContext("/workspace", new WorkspaceHandler(workspaceBrew.getWorkspaces()));
    server.setExecutor(null);
    server.start();
    System.out.println("[SimpleWebServerServer] running on: " + server.getAddress());
  }

  /**
   * Stops the HTTP server gracefully.
   *
   * @param delaySeconds the delay in seconds before forcibly closing existing connections
   */
  public void stop(int delaySeconds) {
    if (server != null) {
      server.stop(delaySeconds);
      System.out.println("[SimpleWebServer] Stopped.");
      server = null;
    }
  }

  /** Simple handler serving HTML with workspace links. */
  private static class IndexHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
      if (!"GET".equals(exchange.getRequestMethod())) {
        exchange.sendResponseHeaders(405, -1);
        return;
      }

      List<Workspace> workspaces = workspaceBrew.getWorkspaces();
      StringBuilder html = new StringBuilder("<html><body><h1>Workspaces</h1><ul>");
      for (int i = 0; i < workspaces.size(); i++) {
        html.append("<li><a href=\"/workspace/").append(i).append("\">Workspace ").append(i).append("</a></li>");
      }
      html.append("</ul></body></html>");

      byte[] bytes = html.toString().getBytes();
      exchange.getResponseHeaders().set("Content-Type", "text/html");
      exchange.sendResponseHeaders(200, bytes.length);
      try (OutputStream os = exchange.getResponseBody()) {
        os.write(bytes);
      }
    }
  }

  /**
   * Handler that returns a PNG snapshot of a workspace.
   */
    private record WorkspaceHandler(List<Workspace> workspaces) implements HttpHandler {

    @Override
      public void handle(HttpExchange exchange) throws IOException {
        String[] parts = exchange.getRequestURI().getPath().split("/");
        if (parts.length < 3) {
          exchange.sendResponseHeaders(400, -1);
          return;
        }

        int id;
        try {
          id = Integer.parseInt(parts[2]);
        } catch (NumberFormatException e) {
          exchange.sendResponseHeaders(400, -1);
          return;
        }

        if (id < 0 || id >= workspaces.size()) {
          exchange.sendResponseHeaders(404, -1);
          return;
        }

        File snapshot = snapshotWorkspace(workspaces.get(id).getBaseLayer());
        exchange.getResponseHeaders().set("Content-Type", Files.probeContentType(snapshot.toPath()));
        exchange.sendResponseHeaders(200, snapshot.length());

        try (FileInputStream fis = new FileInputStream(snapshot);
          OutputStream os = exchange.getResponseBody()) {
          byte[] buffer = new byte[1024];
          int read;
          while ((read = fis.read(buffer)) != -1) {
            os.write(buffer, 0, read);
          }
        }
      }

      /**
       * Captures a snapshot of a Canvas safely on the JavaFX Application Thread.
       */
      private File snapshotWorkspace(Canvas canvas) throws IOException {
        WritableImage fxImage = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
          SnapshotParameters params = new SnapshotParameters();
          params.setFill(Color.TRANSPARENT);
          canvas.snapshot(params, fxImage);
          latch.countDown();
        });

        try {
          latch.await();
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
          throw new IOException("Snapshot interrupted", e);
        }

        BufferedImage img = SwingFXUtils.fromFXImage(fxImage, null);
        File file = new File("workspace_snapshots", "workspace.png");
        file.getParentFile().mkdirs();
        ImageIO.write(img, "png", file);
        return file;
      }
    }
}
