package com.amoure.amoure.data.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.amoure.amoure.data.response.ProductItem
import com.amoure.amoure.data.retrofit.ApiService

class ProductPagingSource(private val apiService: ApiService, private val productPsParams: ProductPSParams) : PagingSource<Int, ProductItem>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProductItem> {
        return try {
            val page = params.key ?: INITIAL_PAGE_INDEX
            val responseData = apiService.getProducts(
                productPsParams.id,
                productPsParams.ownerId,
                productPsParams.categoryId,
                productPsParams.search,
                page,
                params.loadSize).data?.products
            val responseData2: List<ProductItem> = responseData?.filterNotNull() ?: emptyList()

            LoadResult.Page(
                data = responseData2,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (responseData2.isEmpty()) null else page + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ProductItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}