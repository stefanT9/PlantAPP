package plantToTextAPI

interface OnTaskEventListener<T> {
    fun onSuccess(result: T)
    fun onFailure(e: Exception?)
}