package s.yarlykov.fixdataproto.presentation.chart

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.ResourcesCompat
import s.yarlykov.fixdataproto.R
import s.yarlykov.fixdataproto.domain.ChartOptions
import s.yarlykov.fixdataproto.domain.MarketData
import s.yarlykov.fixdataproto.domain.time.TimeEvent

class LineChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr), ChartView {
    /**
     * Все рисование делаем в отдельной битмапе. Потом в onDraw()
     * копируем её контент в битмапу нашей View.
     * @cacheBitmap
     * @cacheCanvas
     */
    private lateinit var cacheBitmap: Bitmap
    private lateinit var cacheCanvas: Canvas
    private lateinit var options: ChartOptions
    private var pathAxis = Path()

    private val colorBackground = ResourcesCompat.getColor(resources, R.color.colorBackground, null)

    // Кисть для осей координат и надписей на них
    private val paintAxis = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        textAlign = Paint.Align.CENTER
        textSize = 55.0f
        typeface = Typeface.create("", Typeface.NORMAL)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(cacheBitmap, 0f, 0f, null)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        // Чтобы не было утечки памяти, удалить старую битмапу перед созданием новой
        if (::cacheBitmap.isInitialized) cacheBitmap.recycle()

        pathAxis = createAxisPath(w, h)

        cacheBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        cacheCanvas = Canvas(cacheBitmap)
        cacheCanvas.drawColor(colorBackground)
//        cacheCanvas.drawPath(pathAxis, paintAxis)

        if (::options.isInitialized) {
            decorateChart()
        }
    }

    override fun setChartOptions(options: ChartOptions) {
        this.options = options
    }

    override fun update(data: List<MarketData>) {

        val xStep = width / data.size
        val yStep = height / (options.max - options.min)
        val yBase = options.min

        val path = Path()
        path.moveTo(0f, 0f)

        cacheCanvas.drawColor(colorBackground)

        data.withIndex().forEach { d ->

            val x = (xStep * d.index).toFloat()
            val y = height - ((d.value.value - yBase) * yStep).toFloat()

            path.lineTo(x, y)

            if(d.value.marker.timeEvent == TimeEvent.MINUTE) {
                cacheCanvas.drawText("m", x, y, paintAxis)
                System.out.println("APP_TAG: minute")
            }
        }

        cacheCanvas.drawPath(path, paintAxis)

        path.reset()
        invalidate()
    }

    /**
     * Надписи вдоль осей координат
     */
    private fun decorateChart() {

    }

    private fun createAxisPath(w: Int, h: Int): Path {

        val paddingH = w.toFloat() / 20f
        val paddingV = h.toFloat() / 20f

        return Path().apply {
            moveTo(paddingH, paddingV)
            lineTo(paddingH, h.toFloat() - paddingV)
            lineTo(w - paddingH, h.toFloat() - paddingV)
        }
    }
}