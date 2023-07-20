package com.example.plansheets.Sheet


import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.graphics.*
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import android.os.Handler
import android.util.DisplayMetrics
import android.util.Log
import android.util.Size
import android.util.SizeF
import android.view.MotionEvent
import android.view.View
import android.view.ViewManager
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.example.plansheets.DrawMarkers.PathMarkerView
import com.example.plansheets.ImageMasterView
import com.example.plansheets.LineMarkers.LineMarkerView
import com.example.plansheets.R
import com.example.plansheets.ShapeMarkers.EllipseMarkerView
import com.example.plansheets.ShapeMarkers.RectMarkerView
import com.example.plansheets.ShapeMarkers.TriangleMarkerView
import java.util.*
import kotlin.math.roundToInt


interface SheetBaseViewInterface {
    fun deleteMarker()
    fun toastMarker()
    fun selectedMarker(id : String)
    fun getLayoutParam():Size
    fun getMarkerLayerLayoutParam():Size
    fun getImageSize():SizeF
    fun performOrientation(context : Context)
    fun reConfigureMarkerLayer()
    fun isSideLayerExpanded():Boolean
    fun toggleSideLayerExpansion()
    fun selectedMarkerToBeDroped(type: PLMShapeType)
    fun drawMarker(mMouseEvent: MotionEvent)
}

