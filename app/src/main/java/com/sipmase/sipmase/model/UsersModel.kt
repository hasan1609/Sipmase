package com.sipmase.sipmase.model

import com.google.gson.annotations.SerializedName


data class UsersModel(

    @field:SerializedName("role")
    val role: Int? = null,

    @field:SerializedName("token_id")
    val tokenId: String? = null,

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("email_verified_at")
    val emailVerifiedAt: Any? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("username")
    val username: String? = null
)
