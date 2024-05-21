package com.example.fintrack

import android.widget.ImageView
import androidx.databinding.BindingAdapter

@BindingAdapter("app:srcCompat")
fun setImageResource(imageView: ImageView, resource: Int) {
    imageView.setImageResource(resource)
}