open class SheetBaseView(private val activity: Activity, context: Context) : AppCompatButton(context),
    SheetBaseViewInterface {
    val holderSize = 40
    val shapeDefaultSize = 200
    val sheetBaseView = FrameLayout(context)
    lateinit var imageView : ImageMasterView
    val markerLayersView = FrameLayout(context)
    val controlBarView = ControlBarView(this,context)
    val sideLayerView = SideLayerView(this,context)
    var markerGroups : MutableList<MarkedGroupCellType> =  ArrayList()
    var selectedGroupIndex = 0
    var selectedMarkerIndex = -1
    var sideLayerExpanded = true
    var markerToBeDroped = PLMShapeType.unknown
    open fun createSheetBaseView(context: Context):View?
    {
        createImageMasterView(context)
        createMarkerLayer(context)
        createControlBar(context)
        createSideLayerView()
        return sheetBaseView
    }
    //CREATE----------------------------------------------
    fun createImageMasterView(context: Context) {

        imageView = ImageMasterView(this,context)
        sheetBaseView.addView(imageView)
        imageView.setImageResource(R.drawable.plan);
//        imageView.setBackgroundColor(Color.YELLOW)
        imageView.constructionDetails(context)

        val handler = Handler()
        handler.postDelayed({
            reConfigureMarkerLayer()
        }, 100)


    }
    fun createMarkerLayer(context: Context) {

        sheetBaseView.addView(markerLayersView)
        val markerLayersViewdrawable = ShapeDrawable()
        markerLayersViewdrawable.shape = RectShape()
        markerLayersViewdrawable.paint.color = Color.RED
        markerLayersViewdrawable.paint.strokeWidth = 10f
        markerLayersViewdrawable.paint.style = Paint.Style.STROKE
        markerLayersView.setBackground(markerLayersViewdrawable)
        markerLayersView.isClickable = false
        markerLayersView.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                selectedMarker("")
                if (event != null) {
                    val touchX = event?.getX()//.toInt()
                    val touchY = event?.getY()//.toInt()
                    if (markerToBeDroped == PLMShapeType.PATH) {

                    }
                    else {
                        addMarker(markerToBeDroped, touchX, touchY)
                        markerToBeDroped = PLMShapeType.unknown
                    }
                }
                return false
            }
        })
    }
    fun createControlBar(context: Context)
    {
        markerGroups.add(
            MarkedGroupCellType(
            true,"yoyo",false,false,"draft", MarkerPermissionType.EDITABLE, MarkerStateType.DRAFT,
            ArrayList()
        )
        )
        val v = controlBarView.getControlBarView(context)
        sheetBaseView.addView(v)
        controlBarView.setBackgroundColor(Color.TRANSPARENT)
    }
    fun createSideLayerView()
    {
        val v = sideLayerView.getSideLayerView(context)
        sheetBaseView.addView(v)
        sideLayerView.sideLayerView.setBackgroundColor(Color.WHITE)
        toggleSideLayerExpansion()
    }
    //ADDINGMARKER----------------------------------------------
    fun addMarker(type : PLMShapeType, x : Float, y : Float) {
        if(type == PLMShapeType.unknown)
        {
            return
        }
        val o1 = PointF(x - (shapeDefaultSize/2), y - (shapeDefaultSize/2))
        val o = PointF(o1.x, o1.y)
        var s = Size(shapeDefaultSize, shapeDefaultSize)
        val o2 = PointF(o1.x + (holderSize/2), o1.y + (holderSize/2))
        var s2 = Size(shapeDefaultSize - (holderSize), shapeDefaultSize - (holderSize))
        if(type == PLMShapeType.RECT){
            val rectMarkerView = RectMarkerView(this, context)
            rectMarkerView.createShapeMarker(o, s)
            rectMarkerView.setUpShapeMarkerMoveGesture()
            rectMarkerView.setUpClickActions()
            val view = rectMarkerView.getShapeMarker(context)
            var markerSet = markerGroups[0]
            markerSet.markers.add(rectMarkerView)
            rectMarkerView.markedInfos.thisMarkerID = UUID.randomUUID().toString()
            rectMarkerView.markedInfos.drawableDetails.drawableframeRect = Frame(o2, s2)
            rectMarkerView.markedInfos.type = PLMShapeType.RECT
            markerLayersView.addView(view)
        }
        if(type == PLMShapeType.ROUND){
            val ellipseMarkerView = EllipseMarkerView(this,context)
            ellipseMarkerView.createShapeMarker(o,s)
            ellipseMarkerView.setUpShapeMarkerMoveGesture()
            ellipseMarkerView.setUpClickActions()
            val view = ellipseMarkerView.getShapeMarker(context)
            var markerSet = markerGroups[0]
            markerSet.markers.add(ellipseMarkerView)
            ellipseMarkerView.markedInfos.thisMarkerID = UUID.randomUUID().toString()
            ellipseMarkerView.markedInfos.drawableDetails.drawableframeRect = Frame(o2, s2)
            ellipseMarkerView.markedInfos.type = PLMShapeType.ROUND
            markerLayersView.addView(view)
        }
        if(type == PLMShapeType.TRIANGLE) {
            val triangleMarkerView = TriangleMarkerView(this,context)
            triangleMarkerView.createShapeMarker(o,s)
            triangleMarkerView.setUpShapeMarkerMoveGesture()
            triangleMarkerView.setUpClickActions()
            val view = triangleMarkerView.getShapeMarker(context)
            var markerSet = markerGroups[0]
            markerSet.markers.add(triangleMarkerView)
            triangleMarkerView.markedInfos.thisMarkerID = UUID.randomUUID().toString()
            triangleMarkerView.markedInfos.drawableDetails.drawableframeRect = Frame(o2, s2)
            triangleMarkerView.markedInfos.type = PLMShapeType.TRIANGLE
            markerLayersView.addView(view)
        }
        if(type == PLMShapeType.LINE){
            val lineMarkerView = LineMarkerView(this,context)
            lineMarkerView.createShapeMarker(PointF(0f,0f),Size(markerLayersView.layoutParams.width,markerLayersView.layoutParams.height))
            val view = lineMarkerView.getShapeMarker(context)
            var markerSet = markerGroups[0]
            markerSet.markers.add(lineMarkerView)
            lineMarkerView.point1Holder.x = o1.x
            lineMarkerView.point1Holder.y = o1.y
            lineMarkerView.point2Holder.x = o1.x + 100f
            lineMarkerView.point2Holder.y = o1.y + 100f
            val p = LinePointsType(Point(o1.x.toInt(),o1.y.toInt()),Point(o1.x.toInt() + 100,o1.y.toInt() + 100))
            lineMarkerView.markedInfos.drawableDetails.drawableRullerPosition = p
            lineMarkerView.setCanvas()
            lineMarkerView.markedInfos.thisMarkerID = UUID.randomUUID().toString()
            lineMarkerView.markedInfos.type = PLMShapeType.LINE
            markerLayersView.addView(view)
        }

        reconfigureLastMarker()
        markerToBeDroped = PLMShapeType.unknown
        sideLayerView.markerGroups = markerGroups
        sideLayerView.reloadTableView()
    }
    override fun drawMarker(mMouseEvent: MotionEvent) {
        val type = markerToBeDroped
        if(type == PLMShapeType.unknown)
        {
            return
        }
        val x = mMouseEvent.x - markerLayersView.x
        val y = mMouseEvent.y - markerLayersView.y
        val o1 = PointF(x , y )
        if(type == PLMShapeType.PATH) {
            when (mMouseEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    val pathMarkerView = PathMarkerView(this,context)
                    pathMarkerView.createShapeMarker(PointF(0f,0f),Size(markerLayersView.layoutParams.width,markerLayersView.layoutParams.height))
                    pathMarkerView.setUpShapeMarkerMoveGesture()
                    pathMarkerView.setUpClickActions()
                    val view = pathMarkerView.getShapeMarker(context)
                    var markerSet = markerGroups[0]
                    markerSet.markers.add(pathMarkerView)

                    var pts : MutableList<PointF> = ArrayList()
                    pts.add(PointF(o1.x,o1.y))
                    pathMarkerView.setCanvasDrawable(pts)
                    pathMarkerView.markedInfos.thisMarkerID = UUID.randomUUID().toString()
                    pathMarkerView.markedInfos.type = PLMShapeType.PATH
                    pathMarkerView.markedInfos.drawableDetails.drawableLayerPoints = pts
                    markerLayersView.addView(view)
                }
                MotionEvent.ACTION_MOVE -> {
                    if(markerGroups.size > 0) {
                        val i = markerGroups.size - 1
                        if(markerGroups[i].markers.size > 0) {
                            val j = markerGroups[i].markers.size - 1
                            val pmv = markerGroups[i].markers[j] as PathMarkerView
                            if(pmv != null){
                                var pts = pmv.markedInfos.drawableDetails.drawableLayerPoints
                                pts.add(PointF(o1.x,o1.y))
                                pmv.setCanvasDrawable(pts)
                            }
                        }
                    }
                }
                MotionEvent.ACTION_UP -> {
                    if(markerGroups.size > 0) {
                        val i = markerGroups.size - 1
                        if(markerGroups[i].markers.size > 0) {
                            val j = markerGroups[i].markers.size - 1
                            val pmv = markerGroups[i].markers[j] as PathMarkerView
                            if(pmv != null){
                                var pts = pmv.markedInfos.drawableDetails.drawableLayerPoints
                                var pts2 : MutableList<PointF> = ArrayList()
                                var pts3 : MutableList<PointF> = ArrayList()
                                var leftx = pts[0].x
                                var lefty = pts[0].y
                                var rightx = pts[0].x
                                var righty = pts[0].y
                                for(point in pts) {
                                    if(leftx > point.x)
                                    {
                                        leftx = point.x
                                    }
                                    if(lefty > point.y)
                                    {
                                        lefty = point.y
                                    }
                                    if(rightx < point.x)
                                    {
                                        rightx = point.x
                                    }
                                    if(righty < point.y)
                                    {
                                        righty = point.y
                                    }
                                }
                                val o = PointF(leftx, lefty)
                                var s = Size(rightx.toInt() - leftx.toInt(), righty.toInt() - lefty.toInt())
                                for(point in pts)
                                {
                                    val xpt = point.x - leftx
                                    val ypt = point.y - lefty
                                    pts2.add(PointF(xpt,ypt))
                                    pts3.add(PointF(xpt/s.width,ypt/s.height))
                                }
                                pmv.markedInfos.drawableDetails.drawableLayerPoints = pts2
                                pmv.markedInfos.originalDetails.originalLayerPoints = pts3
                                pmv.markedInfos.drawableDetails.drawableframeRect = Frame(o, s)

                                pmv.shapeMarkerView.setLayoutParams(FrameLayout.LayoutParams(s.width+holderSize,s.height+holderSize))
                                pmv.shapeMarkerView.x = o.x-(holderSize/2).toFloat()
                                pmv.shapeMarkerView.y = o.y-(holderSize/2).toFloat()
                                pmv.reconfigureRect()
                                reconfigureLastMarker()
                                pmv.reconfigureHolders()
                                pmv.reconfigureSelection()
                            }
                        }
                    }
                    markerToBeDroped = PLMShapeType.unknown
                    sideLayerView.markerGroups = markerGroups
                    sideLayerView.reloadTableView()
                }
            }
        }
    }
    //OPERATIONS---------------------------------------
    override fun deleteMarker() {
        if(markerGroups.size > selectedGroupIndex){
            if(markerGroups[selectedGroupIndex].markers.size > selectedMarkerIndex){
                val v = markerGroups[selectedGroupIndex].markers[selectedMarkerIndex]
                (v.shapeMarkerView.getParent() as ViewManager).removeView(v.shapeMarkerView)
                markerGroups[selectedGroupIndex].markers.removeAt(selectedMarkerIndex)
            }
        }
    }
    override fun selectedMarker(id : String)
    {
        if(markerGroups.size > 0){
            for (i in 0..markerGroups.size - 1) {
                if(markerGroups[i].markers.size > 0){
                    for (j in 0..markerGroups[i].markers.size - 1) {
                        markerGroups[i].markers[j].enableHolder(false)
                        if (markerGroups[i].markers[j].markedInfos.thisMarkerID == id) {
                            this.selectedGroupIndex = i
                            this.selectedMarkerIndex = j
                        }
                    }
                }
            }
        }
        if(id != "") {
            markerGroups[selectedGroupIndex].markers[selectedMarkerIndex].enableHolder(true)
        }
    }
    //VIEW MODIFY---------------------------------------
    override fun performOrientation(context: Context)
    {
        val ims = getImageSize(context)
        val orientation = resources.configuration.orientation
        val metrics = DisplayMetrics()
        activity.windowManager?.defaultDisplay?.getMetrics(metrics)
        val height = metrics.heightPixels
        val width = metrics.widthPixels
        val g = if (height > width)  height else width
        val s = if (height < width)  height else width
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            sheetBaseView.setLayoutParams(FrameLayout.LayoutParams(g, s))
            val w = g
            val h = s
            val si = (ims.width/ims.height)*h
            sideLayerView.sideLayerView.x = 10f
            sideLayerView.sideLayerView.y = h.toFloat() * 0.2.toFloat()
            controlBarView.performOrientation(g,s)
        } else {
            sheetBaseView.setLayoutParams(FrameLayout.LayoutParams(s, g))
            val w = s
            val h = g
            sideLayerView.sideLayerView.x = 10f
            sideLayerView.sideLayerView.y = h.toFloat() * 0.2.toFloat()
            controlBarView.performOrientation(s,g)
        }

        reConfigureImageView()
        reConfigureMarkerLayer()

    }
    fun reConfigureImageView()
    {
        var w = getLayoutParam().width
        var h = getLayoutParam().height
        val mLayoutParams = FrameLayout.LayoutParams(w, h)
        imageView.x = 0f
        imageView.y = 0f
        imageView.layoutParams = mLayoutParams
    }
    override fun reConfigureMarkerLayer()
    {
        val f = FloatArray(9)
        imageView.imageMatrix.getValues(f)
        val scaleX = f[Matrix.MSCALE_X]
        val scaleY = f[Matrix.MSCALE_Y]

        val w = (imageView.getDrawable().intrinsicWidth * scaleX).roundToInt()
        val h = (imageView.getDrawable().intrinsicHeight * scaleY).roundToInt()
        markerLayersView.setLayoutParams(FrameLayout.LayoutParams(w, h))

        val imc = getImageViewContentOrigin()
        markerLayersView.x = imageView.x + imc.x
        markerLayersView.y = imageView.y + imc.y

        Log.d("scale scroll = " + scaleX + " " + scaleY," " + imageView.width + " " + imageView.height + " " + imageView.layoutParams.width + " " + imageView.layoutParams.height)
        reConfigureAllMarkers()
    }
    fun reConfigureAllMarkers()
    {
        for (i in 0..markerGroups.size - 1) {
            for (j in 0..markerGroups[i].markers.size - 1) {
                markerGroups[i].markers[j].enableHolder(false)
                val mi = markerGroups[i].markers[j].markedInfos
                val type = mi.type
                if (type == PLMShapeType.RECT || type == PLMShapeType.ROUND || type == PLMShapeType.TRIANGLE || type == PLMShapeType.ARCS) {
                    val ims = getImageSize(context)
                    val ills = markerLayersView.layoutParams
                    if(mi.originalDetails.originalFrameRect == Frame(PointF(0f,0f),Size(0,0)))
                    {
                        val x = (mi.drawableDetails.drawableframeRect.origin.x.toFloat()/ills.width.toFloat())*ims.width.toFloat()
                        val y = (mi.drawableDetails.drawableframeRect.origin.y.toFloat()/ills.height.toFloat())*ims.height.toFloat()
                        val w = (mi.drawableDetails.drawableframeRect.size.width.toFloat()/ills.width.toFloat())*ims.width.toFloat()
                        val h = (mi.drawableDetails.drawableframeRect.size.height.toFloat()/ills.height.toFloat())*ims.height.toFloat()
                        markerGroups[i].markers[j].markedInfos.originalDetails.originalFrameRect = Frame(PointF(x.toFloat(),y.toFloat()),Size(w.toInt(),h.toInt()))
                    }
                    else{
                        val x = ((mi.originalDetails.originalFrameRect.origin.x.toFloat() / ims.width.toFloat()) * ills.width.toFloat())
                        val y = ((mi.originalDetails.originalFrameRect.origin.y.toFloat() / ims.height.toFloat()) * ills.height.toFloat())
                        val w = ((mi.originalDetails.originalFrameRect.size.width.toFloat() / ims.width.toFloat()) * ills.width.toFloat())
                        val h = ((mi.originalDetails.originalFrameRect.size.height.toFloat() / ims.height.toFloat()) * ills.height.toFloat())

                        markerGroups[i].markers[j].markedInfos.drawableDetails.drawableframeRect = Frame(PointF(x.toFloat(),y.toFloat()),Size(w.toInt(),h.toInt()))
                        markerGroups[i].markers[j].shapeMarkerView.x = x - (holderSize/2)
                        markerGroups[i].markers[j].shapeMarkerView.y = y - (holderSize/2)
                        markerGroups[i].markers[j].shapeMarkerView.setLayoutParams(FrameLayout.LayoutParams(w.roundToInt() + (holderSize), h.roundToInt() + (holderSize)))
                        markerGroups[i].markers[j].reconfigureRect()
                        markerGroups[i].markers[j].reconfigureSelection()
                        markerGroups[i].markers[j].reconfigureHolders()
                    }
                }
                else if (type == PLMShapeType.LINE)
                {
                    val ims = getImageSize(context)
                    val ills = markerLayersView.layoutParams
                    if(mi.originalDetails.originalRullerPosition == LinePointsType(Point(0,0),Point(0,0))) {
                        val x1 =
                            ((mi.drawableDetails.drawableRullerPosition.point1.x.toFloat() / ills.width.toFloat()) * ims.width.toFloat())
                        val y1 =
                            ((mi.drawableDetails.drawableRullerPosition.point1.y.toFloat() / ills.height.toFloat()) * ims.height.toFloat())
                        val x2 =
                            ((mi.drawableDetails.drawableRullerPosition.point2.x.toFloat() / ills.width.toFloat()) * ims.width.toFloat())
                        val y2 =
                            ((mi.drawableDetails.drawableRullerPosition.point2.y.toFloat() / ills.height.toFloat()) * ims.height.toFloat())
                        markerGroups[i].markers[j].markedInfos.originalDetails.originalRullerPosition =
                            LinePointsType(Point(x1.toInt(), y1.toInt()),Point(x2.toInt(), y2.toInt()))
                    }
                    else{
                        val x1 = ((mi.originalDetails.originalRullerPosition.point1.x.toFloat() / ims.width.toFloat()) * ills.width.toFloat())
                        val y1 = ((mi.originalDetails.originalRullerPosition.point1.y.toFloat() / ims.height.toFloat()) * ills.height.toFloat())
                        val x2 = ((mi.originalDetails.originalRullerPosition.point2.x.toFloat() / ims.width.toFloat()) * ills.width.toFloat())
                        val y2 = ((mi.originalDetails.originalRullerPosition.point2.y.toFloat() / ims.height.toFloat()) * ills.height.toFloat())

                        markerGroups[i].markers[j].markedInfos.drawableDetails.drawableRullerPosition = LinePointsType(Point(x1.toInt(), y1.toInt()),Point(x2.toInt(), y2.toInt()))
                        markerGroups[i].markers[j].point1Holder.x = x1 - (holderSize/2)
                        markerGroups[i].markers[j].point1Holder.y = y1 - (holderSize/2)
                        markerGroups[i].markers[j].point2Holder.x = x2 - (holderSize/2)
                        markerGroups[i].markers[j].point2Holder.y = y2 - (holderSize/2)
                        markerGroups[i].markers[j].shapeMarkerView.setLayoutParams(FrameLayout.LayoutParams(markerLayersView.layoutParams))
                        markerGroups[i].markers[j].reconfigureRect()
                        markerGroups[i].markers[j].reconfigureSelection()
                    }
                }
            }
        }
    }
    fun reconfigureLastMarker()
    {
        if(markerGroups.size > 0) {
            val i = markerGroups.size - 1
            if(markerGroups[i].markers.size > 0) {
                val j = markerGroups[i].markers.size - 1
                val mi = markerGroups[i].markers[j].markedInfos
                val type = mi.type
                if (type == PLMShapeType.RECT || type == PLMShapeType.ROUND || type == PLMShapeType.TRIANGLE || type == PLMShapeType.ARCS || type == PLMShapeType.PATH) {
                    val ims = getImageSize(context)
                    val ills = markerLayersView.layoutParams
                    val x =
                        ((mi.drawableDetails.drawableframeRect.origin.x.toFloat() / ills.width.toFloat()) * ims.width.toFloat())
                    val y =
                        ((mi.drawableDetails.drawableframeRect.origin.y.toFloat() / ills.height.toFloat()) * ims.height.toFloat())
                    val w =
                        ((mi.drawableDetails.drawableframeRect.size.width.toFloat() / ills.width.toFloat()) * ims.width.toFloat())
                    val h =
                        ((mi.drawableDetails.drawableframeRect.size.height.toFloat() / ills.height.toFloat()) * ims.height.toFloat())
                    markerGroups[i].markers[j].markedInfos.originalDetails.originalFrameRect =
                        Frame(PointF(x.toFloat(), y.toFloat()), Size(w.toInt(), h.toInt()))
                }
                else if (type == PLMShapeType.LINE) {
                    val ims = getImageSize(context)
                    val ills = markerLayersView.layoutParams
                    val x1 =
                        ((mi.drawableDetails.drawableRullerPosition.point1.x.toFloat() / ills.width.toFloat()) * ims.width.toFloat())
                    val y1 =
                        ((mi.drawableDetails.drawableRullerPosition.point1.y.toFloat() / ills.height.toFloat()) * ims.height.toFloat())
                    val x2 =
                        ((mi.drawableDetails.drawableRullerPosition.point2.x.toFloat() / ills.width.toFloat()) * ims.width.toFloat())
                    val y2 =
                        ((mi.drawableDetails.drawableRullerPosition.point2.y.toFloat() / ills.height.toFloat()) * ims.height.toFloat())
                    markerGroups[i].markers[j].markedInfos.originalDetails.originalRullerPosition =
                        LinePointsType(Point(x1.toInt(), y1.toInt()),Point(x2.toInt(), y2.toInt()))
                }
            }
        }
    }
    //USABILITY---------------------------------------
    fun getImageSize(context: Context) :SizeF
    {
        val ivd = imageView.getDrawable()
        val width = ivd.intrinsicWidth
        val height = ivd.intrinsicHeight
        return SizeF(width.toFloat(),height.toFloat())
    }
    override fun getLayoutParam(): Size {
        var size = Size(500,500)
        val ims = getImageSize(context)
        val orientation = resources.configuration.orientation
        val metrics = DisplayMetrics()
        activity.windowManager?.defaultDisplay?.getMetrics(metrics)
        val height = metrics.heightPixels
        val width = metrics.widthPixels
        val g = if (height > width)  height else width
        val s = if (height < width)  height else width
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            val w = g// - 20
            val h = s - 150
            val si = (ims.width/ims.height)*h
            size = Size(w, h)
        } else {
            val w = s// - 20
            val h = g - 150
            val si = (ims.height/ims.width)*w
            size = Size(w, h)
        }

        return size
    }
    override fun getMarkerLayerLayoutParam():Size
    {
        val f = markerLayersView.layoutParams
        return  Size(f.width,f.height)
    }
    fun getImageViewContentOrigin():PointF
    {
        val drawableCoords = floatArrayOf(0f, 0f)
        imageView.imageMatrix.mapPoints(drawableCoords)
        val onViewX: Float = drawableCoords.get(0)
        val onViewY: Float = drawableCoords.get(1)
        return PointF(onViewX,onViewY)
    }
    override fun getImageSize():SizeF
    {
        return getImageSize(context)
    }
    override fun isSideLayerExpanded():Boolean
    {
        return  sideLayerExpanded
    }
    override fun toggleSideLayerExpansion()
    {
        sideLayerExpanded = !sideLayerExpanded
        if(sideLayerExpanded)
        {
            sideLayerView.sideLayerView.setLayoutParams(FrameLayout.LayoutParams(250, 500))
        }
        else{
            sideLayerView.sideLayerView.setLayoutParams(FrameLayout.LayoutParams(60, 60))

        }
        val w = sideLayerView.sideLayerView.layoutParams.width
        val h = sideLayerView.sideLayerView.layoutParams.height
        sideLayerView.layerListView.setLayoutParams(FrameLayout.LayoutParams(w, h-60))
        sideLayerView.setExpandedContractState()
    }
    override fun selectedMarkerToBeDroped(type: PLMShapeType)
    {
        markerToBeDroped = type
    }
    override fun toastMarker()
    {
        val toast = Toast.makeText(context, "toast", Toast.LENGTH_SHORT)
        toast.show()
    }
//    fun getSideLayerData():HashMap<String, List<String>>
//    {
//        val expandableListDetail = HashMap<String, List<String>>()
////        val myFavCricketPlayers: MutableList<String> =
////            ArrayList()
////        myFavCricketPlayers.add("MS.Dhoni")
////        myFavCricketPlayers.add("Sehwag")
////        myFavCricketPlayers.add("Shane Watson")
////        myFavCricketPlayers.add("Ricky Ponting")
////        myFavCricketPlayers.add("Shahid Afridi")
////        val myFavFootballPlayers: MutableList<String> = ArrayList()
////        myFavFootballPlayers.add("Cristiano Ronaldo")
////        myFavFootballPlayers.add("Lionel Messi")
////        myFavFootballPlayers.add("Gareth Bale")
////        myFavFootballPlayers.add("Neymar JR")
////        myFavFootballPlayers.add("David de Gea")
//        return expandableListDetail
//    }
}