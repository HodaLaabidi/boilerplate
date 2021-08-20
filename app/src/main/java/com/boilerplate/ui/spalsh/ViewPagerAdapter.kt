package com.boilerplate.ui.spalsh

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.viewpager.widget.PagerAdapter
import com.boilerplate.R
import kotlinx.android.synthetic.main.item_onboarding_slider.view.*


class ViewPagerAdapter(private val data: List<Slider>, mContext: Context) : PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val view = LayoutInflater.from(container.context).inflate(
            R.layout.item_onboarding_slider, container, false
        )

        with(view) {
            mImage.setImageDrawable(
                AppCompatResources.getDrawable(
                context, data[position].mImage
            ))
            mTitle.text = data[position].mTitle
            mDescription.text = data[position].mDescription
        }

        container.addView(view)
        return view
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount() = data.size

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}