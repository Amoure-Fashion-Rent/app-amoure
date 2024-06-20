package com.amoure.amoure.data.response

import com.google.gson.annotations.SerializedName

data class Profile(

	@field:SerializedName("addressDetail")
	val addressDetail: String? = null,

	@field:SerializedName("phoneNumber")
	val phoneNumber: String? = null,

	@field:SerializedName("province")
	val province: String? = null,

	@field:SerializedName("district")
	val district: String? = null,

	@field:SerializedName("postalCode")
	val postalCode: String? = null,

	@field:SerializedName("fullName")
	val fullName: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("role")
	val role: String? = null,

	@field:SerializedName("birthDate")
	val birthDate: String? = null,

	@field:SerializedName("email")
	val email: String? = null
)
