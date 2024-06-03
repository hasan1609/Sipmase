package com.sipmase.sipmase.model.eloto

import com.google.gson.annotations.SerializedName

data class Response(

	@field:SerializedName("sukses")
	val sukses: Int? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("grouped_data")
	val groupedData: List<GroupedDataItem?>? = null
)

data class GroupedDataItem(

	@field:SerializedName("data")
	val data: String? = null
)
