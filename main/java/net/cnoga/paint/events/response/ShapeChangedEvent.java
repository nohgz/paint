package net.cnoga.paint.events.response;

import net.cnoga.paint.util.ShapeType;

public record ShapeChangedEvent(ShapeType shapeType, Integer sides) {

}
