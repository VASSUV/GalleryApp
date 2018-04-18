package ru.vassuv.appgallery.screen.start

import android.graphics.Bitmap
import android.net.Uri
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import kotlinx.android.synthetic.main.item_folder.view.*
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import ru.vassuv.appgallery.App
import ru.vassuv.appgallery.R
import ru.vassuv.appgallery.repository.dataclass.Disk
import ru.vassuv.appgallery.repository.dataclass.Files
import ru.vassuv.appgallery.repository.dataclass.Resource
import ru.vassuv.appgallery.utils.atlibrary.network.*
import ru.vassuv.appgallery.utils.atlibrary.uicomponent.SimpleAdapter
import ru.vassuv.appgallery.utils.atlibrary.uicomponent.SimpleAdapter.Companion.with
import ru.vassuv.appgallery.utils.routing.Router
import android.graphics.BitmapFactory
import kotlinx.coroutines.experimental.CommonPool
import java.io.IOException
import java.net.URL


@InjectViewState
class StartPresenter : MvpPresenter<StartView>() {
    override fun onFirstViewAttach() {
        loadList()
    }

    private var job: Job? = null

    private fun loadList() {
        Router.uiListener?.showLoader()
        job = launch(UI) {
            loadPage(0)
            Router.uiListener?.hideLoader()
        }
    }

    val adapter = with(R.layout.item_folder, arrayListOf(), object : SimpleAdapter.Binder<Resource> {
        override fun onBind(position: Int, model: Resource, holder: SimpleAdapter.SimpleViewHolder) {
            holder.itemView.nameResource.text = "$position. ${model.name}"
//            Picasso.Builder(App.context)
//                    .downloader(object : Downloader {
//
//                        val job: Job? = null
//
//                        override fun shutdown() {
//                            job?.cancel()
//                        }
//
//                        override fun load(uri: Uri?, networkPolicy: Int): Downloader.Response =
//                                Downloader.Response(runBlocking {
//                                    RETROFIT.get("""${uri?.scheme}://${uri?.host}${uri?.path}?${uri?.query}""").await().byteInputStream()
//                                }, false)
//
//                    })


            loadBitmap(model.preview) { bitmap ->
                holder.itemView.imageView.setImageBitmap(bitmap)
            }
        }
    }).initLoader(R.layout.item_loader) { position ->
        showLoader()
        job = launch(UI) {
            loadPage(position)
            hideLoader()
        }
    }

    private fun loadBitmap(url: String, callback: (Bitmap) -> Unit) {
        launch (CommonPool){
            try {
                val byteInputStream = RETROFIT.get(url).await().byteInputStream()
                val decodeStream = BitmapFactory.decodeStream(byteInputStream)
                callback(decodeStream)
            } catch (e: IOException) {
                System.out.println(e)
            }
        }
    }

    private suspend fun loadPage(position: Int) {
        val response: Response<Files> = Api.FILES.getData(hashMapOf("offset" to position.toString()))
        if (response.error == NO_ERROR && response.result != null) {
            if (position == 0) {
                adapter.list = response.result.items
                adapter.notifyDataSetChanged()
            } else
                adapter.list.addAll(response.result.items)
        } else {
            adapter.resetLoading()
            Router.uiListener?.showMessage(response.error.message ?: "")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }
}
