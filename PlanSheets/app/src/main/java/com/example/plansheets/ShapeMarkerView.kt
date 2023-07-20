package com.example.plansheets

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import android.util.Size
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatButton
import com.example.plansheets.Sheet.*
import java.util.*
import kotlin.math.roundToInt


open class ShapeMarkerView(private val sheetBaseViewInterface : SheetBaseViewInterface, context : Context) : AppCompatButton(context) {

    val holderSize = 40
    val shapeDefaultSize = 200
    val tlHolder = View(context)
    val trHolder = View(context)
    val blHolder = View(context)
    val brHolder = View(context)
    val point1Holder = View(context)
    val point2Holder = View(context)
    val shapeMarkerView = FrameLayout(context)
    val selectionView = FrameLayout(context)
    var canMove = false

    open var markedInfos = MarkedInfoType(
        UUID.randomUUID().toString(), PLMShapeType.unknown,
        DrawableMarkerInfoType(
            Frame(PointF(0f,0f),Size(0,0)),0f,"","",0f,ArrayList(),
            LinePointsType(Point(0,0),Point(0,0))
        ),
        OriginalMarkerInfoType(
            Frame(PointF(0f,0f),Size(0,0)),0f,"","",0f,ArrayList(),"",
            LinePointsType(Point(0,0),Point(0,0))
        ),
        "",
        ImageMarkerType(ArrayList(),""),
        MeasurmentsVisibleType(false,false,false),
        false,"Draft","","","",""
    )

