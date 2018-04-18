package ru.vassuv.appgallery.utils.atlibrary.uicomponent

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Handler
import android.view.LayoutInflater
import android.view.ViewGroup
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.CompoundButton


class SimpleAdapter<M>(
        var list: ArrayList<M>,
        var layout: Int,
        private val binder: Binder<M>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var clickableIds: IntArray? = null

    var checkableIds: IntArray? = null

    interface Binder<M> {
        fun onBind(position: Int, model: M, holder: SimpleViewHolder)
    }

    abstract class SmartBinder<M> : Binder<M> {
        fun onClick(view: View, model: M, position: Int) = Unit
        fun onCheckedChange(view: View, model: M, position: Int) = Unit
    }

    fun setClickableViews(vararg ids: Int): SimpleAdapter<M> {
        this.clickableIds = ids
        return this
    }

    fun setCheckableView(vararg ids: Int): SimpleAdapter<M> {
        this.checkableIds = ids
        return this
    }

    val ITEM = 1
    val ITEM_LOADER = 2

    override fun getItemViewType(position: Int): Int {
        return if (loaderVisibility && position == list.size) ITEM_LOADER else ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleViewHolder {
        return when (viewType) {
            ITEM -> {
                val viewHolder = SimpleViewHolder(parent, layout)
                if (binder is SmartBinder<M>) {
                    clickableIds?.forEach { clickableId ->
                        viewHolder.itemView.findViewById<View>(clickableId)?.setOnClickListener { view ->
                            binder.onClick(view, list[viewHolder.adapterPosition], viewHolder.adapterPosition)
                        }
                    }

                    checkableIds?.forEach { clickableId ->
                        val view = viewHolder.itemView.findViewById<View>(clickableId)
                        if (view is CompoundButton) {
                            view.setOnCheckedChangeListener { compoundButton, b ->
                                binder.onCheckedChange(compoundButton, list[viewHolder.adapterPosition], viewHolder.adapterPosition)
                            }
                        }
                    }
                }
                viewHolder
            }
            else -> SimpleViewHolder(parent, layoutLoader)
        }
    }

    private var lastNotePosition = -1

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position < list.size)
            binder.onBind(holder.adapterPosition, list[position], holder as SimpleAdapter.SimpleViewHolder)

        if (position == list.size - 1 && lastNotePosition != position) {
            lastNotePosition = position
            loadNextPage?.invoke(this, position)
        }
    }

    override fun getItemCount() = list.size + if (loaderVisibility) 1 else 0

    private var loaderVisibility = false

    private var layoutLoader: Int = -1

    private var loadNextPage: (SimpleAdapter<M>.(position: Int) -> Unit)? = null

    fun initLoader(layoutLoader: Int, loadNextPage: SimpleAdapter<M>.(position: Int) -> Unit): SimpleAdapter<M> {
        this.layoutLoader = layoutLoader
        this.loadNextPage = loadNextPage
        return this
    }

    fun showLoader() {
        loaderVisibility = true
        Handler().post { notifyItemInserted(list.size) }
    }

    fun hideLoader() {
        loaderVisibility = false
        Handler().post { notifyItemRemoved(list.size) }
    }

    class SimpleViewHolder(parent: ViewGroup, res: Int)
        : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(res, parent, false))

    companion object {

        fun <M> with(res: Int, list: ArrayList<M>, binder: Binder<M>): SimpleAdapter<M> =
                SimpleAdapter(list, res, binder)

        fun <M> with(res: Int, list: ArrayList<M>, binder: SmartBinder<M>): SimpleAdapter<M> =
                SimpleAdapter(list, res, binder)
    }

    fun resetLoading() {
        lastNotePosition = -1
    }
}