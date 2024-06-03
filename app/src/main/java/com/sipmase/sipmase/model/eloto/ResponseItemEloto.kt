package com.sipmase.sipmase.model.eloto

import com.google.gson.annotations.SerializedName

data class ResponseItemEloto(

	@field:SerializedName("sukses")
	val sukses: Int? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("grouped_data")
	val groupedData: List<GroupedDataEloto?>? = null
)

data class GroupedDataEloto(

	@field:SerializedName("data")
	val data: String? = null,

	@field:SerializedName("tw")
	val tw: String? = null,

	@field:SerializedName("tahun")
	val tahun: String? = null
)
