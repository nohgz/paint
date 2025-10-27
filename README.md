# Pain(t)

**Pain(t)** is a JavaFX-based paint application inspired by Minecraft Forge-style event handling.
It provides a flexible canvas environment for image editing, file I/O, and experimentation with
custom workspaces.

---

## Overview

Pain(t) is built with JavaFX and emphasizes a clean, modular architecture.
By leveraging an internal event bus, the application decouples UI interactions from backend logic,
making it easy to extend and maintain.

Whether you want to sketch, edit images, or even extend it custom features, Pain(t) keeps everything
organized and responsive.

---

## Key Features

- **Canvas rendering** – Supports basic drawing, though JavaFX snapshotting can produce minor visual
  artifacts.
- **Pannable workspaces** – Navigate large canvases effortlessly.
- **Multi-file support** – Create new canvases, open existing images, save progress, or perform
  “Save As” operations.
- **Shortcut bar** – Quickly access tools and actions.
- **Event-driven architecture** – Built on a flexible event bus for easy integration of new tools
  and features.
- **Modular UI components** – Separate controllers for workspace, tool info, and status display
  ensure a clean interface.

---

## Architecture

- **Workspace Brew** – Central dispatcher that routes user actions to the active workspace.
- **Capabilities** – Modular units like **Undo/Redo** and **Selection** that extend workspace
  functionality.
- **Event Bus** – Decouples UI events from backend logic, enabling flexible and extensible
  interactions.