package com.alimsuma.movieexpert.favorite

import android.content.Context
import dagger.hilt.EntryPoints

abstract class DynamicFeatureModule {
    companion object {
        fun <T> getDependencies(context: Context, clazz: Class<T>): T {
            val appContext = context.applicationContext ?: throw IllegalStateException()
            return EntryPoints.get(appContext, clazz)
        }
    }
}