    open fun createShapeMarker(origin:PointF,size: Size){
        shapeMarkerView.setLayoutParams(FrameLayout.LayoutParams(size.width, size.height))
        shapeMarkerView.x = origin.x
        shapeMarkerView.y = origin.y
        shapeMarkerView.isClickable = true

//        shapeMarkerView.setBackgroundColor(Color.RED)
    }
    open fun setUpShapeMarkerMoveGesture(){
        shapeMarkerView.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                if(event != null && canMove)
                {
                    when (event!!.action) {
                        MotionEvent.ACTION_MOVE -> {
                            sheetBaseViewInterface.selectedMarker(markedInfos.thisMarkerID)
                            shapeMarkerView.x = ((event!!.getX()) +  (shapeMarkerView.x) - (shapeMarkerView.layoutParams.width/2)).toFloat()
                            shapeMarkerView.y = ((event!!.getY()) +  (shapeMarkerView.y) -  (shapeMarkerView.layoutParams.height/2)).toFloat()
                            markerMoved()
                        }
                        MotionEvent.ACTION_UP -> {
                            canMove = false
                        }
                    }

                }
                return v?.onTouchEvent(event) ?: true
            }
        })
        shapeMarkerView.setOnLongClickListener(object: View.OnLongClickListener {
            override fun onLongClick(p0: View?): Boolean {
                canMove = true
                return true
            }
        })
    }
    open fun setUpClickActions()
    {
        shapeMarkerView.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                sheetBaseViewInterface.selectedMarker(markedInfos.thisMarkerID)
                markerClicked()
                canMove = false
            }

        })
    }
    open fun getShapeMarker(context: Context): View? {

        selectionView.setBackgroundColor(Color.parseColor("#e7eecc"));
        shapeMarkerView.addView(selectionView)
        reconfigureSelection()
        return shapeMarkerView
    }
    open fun addLineHolders(context: Context): View?
    {

        setUpHolders(context, point1Holder)
        point1Holder.x = 100f
        point1Holder.y = 100f
        point1Holder.setBackgroundColor(Color.GREEN)
        setUpHolders(context, point2Holder)
        point2Holder.x = 200f
        point2Holder.y = 200f
        point2Holder.setBackgroundColor(Color.GREEN)

        return shapeMarkerView
    }
    open fun addShapeHolders(context: Context): View?
    {
        setUpHolders(context, tlHolder)
        setUpHolders(context, trHolder)
        setUpHolders(context, blHolder)
        setUpHolders(context, brHolder)

        reconfigureHolders()

        enableHolder(false)

        return shapeMarkerView
    }
    fun setUpHolders(context: Context, holder : View)
    {
        holder.setBackgroundColor(Color.BLUE);
        holder.setLayoutParams(FrameLayout.LayoutParams(holderSize, holderSize))
        holder.isClickable = true
        shapeMarkerView.addView(holder)
        holder.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {


                if(event != null)
                {
                    val touchX = event?.getX().toInt()
                    val touchY = event?.getY().toInt()

                    if(holder == tlHolder || holder == blHolder || holder == trHolder || holder == brHolder) {
                        if (holder == tlHolder) {
                            val x = shapeMarkerView.x + touchX - (holderSize / 2).toInt()
                            val y = shapeMarkerView.y + touchY - (holderSize / 2).toInt()
                            val w = (shapeMarkerView.layoutParams.width + shapeMarkerView.x) - x
                            val h = (shapeMarkerView.layoutParams.height + shapeMarkerView.y) - y
                            shapeMarkerView.setLayoutParams(
                                FrameLayout.LayoutParams(
                                    w.roundToInt(),
                                    h.roundToInt()
                                )
                            )
                            shapeMarkerView.x = x
                            shapeMarkerView.y = y
                            holder.y = 0f
                            holder.x = 0f
                            trHolder.x = (shapeMarkerView.layoutParams.width - holderSize).toFloat()
                            blHolder.y =
                                (shapeMarkerView.layoutParams.height - holderSize).toFloat()
                            brHolder.x = (shapeMarkerView.layoutParams.width - holderSize).toFloat()
                            brHolder.y =
                                (shapeMarkerView.layoutParams.height - holderSize).toFloat()
                        }
                        else if (holder == blHolder) {
                            val h = touchY + holder.y + (holderSize / 2).toInt()
                            val x = shapeMarkerView.x + touchX - (holderSize / 2).toInt()
                            val w = (shapeMarkerView.layoutParams.width + shapeMarkerView.x) - x
                            shapeMarkerView.setLayoutParams(
                                FrameLayout.LayoutParams(
                                    w.roundToInt(),
                                    h.roundToInt()
                                )
                            )
                            shapeMarkerView.x = x
                            holder.y = (shapeMarkerView.layoutParams.height - holderSize).toFloat()
                            trHolder.x = (shapeMarkerView.layoutParams.width - holderSize).toFloat()
                            brHolder.x = (shapeMarkerView.layoutParams.width - holderSize).toFloat()
                            brHolder.y =
                                (shapeMarkerView.layoutParams.height - holderSize).toFloat()
                        }
                        else if (holder == trHolder) {
                            val w = touchX + holder.x + (holderSize / 2).toInt()
                            val y = shapeMarkerView.y + touchY - (holderSize / 2).toInt()
                            val h = (shapeMarkerView.layoutParams.height + shapeMarkerView.y) - y
                            shapeMarkerView.setLayoutParams(
                                FrameLayout.LayoutParams(
                                    w.roundToInt(),
                                    h.roundToInt()
                                )
                            )
                            shapeMarkerView.y = y
                            holder.x = (shapeMarkerView.layoutParams.width - holderSize).toFloat()
                            blHolder.y =
                                (shapeMarkerView.layoutParams.height - holderSize).toFloat()
                            brHolder.x = (shapeMarkerView.layoutParams.width - holderSize).toFloat()
                            brHolder.y =
                                (shapeMarkerView.layoutParams.height - holderSize).toFloat()
                        }
                        else if (holder == brHolder) {
                            val w = touchX + holder.x + (holderSize / 2).toInt()
                            val h = touchY + holder.y + (holderSize / 2).toInt()
                            shapeMarkerView.setLayoutParams(
                                FrameLayout.LayoutParams(
                                    w.roundToInt(),
                                    h.roundToInt()
                                )
                            )
                            holder.x = (shapeMarkerView.layoutParams.width - holderSize).toFloat()
                            holder.y = (shapeMarkerView.layoutParams.height - holderSize).toFloat()
                            blHolder.y =
                                (shapeMarkerView.layoutParams.height - holderSize).toFloat()
                            trHolder.x = (shapeMarkerView.layoutParams.width - holderSize).toFloat()
                        }
                        selectionView.setLayoutParams(
                            FrameLayout.LayoutParams(
                                shapeMarkerView.layoutParams.width - holderSize + 4,
                                shapeMarkerView.layoutParams.height - holderSize + 4
                            )
                        )
                        selectionView.x = ((holderSize / 2) - 2).toFloat()
                        selectionView.y = ((holderSize / 2) - 2).toFloat()
                    }
                    else if(holder == point1Holder || holder == point2Holder){
                        if (holder == point1Holder) {
                            holder.x = (touchX + holder.x - (holderSize/2)).toFloat()
                            holder.y = (touchY + holder.y - (holderSize/2)).toFloat()
                        }
                        else if (holder == point2Holder) {
                            holder.x = (touchX + holder.x - (holderSize/2)).toFloat()
                            holder.y = (touchY + holder.y - (holderSize/2)).toFloat()
                        }
                    }
                    holderMoved()

                }
                return v?.onTouchEvent(event) ?: true
            }
        })
    }
    open fun holderMoved() {
        reconfigureMarker()
    }

    open fun reconfigureRect()
    {

    }
    open fun reconfigureSelection()
    {
        if (markedInfos.type == PLMShapeType.RECT || markedInfos.type == PLMShapeType.ROUND || markedInfos.type == PLMShapeType.TRIANGLE || markedInfos.type == PLMShapeType.ARCS || markedInfos.type == PLMShapeType.PATH) {
            selectionView.setLayoutParams(
                FrameLayout.LayoutParams(
                    shapeMarkerView.layoutParams.width - holderSize + 4,
                    shapeMarkerView.layoutParams.height - holderSize + 4
                )
            )
            selectionView.x = ((holderSize / 2) - 2).toFloat()
            selectionView.y = ((holderSize / 2) - 2).toFloat()
        }
    }
    open fun reconfigureHolders()
    {
        if (markedInfos.type == PLMShapeType.RECT || markedInfos.type == PLMShapeType.ROUND || markedInfos.type == PLMShapeType.TRIANGLE || markedInfos.type == PLMShapeType.ARCS || markedInfos.type == PLMShapeType.PATH) {
            tlHolder.x = 0f
            tlHolder.y = 0f

            trHolder.x = (shapeMarkerView.layoutParams.width - holderSize).toFloat()
            trHolder.y = 0f

            blHolder.x = 0f
            blHolder.y = (shapeMarkerView.layoutParams.height - holderSize).toFloat()

            brHolder.x = (shapeMarkerView.layoutParams.width - holderSize).toFloat()
            brHolder.y = (shapeMarkerView.layoutParams.height - holderSize).toFloat()
        }
    }
    open fun markerMoved() {
        reconfigureMarker()
    }
    fun reconfigureMarker()
    {
        val mi = markedInfos
        val type = mi.type
        val ims = sheetBaseViewInterface.getImageSize()
        val ills = sheetBaseViewInterface.getMarkerLayerLayoutParam()

        if (markedInfos.type == PLMShapeType.RECT || markedInfos.type == PLMShapeType.ROUND || markedInfos.type == PLMShapeType.TRIANGLE || markedInfos.type == PLMShapeType.ARCS || markedInfos.type == PLMShapeType.PATH) {
            markedInfos.drawableDetails.drawableframeRect.origin.x = shapeMarkerView.x + (holderSize/2)
            markedInfos.drawableDetails.drawableframeRect.origin.y = shapeMarkerView.y + (holderSize/2)
            markedInfos.drawableDetails.drawableframeRect.size = Size(shapeMarkerView.layoutParams.width - holderSize,shapeMarkerView.layoutParams.height - holderSize)

            val x = ((mi.drawableDetails.drawableframeRect.origin.x.toFloat() / ills.width.toFloat()) * ims.width.toFloat())
            val y = ((mi.drawableDetails.drawableframeRect.origin.y.toFloat() / ills.height.toFloat()) * ims.height.toFloat())
            val w = ((mi.drawableDetails.drawableframeRect.size.width.toFloat() / ills.width.toFloat()) * ims.width.toFloat())
            val h = ((mi.drawableDetails.drawableframeRect.size.height.toFloat() / ills.height.toFloat()) * ims.height.toFloat())
            markedInfos.originalDetails.originalFrameRect =
                Frame(PointF(x.toFloat(), y.toFloat()), Size(w.toInt(), h.toInt()))

        }
        else if(markedInfos.type == PLMShapeType.LINE){
            markedInfos.drawableDetails.drawableRullerPosition = LinePointsType(Point(point1Holder.x.toInt() + (holderSize/2), point1Holder.y.toInt() + (holderSize/2)),Point(point2Holder.x.toInt() + (holderSize/2), point2Holder.y.toInt() + (holderSize/2)))

            val x1 =
                ((mi.drawableDetails.drawableRullerPosition.point1.x.toFloat() / ills.width.toFloat()) * ims.width.toFloat())
            val y1 =
                ((mi.drawableDetails.drawableRullerPosition.point1.y.toFloat() / ills.height.toFloat()) * ims.height.toFloat())
            val x2 =
                ((mi.drawableDetails.drawableRullerPosition.point2.x.toFloat() / ills.width.toFloat()) * ims.width.toFloat())
            val y2 =
                ((mi.drawableDetails.drawableRullerPosition.point2.y.toFloat() / ills.height.toFloat()) * ims.height.toFloat())
            markedInfos.originalDetails.originalRullerPosition =
                LinePointsType(Point(x1.toInt(), y1.toInt()),Point(x2.toInt(), y2.toInt()))
        }
    }
    open fun markerClicked()
    {

    }
    open fun hideMenu()
    {

    }
    fun enableSelectionHolder(enable : Boolean)
    {
        if(!enable) {
            selectionView.setBackground(null)
        }
        else{
            val selectionViewShapedrawable = ShapeDrawable()
            selectionViewShapedrawable.shape = RectShape()
            selectionViewShapedrawable.paint.color = Color.RED
            selectionViewShapedrawable.paint.style = Paint.Style.STROKE
            selectionViewShapedrawable.paint.strokeWidth = 2f
            selectionView.setBackground(selectionViewShapedrawable)
        }

    }
    fun enableHolder(enable : Boolean)
    {
        if(!enable) {
            tlHolder.visibility = View.GONE
            trHolder.visibility = View.GONE
            blHolder.visibility = View.GONE
            brHolder.visibility = View.GONE
        }
        else{
            tlHolder.visibility = View.VISIBLE
            trHolder.visibility = View.VISIBLE
            blHolder.visibility = View.VISIBLE
            brHolder.visibility = View.VISIBLE
        }
        enableSelectionHolder(enable)
    }
    fun performOrientation(activity : Activity, context: Context) {

    }

}
