package com.amoure.amoure.data.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.amoure.amoure.data.response.RentItem
import com.amoure.amoure.data.retrofit.ApiService

class RentHistoryPagingSource(private val apiService: ApiService, private val rentHistoryPsParams: RentHistoryPSParams) : PagingSource<Int, RentItem>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RentItem> {
        return try {
            val page = params.key ?: INITIAL_PAGE_INDEX
            val responseData = apiService.getRents(
                rentHistoryPsParams.userId,
                page,
                params.loadSize).data?.orders
            val responseData2: List<RentItem> = responseData?.filterNotNull() ?: emptyList()

            LoadResult.Page(
                data = responseData2,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (responseData2.isEmpty()) null else page + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, RentItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}