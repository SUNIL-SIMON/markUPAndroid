package com.example.plansheets.Sheet

import android.graphics.Point
import android.graphics.PointF
import android.graphics.drawable.Drawable
import android.util.Size
import com.example.plansheets.ShapeMarkerView

public enum class MarkerStateType
{
    DRAFT,
    PUBLISHED
}
public enum class MarkerPermissionType
{
    EDITABLE,
    VIEWABLE
}
public enum class MarkerMenuType
{
    DELETE,
    COPY,
    PASTE,
    INFO,
    ADDIMAGE
}
public data class MarkerMenuOptionsType(
    var option : MarkerMenuType,
    var string : String
)
public data class MarkedGroupCellType(
    var expanded : Boolean,
    var typeStr : String,
    var allHidden : Boolean,
    var allLocked : Boolean,
    var title : String,
    var permissionType : MarkerPermissionType,
    var state : MarkerStateType,
    var markers : MutableList<ShapeMarkerView>
)
public enum class PLMShapeType
{
    unknown,
    RECT,
    ROUND,
    TRIANGLE,
    ARCS,
    ARROW,
    LINE,
    IMAGE,
    TEXT,
    PATH,
    HIGHLIGHTER,
    RULLER
}
public data class MarkedInfoType (
    var thisMarkerID : String,
    var type : PLMShapeType,
    var drawableDetails : DrawableMarkerInfoType,
    var originalDetails : OriginalMarkerInfoType,
    var text : String,
    var image : ImageMarkerType,
    var measurmnetsVisible : MeasurmentsVisibleType,
    var isLocked : Boolean,
    var groupTitle : String,
    var createdBy : String,
    var location : String,
    var comments : String,
    var createdON : String,
    )
public data class DrawableMarkerInfoType (
    var drawableframeRect : Frame,
    var drawableStrokeWidth : Float,
    var drawableStrokeColor : String,
    var drawableFillColor : String,
    var drawableAngle : Float,
    var drawableLayerPoints : MutableList<PointF>,
    var drawableRullerPosition : LinePointsType
)
public data class OriginalMarkerInfoType (
    var originalFrameRect : Frame,
    var originalStrokeWidth : Float,
    var originalStrokeColor : String,
    var originalFillColor : String,
    var originalAngle : Float,
    var originalLayerPoints : MutableList<PointF>,
    var originalLayerPath : String,
    var originalRullerPosition : LinePointsType
)
public data class LinePointsType(
    var point1 : Point,
    var point2 : Point
)
public data class Frame(
    var origin : PointF,
    var size : Size
)
public data class ImageMarkerType(

    var images : MutableList<Drawable>,
    var urlID : String
)
public data class MeasurmentsVisibleType(
    var areaShown : Boolean,
    var lengthShown : Boolean,
    var breadthShown : Boolean
)