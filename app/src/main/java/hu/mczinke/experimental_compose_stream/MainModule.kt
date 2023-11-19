package hu.mczinke.experimental_compose_stream

import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainModule = module {

    viewModel {
        StreamViewModel(application = androidApplication())
    }
}