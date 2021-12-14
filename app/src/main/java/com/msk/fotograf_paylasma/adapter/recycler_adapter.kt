package com.msk.fotograf_paylasma.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.msk.fotograf_paylasma.R
import com.msk.fotograf_paylasma.model.Post
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recyclerow.view.*
import java.util.zip.Inflater

class recycler_adapter(val postlist:ArrayList<Post>) : RecyclerView.Adapter<recycler_adapter.View_holder>() {
    class View_holder(view: View):RecyclerView.ViewHolder(view){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): View_holder {

        var inflater=LayoutInflater.from(parent.context)
        var view=inflater.inflate(R.layout.recyclerow,parent,false)
        return View_holder(view)
    }

    override fun onBindViewHolder(holder: View_holder, position: Int) {
        holder.itemView.recycler_row_yorum.text=postlist[position].yorum
        holder.itemView.recycler_row_mail.text=postlist.get(position).mail
        Picasso.get().load(postlist.get(position).uri).into(holder.itemView.recycler_row_image)
    }

    override fun getItemCount(): Int {
        return postlist.size
    }
}