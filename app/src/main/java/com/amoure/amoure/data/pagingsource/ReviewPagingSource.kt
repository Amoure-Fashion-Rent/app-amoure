package com.amoure.amoure.data.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.amoure.amoure.data.response.ReviewItem
import com.amoure.amoure.data.retrofit.ApiService

class ReviewPagingSource(private val apiService: ApiService, private val reviewPsParams: ReviewPSParams) : PagingSource<Int, ReviewItem>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ReviewItem> {
        return try {
            val page = params.key ?: INITIAL_PAGE_INDEX
            val responseData = apiService.getReviews(
                reviewPsParams.id,
                reviewPsParams.productId,
                reviewPsParams.userId,
                page,
                params.loadSize).data?.reviews
            val responseData2: List<ReviewItem> = responseData?.filterNotNull() ?: emptyList()

            LoadResult.Page(
                data = responseData2,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (responseData2.isEmpty()) null else page + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ReviewItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}