package com.revolut.androidtest.view.extensions

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.revolut.androidtest.R

fun ImageView.loadImage(uri: Int?) {
    val options: RequestOptions = RequestOptions()
        .placeholder(R.drawable.ic_launcher_background)
        .circleCrop()
        .error(R.mipmap.ic_launcher_round)
    Glide.with(this.context)
        .setDefaultRequestOptions(options)
        .load(uri)
        .into(this)
}
