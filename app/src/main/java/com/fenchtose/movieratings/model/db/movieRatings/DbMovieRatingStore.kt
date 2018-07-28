package com.fenchtose.movieratings.model.db.movieRatings

import android.support.annotation.WorkerThread
import com.fenchtose.movieratings.model.db.dao.MovieRatingDao
import com.fenchtose.movieratings.model.entity.RatingNotFound

class DbMovieRatingStore private constructor(private val dao: MovieRatingDao): MovieRatingStore {

    companion object {
        private lateinit var instance: DbMovieRatingStore
        fun getInstance(dao: MovieRatingDao): DbMovieRatingStore {
            if (!::instance.isInitialized) {
                instance = DbMovieRatingStore(dao)
            }

            return instance
        }
    }

    @WorkerThread
    override fun update404(title: String, year: String?) {
        dao.insert(RatingNotFound(0, title,
                year?:"", System.currentTimeMillis()/1000))
    }

    override fun was404(title: String, year: String?, timestamp: Long): Boolean {
        val count = if (year != null && year.isNotEmpty()) {
            dao.get404ForTitle(title, timestamp)
        } else {
            dao.get404ForTitle(title, timestamp, year!!)
        }

        return count > 0
    }

}