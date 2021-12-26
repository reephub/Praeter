package com.praeter.core.views

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.AppCompatRadioButton
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.praeter.databinding.PremiumPlanRadioButtonContentBinding

class MyPlanRadioButton : AppCompatRadioButton {

    private var _viewBinding: PremiumPlanRadioButtonContentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _viewBinding!!

    private lateinit var view: View

    private val requestListener: RequestListener<Bitmap> = object : RequestListener<Bitmap> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any,
            target: Target<Bitmap?>,
            isFirstResource: Boolean
        ): Boolean {
            return false
        }

        override fun onResourceReady(
            resource: Bitmap?,
            model: Any,
            target: Target<Bitmap?>,
            dataSource: DataSource,
            isFirstResource: Boolean
        ): Boolean {
            //imageView.setImageBitmap(resource);
            redrawLayout()
            return false
        }
    }

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context)
    }

    fun setImageResource(resId: Int) {
        Glide.with(context)
            .asBitmap()
            .load(resId)
            .apply(
                RequestOptions.bitmapTransform(
                    MultiTransformation(
                        CenterCrop(),
                        RoundedCorners(dp2px(context, 24))
                    )
                )
            )
            .listener(requestListener)
            .submit()
    }

    fun setImageBitmap(bitmap: Bitmap?) {
        Glide.with(context)
            .asBitmap()
            .load(bitmap)
            .apply(
                RequestOptions.bitmapTransform(
                    MultiTransformation(
                        CenterCrop(),
                        RoundedCorners(dp2px(context, 24))
                    )
                )
            )
            .listener(requestListener)
            .submit()
    }

    // setText is a final method in ancestor, so we must take another name.
    fun setTitleTextWith(resId: Int) {
        binding.tvPlanTitle.setText(resId)
        redrawLayout()
    }

    fun setTitleTextWith(text: CharSequence?) {
        binding.tvPlanTitle.text = text
        redrawLayout()
    }

    // setText is a final method in ancestor, so we must take another name.
    fun setDescriptionTextWith(resId: Int) {
        binding.tvPlanDescription.setText(resId)
        redrawLayout()
    }

    fun setDescriptionTextWith(text: CharSequence?) {
        binding.tvPlanDescription.text = text
        redrawLayout()
    }

    private fun init(context: Context) {
        _viewBinding = PremiumPlanRadioButtonContentBinding.inflate(LayoutInflater.from(context))
        view = binding.root
        //imageView = view.findViewById(R.id.imageView);
        redrawLayout()
    }

    private fun redrawLayout() {
        view.isDrawingCacheEnabled = true
        view.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED)
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
        view.buildDrawingCache(true)
        //Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        //setCompoundDrawablesWithIntrinsicBounds(new BitmapDrawable(getResources(), bitmap), null, null, null);
        view.isDrawingCacheEnabled = false
    }

    private fun dp2px(context: Context, dp: Int): Int {
        return (dp * context.resources.displayMetrics.density).toInt()
    }
}