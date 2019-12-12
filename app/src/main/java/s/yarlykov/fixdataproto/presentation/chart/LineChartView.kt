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

    /**
     * Paddind'и для области рисования и прямоугольник для рисования, который
     * содержит координаты (L,T R,B)
     */
    private var chartPaddings = Rect()
    private var chartArea = Rect()

    /**
     * Цвета рамки и области рисования
     */
    private val colorChartFrame = ResourcesCompat.getColor(resources, R.color.colorFrame, null)
    private val colorChartArea = ResourcesCompat.getColor(resources, R.color.colorBackground, null)

    /**
     * Кисти
     */
    private val paintChartArea = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = colorChartArea
    }

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

        // Пересчитать отступы области рисования
        with(chartPaddings) {
            left = w / 10
            top = h / 20
            bottom = h / 10
            right = w / 20
        }

        // Пересчитать координаты области рисования
        with(chartArea) {
            left = chartPaddings.left
            top = chartPaddings.top
            bottom = h - chartPaddings.bottom
            right = w - chartPaddings.right
        }

//        pathAxis = createAxisPath(w, h)

        cacheBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        cacheCanvas = Canvas(cacheBitmap)

        cacheCanvas.drawColor(colorChartFrame)
        cacheCanvas.drawRect(chartArea, paintChartArea)
//        cacheCanvas.drawPath(pathAxis, paintAxis)

        if (::options.isInitialized) {
            decorateChart()
        }
    }

    override fun setChartOptions(options: ChartOptions) {
        this.options = options
    }

    override fun update(data: List<MarketData>) {

        // Предыдущие координаты для отрисовки более плавного перехода
        // к новым координатам с помощью path.quadTo()
        var xPrev = 0f
        var yPrev = 0f

        // Область рисования и её смещения внутри View
        val w = chartArea.width()
        val h = chartArea.height()
        val shiftX = chartArea.left.toFloat()
        val shiftY = chartArea.top.toFloat()

        // Размер "единицы измерения по каждой из осей
        val xStep = w / data.size
        val yStep = h / (options.max - options.min)
        val yBase = options.min

        val pathChart = Path()

        data.withIndex().forEach { d ->

            val x = (xStep * d.index).toFloat() + shiftX
            val y = h - ((d.value.value - yBase) * yStep).toFloat() + shiftY

            if (d.index != 0) {
                pathChart.quadTo(xPrev, yPrev, (x + xPrev) / 2, (y + yPrev) / 2)

                if (d.value.marker.timeEvent == TimeEvent.MINUTE) {
                    cacheCanvas.drawText("m", x, y, paintAxis)
                }
            } else {
                pathChart.moveTo(x, y)
            }

            xPrev = x
            yPrev = y
        }

        cacheCanvas.drawColor(colorChartFrame)
        cacheCanvas.drawRect(chartArea, paintChartArea)
        cacheCanvas.drawPath(pathChart, paintAxis)

        pathChart.reset()
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