package net.cnoga.paint.events.response;

import net.cnoga.paint.tool.shape.ShapeType;

public record ShapeChangedEvent(ShapeType shapeType, Integer sides) {

}
