package cain.tencent.com.androidexercisedemo.domain

/**
 * @author cainjiang
 * @date 2019-05-15
 */
data class User(val userName: String = "",
                val addressList: MutableList<Address> = mutableListOf())