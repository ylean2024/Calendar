package com.example.android.calendar.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.android.calendar.R
import com.example.android.calendar.model.Article
import java.util.*

class NewsAdapter(private val originalArticles: List<Article>) :
    RecyclerView.Adapter<NewsAdapter.NewsViewHolder>(), Filterable {

    private var filteredArticles: List<Article> = originalArticles

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_news_recycler_view, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val article = filteredArticles[position]
        holder.bind(article)
    }

    override fun getItemCount(): Int {
        return filteredArticles.size
    }

    inner class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        private val imageView: ImageView = itemView.findViewById(R.id.imageView)

        fun bind(article: Article) {
            titleTextView.text = article.title
            descriptionTextView.text = article.description
            Glide.with(itemView)
                .load(article.urlToImage)
                .placeholder(R.drawable.placeholder)
                .into(imageView)
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList = mutableListOf<Article>()
                val filterPattern = constraint.toString().toLowerCase(Locale.getDefault()).trim()

                for (article in originalArticles) {
                    if (article.title.toLowerCase(Locale.getDefault()).contains(filterPattern)) {
                        filteredList.add(article)
                    }
                }

                val results = FilterResults()
                results.values = filteredList
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                @Suppress("UNCHECKED_CAST")
                filteredArticles = results?.values as List<Article>
                notifyDataSetChanged()
            }
        }
    }
}
